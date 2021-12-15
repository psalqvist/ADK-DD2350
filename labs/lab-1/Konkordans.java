import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


/**
 * Make a Concordance from a large text. Will be given corpus and rawindex.
 * rawindex: tokenized list of words and occurrence index from corpus. Created from
 * provided tokenizer.
 *
 * To be solved with lazy man hashing. Datastructures:
 * A: fixed size based on the first three letters. Points to first occurrence in I.
 * I: index of unique words created from rawindex. Points to first occurrence in P. Occurrences of word.
 * P: contains all indices of words/rawindex.
 *
 * @author Richard Liu, Philip Salqvist
 * @version 2021-09
 *
 */
public class Konkordans {
    
    private static final int SWEARRAYSIZE = 30*30*30; // combination based on the swedish alphabet
    private static final int NUMUNIQUEWORDS = 1943440; // number of unique words from rawindex
    private static final int HASHOFLASTWORD = 26999; // hash of "ööö"-word
    private static final int MAXLINESTOPRINT = 25;
    private static int[] A = new int[SWEARRAYSIZE];
    private static short[] c;
    
    // Test location
    private static final File RAWINDEX = new File("rawindex.txt");
    private static final File I_FILE = new File("I.txt");
    private static final File A_FILE = new File("A.txt");
    private static final File P_FILE = new File("P.txt");
    private static final File K_FILE = new File("korpus");
    // Location of files on presentation
/*    private static final File RAWINDEX = new File("/afs/kth.se/misc/info/kurser/DD2350/adk21/labb1/rawindex.txt");
    private static final File I_FILE = new File("/var/tmp/I.txt");
    private static final File A_FILE = new File("/var/tmp/A.txt");
    private static final File P_FILE = new File("/var/tmp/P.txt");
    private static final File K_FILE = new File("/afs/kth.se/misc/info/kurser/DD2350/adk21/labb1/korpus");*/

    /**
     * Usage in terminal: java konkordans [search_word]
     *
     * @param args the search word. Only one accepted
     * @throws IOException on file handling
     */
    public static void main(String[] args) throws IOException {
        createCharValueArray();
    
        if (args.length != 1) {
            System.out.println("Du måste söka ett ord!");
            System.exit(1);
        }

        String searchWord = args[0];

        // Create files if they don't exist
        if (!filesExist()) {
            createFiles(Mio.OpenRead(RAWINDEX));
        }
        
        loadA();
        System.out.println("Sökning av: " + searchWord);
        int result = search(searchWord);
        String resultOutput = null;
        switch (result) {
            case -1: resultOutput = "Inget ord hittades."; break;
            case 0: case 1: resultOutput = "Sökning klar."; break;
            default:
        }
        System.out.println("Resultat: " + resultOutput);
    }
    
