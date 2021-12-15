import java.io.*                                                            ;
/** Enkel in- och utmatning, Henrik Eriksson, 1997-02-03 */
class Mio                                                                   {
  static BufferedInputStream keyboard = (BufferedInputStream)System.in      ;
 
  static boolean IsWhitespace(char c)
  { return c == ' ' || c == '\t' || c == '\n' || c == '\r'; }

  /** öppna filen för läsning */
  public static BufferedInputStream OpenRead(File s)                        {
    try                                                                     {
        return new BufferedInputStream(new FileInputStream(s))              ;}
    catch (IOException e)                                                   {
        System.err.println("Kan inte öppna "+s)                             ;}
    return null                                                             ;}

  /** Läs en rad från filen, glufsa radbytet, returnera raden */
  public static String GetLine(BufferedInputStream in)                      {
    StringBuilder word = new StringBuilder()                                ;
    char tkn                                                                ;
    if (EOF(in)) return ""                                                  ;
    while((tkn=GetChar(in))!='\n') word.append(tkn)                         ; 
    return word.toString()                                                  ;}

  /** Läs en rad från tangenterna, glufsa radbytet, returnera raden */
  public static String GetLine()                                            {
    return GetLine(keyboard)                                                ;}

  /** Läs ett ord avgränsat av blanka från filen och returnera det */
  public static String GetWord(BufferedInputStream in)                      {
    StringBuilder word = new StringBuilder()                                ;
    SkipBlanks(in)                                                          ;
    if (EOF(in)) return ""                                                  ;
    while(!IsWhitespace(NextChar(in))) word.append(GetChar(in))             ;
    return word.toString()                                                  ;}

  /** Läs ett ord avgränsat av blanka från tangenterna och returnera det */
  public static String GetWord()                                            {
    return GetWord(keyboard)                                                ;}

  /** Läs ett heltal avgränsat av blanka från filen och returnera det */
  public static int GetInt(BufferedInputStream in)                          {
    String word=GetWord(in)                                                 ;
    try                                                                     {
      return Integer.parseInt(word)                                         ;}
    catch(NumberFormatException e)                                          {
      System.err.println("Not an integer:"+word)                            ;}
    return 0                                                                ;}

  /** Läs ett heltal avgränsat av blanka från tangenterna och returnera det */
  public static int GetInt()                                                {
    return GetInt(keyboard)                                                 ;}

  /** Läs ett doubletal avgränsat av blanka från filen och returnera det */
  public static double GetReal(BufferedInputStream in)                      {
    String word=GetWord(in)                                                 ;
    try                                                                     {
      return Double.parseDouble(word)                                       ;}
    catch(NumberFormatException e)                                          {
      System.err.println("Not a number:"+word)                              ;}
    return 0                                                                ;}

  /** Läs ett doubletal avgränsat av blanka från tangenterna och returnera det */
  public static double GetReal()                                            {
    return GetReal(keyboard)                                                ;}

  /** Läs ett tecken från filen och returnera det */
  public static char GetChar(BufferedInputStream in)                        {
    char tkn='\000'                                                         ;
    try                                                                     {
      tkn=(char)in.read()                                                   ;}
    catch(IOException e)                                                    {
      System.err.println("Fel i GetChar")                                   ;}
    return tkn                                                              ;}

  /** Läs ett tecken från tangenterna och returnera det */
  public static char GetChar()                                              {
    return GetChar(keyboard)                                                ;}

  /** Tjuvtitta på nästa tecken i filen och returnera det */
  public static char NextChar(BufferedInputStream in)                       {
    char tkn='\000'                                                         ;
    in.mark(1)                                                      ;
    try                                                                     { 
      tkn=(char)in.read()                                                   ;
      in.reset()                                                            ;}
    catch (IOException e)                                                   {
      System.err.println("Fel i NextChar\n")                                ;}
    return tkn                                                              ;}

  /** Tjuvtitta på nästa tecken från tangenterna och returnera det */
  public static char NextChar()                                             {
    return NextChar(keyboard)                                               ;}

  /** Glufsa eventuella blanka på tur att läsas i filen */
  public static void SkipBlanks(BufferedInputStream in)                     {
    while(IsWhitespace(NextChar(in))) GetChar(in)                           ;}

  /** Glufsa eventuella blanka på tur att läsas från tangenterna */
  public static void SkipBlanks()                                           {
    SkipBlanks(keyboard)                                                    ;}

  /** Returnera true om filen är slutläst */
  public static boolean EOF(BufferedInputStream in)                         {
    int n=0                                                                 ;
    in.mark(1)                                                      ;
    try                                                                     {
      n=in.read()                                                           ;
      in.reset()                                                            ;}
    catch (IOException e)                                                   {
      System.err.println("Fel i EOF\n")                                     ;}
    return (n==-1)                                                          ;}

  /** Returnera true om ctrl-D står i tur från tangenterna */
  public static boolean EOF()                                               {
    return EOF(keyboard)                                                    ;}
}

