# Run from terminal
1. javac FlowStep3.java
2. javac Combine.java
3. If on OSX -> ./../help-files/OSX/bipgen 5000 5000 10000 > graph.txt
4. If not -> ./../help-files/bipgen 5000 5000 10000 > graph.txt
5. java Combined < graph.txt > match.txt

# Flöden och matchningar

Du ska i tre steg skriva ett program som får en bipartit graf som indata och producerar en matchning av maximal storlek som utdata genom att reducera (transformera) matchningsproblemet till flödesproblemet. Korrekthet och effektivitet testas genom att lösningarna på de tre stegen skickas till Kattis (Länkar till en externa sida.). För att klara labben ska du bli godkänd av Kattis på de tre stegen samt redovisa labben för en handledare. Kattis kontrollerar både att programmet gör rätt och att det löser problemet tillräckligt snabbt. Kattis klarar av programspråken Java, C, C++ och Python, men tidskraven i denna labb gör att vi avråder från Python.

## Steg 1: Reducera problemet till flödesproblemet

Du ska skriva ett program som löser matchningsproblemet med hjälp av en svart låda som löser flödesproblemet. Programmet ska fungera enligt denna översiktliga programstruktur:

*  Läs indata för matchningsproblemet från standard input.
*  Översätt matchningsinstansen till en flödesinstans.
*  Skriv indata för flödesproblemet till standard output (se till att utdata flushas).
*  Den svarta lådan löser flödesproblemet.
*  Läs utdata för flödesproblemet från standard input.
*  Översätt lösningen på flödesproblemet till en lösning på matchningsproblemet.
*  Skriv utdata för matchningsproblemet till standard output.

Se nedan hur in- och utdataformaten för matchnings- och flödesproblemen ser ut.