    /**
     * Search for a word using all files/data structures to find the occurrences in korpus.
     * Memory usage should not (significantly) grow with korpus word size.
     *
     * @param word to search
     * @return -1 if not found. 0 if all found. 1 if manually exit early
     * @throws IOException from file handling
     */
    @SuppressWarnings("StatementWithEmptyBody")
    static int search(String word) throws IOException {
        int hash = lazyHash(word);
        int firstThreeBytePosition = A[hash];
        int nextThreeBytePosition;
        if (hash == HASHOFLASTWORD) { // lazy hash of "ööö"-word
            nextThreeBytePosition = firstThreeBytePosition;
        } else {
            // If no word of next increment of three letters, keep checking
            for (int i = 1; (nextThreeBytePosition = A[hash + i]) == 0; i++);
        }
        
        // Search in I-file
        int[] pByteArray = binarySearch(word, firstThreeBytePosition, nextThreeBytePosition);
        int firstPBytePosition = pByteArray[0];
        
        // Word not found
        if (firstPBytePosition == -1) return -1;
        
        int lastPBytePosition = pByteArray[1];
        int numberOfWordOccurrences = pByteArray[2];
        System.out.printf("Det finns %d förekomster av ordet.\n", numberOfWordOccurrences);
        
        // Search in P-file
        RandomAccessFile P = new RandomAccessFile(P_FILE, "r");
        RandomAccessFile K = new RandomAccessFile(K_FILE, "r");
        P.seek(firstPBytePosition);
        BufferedReader bufP = new BufferedReader(new InputStreamReader(new FileInputStream(P.getFD()), StandardCharsets.ISO_8859_1));
    
        // If last word
        if (lastPBytePosition == -1) {
            String line;
            while ((line = bufP.readLine()) != null) {
                printFromK(K, Integer.parseInt(line), word.length());
            }
        } else {
            int printedLines = 0;
            // Could also save numbers to an int[] array instead of print as they go
            while (firstPBytePosition < lastPBytePosition && printedLines < MAXLINESTOPRINT) {
                String kByteLine = bufP.readLine();
                printFromK(K, Integer.parseInt(kByteLine), word.length());
                firstPBytePosition += kByteLine.length() + 1;
                printedLines++;
                if (printedLines == MAXLINESTOPRINT - 1) {
                    System.out.println("Print more results (y?): ");
                    if (Mio.GetWord().equalsIgnoreCase("y")) {
                        printedLines = 0;
                    } else {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    
    /**
     * Binary search of word in I-file. Switches to linear search at lower distance
     *
     * @param searchWord to search in file
     * @param i lower byte position of the first three letter words
     * @param j upper byte position of the first three letter words
     * @return [byte position in P of search word, Byte position of next word, Occurrences]. -1 if word not found
     * @throws IOException from file handling
     */
    static int[] binarySearch(String searchWord, int i, int j) throws IOException {
        RandomAccessFile I = new RandomAccessFile(I_FILE, "r");
        BufferedReader bufI = new BufferedReader(new InputStreamReader(new FileInputStream(I.getFD()), StandardCharsets.ISO_8859_1));
        // pre-compile and reuse regex for performance
        Pattern p = Pattern.compile(" ");
        int[] retArray = new int[]{-1, -1, 0};
        
        // Divide and conquer search
        while (j-i > 1000) {
            int mid = i + (j-i) / 2;
            I.seek(mid);
            mid += I.readLine().length() + 1; // To adjust if seek to middle of line
            String midWord = p.split(I.readLine())[0];
            if (midWord.compareTo(searchWord) < 0) {
                i = mid;
            } else {
                j = mid;
            }
        }
        I.seek(i);
        
        // Linear search
        while (i <= j) {
            String line = bufI.readLine();
            String[] lineInfo = p.split(line);
            String linearWord = lineInfo[0];
            if (linearWord.equals(searchWord)) {
                // Starting byte position of word in P
                retArray[0] = Integer.parseInt(lineInfo[1]);
                
                // Starting byte position of next word in P
                String lineCheck = bufI.readLine();
                if (lineCheck != null) { // last line check
                    retArray[1] = Integer.parseInt(p.split(lineCheck)[1]);
                }
                
                // Number of word occurences
                retArray[2] = Integer.parseInt(lineInfo[2]);
                
                return retArray;
            }
            i += line.length() + 1;
        }
        return retArray;
    }
    
    
    /**
     * Create the files needed (A, I, P) for search to function.
     * Writes I, P concurrently to only parse rawindex once.
     *
     * A: array with index values delimited by space.
     * Values are byte positions in P of word with the first three letters of search word.
     *
     * I: file with all unique words in korpus. One unique word and info per line. Info delimited by space.
     * [word byte_position_of_word_in_P occurrence_of_word_in_korpus_and_P]
     * First value of info is byte position in P at first instance of that words byte positions in korpus.
     * Second value is number of occurrences the word occur in korpus, and number of byte positions in P.
     *
     * P: file with all byte positions of all words in korpus. Identical to rawindex if remove words and spaces.
     *
     * @param raw is rawindex input file which all other files are created from
     * @throws IOException for file handling
     */
    private static void createFiles(BufferedInputStream raw) throws IOException {
        String prevWord = "a";
        String currentWord;
        String index;
        int iBytePosition = 0;
        int pBytePosition = 0;
        int prevPBytePosition = 0;
        int wordOccurrences = 0;
        int indexLength;
        try (BufferedOutputStream iFile = new BufferedOutputStream(new FileOutputStream(I_FILE))) {
            try (BufferedOutputStream pFile = new BufferedOutputStream(new FileOutputStream(P_FILE))) {
                // EOF() works badly due to \n at EOF.
                while (!(currentWord = Mio.GetWord(raw)).equals("")) {
                    index = Mio.GetWord(raw);
                    indexLength = index.length();
    
                    // New word
                    if (!currentWord.equals(prevWord)) {
                        
                        // I
                        iFile.write((prevWord + " " + prevPBytePosition + " " + wordOccurrences + "\n").getBytes(StandardCharsets.ISO_8859_1));
    
                        // A
                        // Different words with same first 3 letters have same hash. Only first instance entered
                        // Must check if a previous word with same hash has already been entered
                        int hash = lazyHash(prevWord);
                        if (A[hash] == 0) {
                            A[hash] = iBytePosition;
                        }
                        iBytePosition += prevWord.length() + getLengthOfInt(prevPBytePosition) + getLengthOfInt(wordOccurrences) + 3;
                        prevWord = currentWord;
                        prevPBytePosition = pBytePosition;
                        wordOccurrences = 0;
                    }
                    wordOccurrences++;
                    pBytePosition += indexLength + 1;
    
                    // P
                    pFile.write(index.concat("\n").getBytes(StandardCharsets.ISO_8859_1));
                }
                // Fix for last word
                iFile.write((prevWord + " " + prevPBytePosition + " " + wordOccurrences + "\n").getBytes(StandardCharsets.ISO_8859_1));
                int hash = lazyHash(prevWord);
                if (A[hash] == 0) {
                    A[hash] = iBytePosition;
                }
                
            }
        }
        writeA();
    }
    
    /**
     * Print in K around a byte position
     *
     * @param K file to print from
     * @param bytePos in file to print around
     * @param wordLength of word
     * @throws IOException on seek/read
     */
    static void printFromK(RandomAccessFile K, int bytePos, int wordLength) throws IOException {
        byte[] byteBuffer = new byte[60 + wordLength];
        // Avoid error when near start of file
        if ((bytePos -= 30) < 0) bytePos = 0;
        K.seek(bytePos);
        K.read(byteBuffer);
        replaceLinebreakWithSpace(byteBuffer);
        System.out.println(new String(byteBuffer, StandardCharsets.ISO_8859_1));
    }
    
    /**
     * A lazy hash based on the first three characters of a word. Chosen to not cause collisions in the array.
     *
     * @param word from korpus/index that needs to be hashed
     * @return an index position for the A-array
     */
    static int lazyHash(String word) {
        // Need to buffer words with less than 3 letters
        switch (word.length()) {
            case 1: word = "  " + word; break;
            case 2: word = " " + word; break;
            default:
        }
        return c[word.charAt(0)] * 900 + c[word.charAt(1)] * 30 + c[word.charAt(2)];
    }
    
    /**
     * Replace linebreak with space
     * ISO_8859_1 new line: 0xA
     * ISO_8859_1 space: 0x20
     *
     * @param b byte array with char to replace
     */
    static void replaceLinebreakWithSpace(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0xA) b[i] = 0x20;
        }
    }
    
    /**
     * Divide and conquer for fast length of int number
     *
     * @param num to check for length
     * @return length of num
     */
    static int getLengthOfInt(int num) {
        if (num < 100000) {
            if (num < 100) {
                if (num < 10) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                if (num < 1000) {
                    return 3;
                } else {
                    if (num < 10000) {
                        return 4;
                    } else {
                        return 5;
                    }
                }
            }
        } else {
            if (num < 10000000) {
                if (num < 1000000) {
                    return 6;
                } else {
                    return 7;
                }
            } else {
                if (num < 100000000) {
                    return 8;
                } else {
                    if (num < 1000000000) {
                        return 9;
                    } else {
                        return 10;
                    }
                }
            }
        }
    }
    
    /**
     * Create a pre-calculated array for values to use with lazyHash method.
     * The letters have values 1-29, with space having value 0.
     * This also avoid repeated use of tolower() method on input.
     */
    static void createCharValueArray() {
        c = new short[247]; // highest ascii-value in swedish alphabet is ö (246)
        c[(short) ' '] = 0; // unnecessary due to 0-initialization and no collision, but here for completion
        c[(short) 'Ä'] = c[(short) 'ä'] = 27;
        c[(short) 'Å'] = c[(short) 'å'] = 28;
        c[(short) 'Ö'] = c[(short) 'ö'] = 29;
        for (short i = 1; i <= 26; i++) {
            c['A'+i-1] = c['a'+i-1] = i;
        }
    }
    
    /**
     * Load file content into an int[]. Inverse of writeA(), such as Arrays.equals(writeA[], loadA[]).
     *
     * @throws IOException on file error
     */
    static void loadA() throws IOException {
        try (BufferedInputStream aFile = new BufferedInputStream(new FileInputStream(A_FILE))) {
            // Not using Mio.EOF, but A.length due to GetInt() throw err at end
            for (int i = 0; i < A.length; i++) {
                A[i] = Mio.GetInt(aFile);
            }
        }
    }
    
    /**
     *  Write hash array A to file to be loaded later when needed.
     *  Each array index delimited by space " ".
     *  Inverse of loadA(), such as Arrays.equals(writeA[], loadA[]).
     *
     * @throws IOException on write I/O error
     */
    static void writeA() throws IOException {
        try (BufferedOutputStream aFile = new BufferedOutputStream(new FileOutputStream(A_FILE))) {
            for (int iBytePosition : A) {
                // Obs: file will end with space
                String s = iBytePosition + " ";
                aFile.write(s.getBytes(StandardCharsets.ISO_8859_1));
            }
        }
    }
    
    /**
     * Check if files already exist. Run to decide if files should be created
     *
     * @return true if all files exist
     */
    static boolean filesExist() {
        return I_FILE.exists() && A_FILE.exists() && P_FILE.exists();
    }
    
    /**
     * Takes a buffered input stream from the raw index file and returns #uniquewords
     * Always same number (1943440) in lab so method should be run before evaluation
     *
     * @param bis stream from file
     * @return number of unique words
     */
    @SuppressWarnings("StatementWithEmptyBody")
    static int getUniqueWords(BufferedInputStream bis) {
        int uniqueWords = 0;
        String prevWord = "";
        String currentWord;
        assert bis != null;
        while (!Mio.EOF(bis)) {
            currentWord = Mio.GetWord(bis);
            if (!currentWord.equals(prevWord)) {
                uniqueWords++;
                prevWord = currentWord;
            }
            while (Mio.GetChar(bis)!='\n');
        }
        return uniqueWords;
    }
}
