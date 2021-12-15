# Konkordans

En konkordans är en databas där man kan slå upp ord och då få se alla förekomster av ordet tillsammans med orden närmast före och närmast efter i texten. Detta är ett stort hjälpmedel för lingvister som vill undersöka hur olika ord används i språket.

I denna uppgift ska du skriva ett program som givet en text skapar en konkordansdatabas och ett program som frågar användaren efter ord, slår upp ordet och presenterar alla förekomster av ordet i sitt sammanhang. Det är viktigt att varje sökning går mycket snabbt så det gäller att det första programmet lagrar konkordansen på ett sådant sätt att det går snabbt att göra en sökning.

Exempel på körning av sökprogrammet:

```
$ java Konkordans komplexiteten
Det finns 7 förekomster av ordet.
ta på scen. Breddningsarbete. Komplexiteten har denna innebörd bland anna
räckvidden, hastigheterna och komplexiteten i omvärlden ökar. Domen inneb
 beter sig misstänkt? Ändå är komplexiteten så hög att jag stundtals blir
ttsplatsen. Vi är medvetna om komplexiteten i denna fråga och ser med oro
n. I det övriga materialet är komplexiteten sedan så stor att en fantasif
 av den från 1928 tilltagande komplexiteten i skattelagstiftningen. De då
ttelseorganisationen CIA. Men komplexiteten hos de föreningar som kemiste
```

## Krav
Följande krav ställs på din lösning:

* Programmet ska vara skrivet i ett riktigt programspråk och inte något operativsystemnära skriptspråk eller liknande.

* Konkordansen ska inte skilja på stora och små bokstäver. Användaren ska alltså kunna skriva in alla sökfrågor med små bokstäver.

* Det givna programmet tokenizer.c på kurskatalogen (se nedan) definierar hur texten ska delas upp i enskilda ord.
Konstruktionsprogrammet behöver inte vara jättesnabbt eftersom det bara ska köras en gång, men det måste vara någorlunda effektivt så att det kan skapa konkordansen på rimlig tid. Det får inte ta mer än tre minuter att skapa konkordansen på en Ubuntudator.

* Sökprogrammets utmatning ska inledas med en rad som anger antalet förekomster. Därefter ska varje förekomst av ordet presenteras på varje rad med till exempel 30 tecken före och 30 tecken efter. Ersätt radbyten med mellanslag. Om det finns fler än 25 förekomster ska programmet fråga användaren om hon vill ha förekomsterna utskrivna på skärmen.

* Man ska kunna söka efter ett ord, till exempel "bil", genom att i terminalfönstret ge kommandot konkordans bil (Om du använt C, C++ eller liknande) eller java Konkordans bil (om du använt Java). 

* Svaret (som alltså innehåller antalet förekomster men högst 25 rader med förekomster) måste komma inom en sekund på en av skolans Ubuntudatorer. Vid redovisningen ska programmet exekveras på en av skolans Ubuntudatorer (via fjärrinloggning, se nedan).

* Sökprogrammet ska inte läsa igenom hela texten och får inte använda speciellt mycket internminne. Internminnesbehovet ska inte växa med antalet distinkta ord i den ursprungliga texten (med andra ord ha konstant internminneskomplexitet). Du ska därför använda latmanshashning (se föreläsning 3) som datastruktur.

## Teoriuppgifter
1. Vilka är rollerna vid parprogrammering och vilka uppgifter har varje roll?
2. Indexinformationen för ett ord (det vill säga i vilka teckenpositioner ordet förekommer i den stora texten) kan bli mycket stor. Hur bör positionerna lagras för att det ska bli effektivast, som text eller binärt (data streams i Java)? Bör indexinformationen lagras tillsammans med själva ordet eller på ett separat ställe?

3. I denna labb ska datastrukturen för konkordansen huvudsakligen ligga på fil, vilket betyder att sökningar görs i filen istället för som vanligt i       internminnet. Det påverkar till exempel hur man representerar pekare (lämpligen som bytenummer i filen). Diskutera för- och nackdelar med olika   implementationer av konkordansen med avseende på följande egenskaper:

  * snabbhet (antal filläsningar och filpositioneringar per sökning),

  * minneskomplexitet för fillagringen (bara konstant mycket internminne ska användas)

  * enkelhet att konstruera och lagra på fil.

  Ta åtminstone upp följande datastrukturer:

  * binärt sökträd,

  * sorterad array,

  * hashtabell,

  * trie (träd där varje nivå motsvarar en bokstav i ordet),

  * latmanshashning

  Redovisa för- och nackdelarna i en tabell. 

4. Ge exempel på minst 7 indata (dvs ord) som är lämpliga testfall i labb 1 och motivera varför.