Ditt program ska lösa problemet effektivt. Kattis kommer att provköra programmet på bipartita grafer på upp till (5000+5000) hörn och upp till 10000 kanter. Kattis känner till problemet som [https://kth.kattis.com/problems/oldkattis.adkreducetoflow](oldkattis.adkreducetoflow) (reflektera gärna över varför problemet heter *reduce TO flow*).

Det finns ett programskelett för steg 1 i några olika språk på katalogen /afs/nada.kth.se/info/adk20/labb3/exempelprogram

## Steg 2: Lös flödesproblemet

Nu ska du skriva ett program som löser flödesproblemet. Programmet ska läsa indata från standard input och skriva lösningen till standard output. Se nedan hur in- och utdataformaten för flödesproblemet ser ut.

Ditt program ska lösa problemet effektivt. Kattis kommer att provköra programmet på generella flödesgrafer på upp till 2000 hörn och 10000 kanter. Kattis känner till problemet som [https://kth.kattis.com/problems/oldkattis.adkmaxflow](oldkattis.adkmaxflow).

## Steg 3: Kombinera steg 1 & 2

I steg 1 löste du matchningsproblemet med hjälp av en lösning till flödesproblemet. I steg 2 löste du flödesproblemet. Nu ska du kombinera dessa lösningar till ett enda program genom att byta ut kommunikationen av flödesinstansen över standard input och standard output till ett funktionsanrop. Programmet ska fortfarande läsa indata från standard input och skriva lösningen till standard output.

Ditt program ska lösa problemet effektivt. Kattis kommer att provköra programmet på bipartita grafer på upp till (5000+5000) hörn och upp till 10000 kanter. Kattis känner till problemet som [https://kth.kattis.com/problems/oldkattis.adkbipmatch](oldkattis.adkbipmatch).

### Matchningsproblemet
Givet en bipartit graf G = (X,Y,E) finn en maximal matchning.

### Indata

Den första raden består av två heltal som anger antalet hörn i X respektive Y.
Den andra raden består av ett tal som anger |E|, det vill säga antalet kanter i grafen.
De följande |E| raderna består var och en av två heltal som svarar mot en kant.

Hörnen numreras från 1 och uppåt. Om man angett a hörn i X och b hörn i Y så låter vi X = {1, 2,..., a} och Y = {a+1, a+2,..., a+b}. En kant anges med ändpunkterna (först X-hörnet och sedan Y-hörnet).

*Exempel*: En graf kan till exempel kodas så här.

```
2 3
4
1 3
1 4
2 3
2 5
```

Denna graf har alltså X = {1, 2} och Y = {3, 4, 5}. Kantmängden *E* innehåller kanterna (1, 3), (1, 4), (2, 3) och (2, 5).

### Utdata

Först skrivs en rad som är densamma som den första i indata, och därefter en rad med ett heltal som anger antalet kanter i den funna matchningen. Därefter skrivs en rad för varje kant som ingår i matchningen. Kanten beskrivs av ett talpar på samma sätt som i indata.

*Exempel*: Om vi har grafen ovan som indata så kan utdata se ut så här.

```
2 3
2
1 3
2 5
```

### Flödesproblemet
Givet en flödesgraf G = (V,E) finn ett maximalt flöde. Lös flödesproblemet med Edmonds-Karps algoritm, det vill säga Ford-Fulkersons algoritm där den kortaste stigen hittas med breddenförstsökning.

### Ford-Fulkersons algoritm i pseudokod

c[u,v] är kapaciteten från u till v, f[u,v] är flödet, cf[u,v] är restkapaciteten.

```
for varje kant (u,v) i grafen do 
    f[u,v]:=0; f[v,u]:=0 
    cf[u,v]:=c[u,v]; cf[v,u]:=c[v,u] 
while det finns en stig p från s till t i restflödesgrafen do 
    r:=min(cf[u,v]: (u,v) ingår i p) 
    for varje kant (u,v) i p do 
         f[u,v]:=f[u,v]+r; f[v,u]:= -f[u,v] 
         cf[u,v]:=c[u,v] - f[u,v]; cf[v,u]:=c[v,u] - f[v,u]
```

### Indata

Den första raden består av ett heltal som anger antalet hörn i V.
Den andra raden består av två heltal s och t som anger vilka hörn som är källa respektive utlopp.
Den tredje raden består av ett tal som anger |E|, det vill säga antalet kanter i grafen.
De följande |E| raderna består var och en av tre heltal som svarar mot en kant.

Hörnen numreras från 1 och uppåt. Om man angett a hörn i V så låter vi V = {1, 2,..., a}. En kant anges med ändpunkterna (först från-hörnet och sedan till-hörnet) följt av dess kapacitet.

*Exempel*: En graf kan till exempel kodas så här.

```
4
1 4
5
1 2 1
1 3 2
2 4 2
3 2 2
3 4 1
```

### Utdata

Den första raden består av ett heltal som anger antalet hörn i V.
Den andra raden består av tre heltal s,t, samt flödet från s till t.
Den tredje raden består av ett heltal som anger antalet kanter med positivt flöde.
Därefter skrivs en rad för varje sådan kant. Kanten beskrivs av tre tal på liknande sätt som i indata, men i stället för kapacitet har vi nu flöde.

*Exempel*: Om vi har grafen ovan som indata så kan utdata se ut så här.

```
4
1 4 3
5
1 2 1
1 3 2
2 4 2
3 2 1
3 4 1
```

## Teoriuppgifter

1. Jämför tidskomplexiteten för Edmonds-Karps algoritm då grafen implementeras som en grannmatris och då den implementeras med grannlistor. (För att satsen f[v,u]:= -f[u,v] ska kunna implementeras effektivt måste grannlisteimplementationen utökas så att varje kant har en pekare till den omvända kanten.)
Uttryck tidskomplexiteten i n och m där n är totala antalet hörn och m antalet kanter i den bipartita grafen. Välj sedan den implementation som är snabbast då m=O(n), alltså då grafen är gles.

2. Kalle menar att om vi börjar med en bipartit graf G och gör om den till en flödesgraf H med ny källa s och nytt utlopp t så kommer avståndet från s till t att vara 3.
Kalle tycker därför att BFS-steget alltid kommer att hitta en stig av längd 3 i restflödesgrafen (om det finns någon stig).

Det första påståendet är sant, men inte det andra. Varför har stigarna som BFS hittar i restflödesgrafen inte nödvändigtvis längd 3? Hur långa kan de bli?

3. Anledningen till att bipartit matchning kan reduceras till flöde är att en lösning till flödesproblemet kan tolkas som en lösning till matchningsproblemet. Detta gäller bara om det flöde som algoritmen ger är ett heltalsflöde (flödet i varje kant är ett heltal), vilket i detta fall innebär att flödet längs en kant antingen är 0 eller 1. Som tur är så är det på det sättet.
*  Bevisa att Ford-Fulkerson alltid genererar heltalsflöden om kantkapaciteterna är heltal!
*  Vad händer med lösningarna som flödesalgoritmen ger om man ändrar i reduktionen så att kantkapaciteterna sätts till 2 istället för 1? 
