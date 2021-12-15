
Labb 4
======

*   Inlämningsdatum 22 nov av 12:00
*   Poäng 1

NP-fullständighetsreduktioner - Rollbesättning
----------------------------------------------

_Om du redovisar labben senast den 22 november får du en labbleveranspoäng, som kan ge högre betyg på labbmomentet. Till labben hör [teoriuppgifter](/courses/27078/assignments/167042 "Teroriuppgifter till labb 4") som kan redovisas för en teoripoäng till tentan, och detta görs på övningen den 10 november (ingen annan redovisningsmöjlighet finns). Det är frivilligt att redovisa teoriuppgifterna, men för att klara av att göra labben bör du ha gjort dom._ 

Målen för labb 4 är att du ska

*   öva på att hitta ett lämpligt problem att reducera till ett problem som ska visas NP-svårt,
*   konstruera och implementera en enklare polynomisk reduktion mellan givna problem,
*   visa att ett problem är NP-fullständigt.

Ansvarig för castingen på ett filmbolag behöver koppla ihop rätt skådespelare med rätt roller. Samma person kan spela flera roller, men samma roll kan endast innehas av en person. Manus anger vilka roller som är med i samma scener. Inga monologer får förekomma. Varje skådespelare får bara ha en roll i varje scen. Varje roll måste förekomma i minst en scen.

Dessutom är divorna _p1_ och _p2_ garanterade att få (minst) en roll var (och de rollerna ska självklart delta i åtminstone en scen var). Detta medför extraarbete eftersom de båda inte tål varandra och rollerna ska besättas så att de aldrig spelar mot varandra. Rollbesättningsproblemet är att avgöra ifall alla roller kan besättas med de skådespelare som finns till hands. Ingående parametrar är alltså:   
Roller _r1_, _r2_,... , _rn_   
Skådespelare _p1_, _p2_,... ,_pk_   
Villkor typ 1 (till varje roll): _rt_ kan besättas av _p1_, _p2_, _p6_   
Villkor typ 2 (till varje scen): i _su_ medverkar _r1_, _r3_, _r5_, _r6_ och _r7_

#### **Indataformat  
**De tre första raderna består av talen _n_, _s_ och _k_ (antal roller, antal scener och antal skådespelare, _n_≥1, _s_≥1, _k_≥2), ett tal per rad. 

De följande _n_ raderna representerar villkoren av typ 1 och börjar med ett tal som anger antalet efterföljande tal på raden, följt av de möjliga skådespelarnas nummer (mellan 1 och _k_, kursiverade i exemplen nedan).   
De sista _s_ raderna är villkor av typ 2 och börjar ett tal som anger antalet efterföljande tal på raden, följt av tal som representerar de olika rollerna som är med i respektive scen. Varje roll förekommer högst en gång på varje sådan rad, så antalet roller på en rad ligger mellan 2 och _n_.

Fråga: Kan rollerna besättas med högst _k_ st skådespelare så att _p1_ och _p2_ deltar men inte är med i samma scener som varandra?

Exempel på godkända indata

**nej-instans:**

5  
5   
3
3 _1 2 3_
2 _2 3_
2 _1 3_
1 _2_
3 _1 2 3_
2 1 2
2 1 2
3 1 3 4
2 3 5
3 2 3 5

      

**ja-instans:**

6  
5  
4
3 _1 3 4_
2 _2 3_
2 _1 3_
1 _2_
4 _1 2 3 4_
2 _1 4_
3 1 2 6
3 2 3 5
3 2 4 6
3 2 3 6
2 1 6 

#### **Uppgift  
**I den här laborationen ska du visa att rollbesättningsproblemet är NP-svårt genom att reducera ett känt NP-fullständigt problem, som finns inlagt i Kattis. Din reducerade instans kommer att granskas och lösas av Kattis. Du får välja mellan att reducera problemen Graffärgning (problem-id [kth.adk.npreduction1](https://kth.kattis.com/problems/kth.adk.npreduction1)) och Hamiltonsk cykel (problem-id [kth.adk.npreduction2](https://kth.kattis.com/problems/kth.adk.npreduction2)). Indataformat för dessa problem beskrivs nedan. Din uppgift är alltså att implementera en reduktion, inte att lösa problemet. En Karpreduktion transformerar en instans av ett beslutsproblem (A) till en instans av ett annat beslutsproblem (B). Programmet ska alltså som indata ta en A-instans och som utdata generera en B-instans. Tänk noga igenom vilket problem som är A och vilket som är B.

Kattis testar om din reduktion är korrekt, men du måste naturligtvis kunna bevisa att den är det vid redovisningen. Kattis svar är egentligen avsedda att vägleda dig i arbetet med beviset och påpeka om du glömt något viktigt specialfall. Vid redovisningen kommer handledaren också att fråga varför problemet ligger i NP och vad komplexiteten är för din reduktion.

Vid rättningen utnyttjas en lösare för instanser av ett (annat) NP-fullständigt problem inom rimliga storleksgränser. Av tekniska skäl har Kattis en maximal tillåten storlek på instanserna. Du får bara meddelanden om den ifall du skickar in en för stor instans. Du får redovisa din reduktion om du kan bevisa att den är korrekt, oavsett om Kattis har godkänt den eller inte.

#### **Tips**

Det kan underlätta om man använder en verifikator för den producerade lösningen, så att man upptäcker om en otillåten instans produceras vid reduktionen. En av kursens assistenter har skrivit en verifikator som finns kompilerad för KTH:s datorsystem på /afs/kth.se/misc/info/kurser/DD2350/adk21/labb4/verifyLab4

Verifikatorn förväntar sig att få utdata från er reduktion på standard input. Kör verifikatorn med t.ex.

/afs/kth.se/misc/info/kurser/DD2350/adk21/labb4/verifyLab4 < out.txt

där out.txt är en fil som innehåller utdata från er reduktion.

#### **_Graffärgning  
_**Indata: En oriktad graf och ett antal färger _m_. Isolerade hörn och dubbelkanter kan förekomma, inte öglor.

Fråga: Kan hörnen i grafen färgas med högst _m_ färger så att inga grannar har samma färg?

Indataformat:   
Rad ett: tal V (antal hörn, ![tex:\displaystyle 1 \leq V \leq 300](https://www.kth.se/api/webtex/v1/WebTex?D=1&tex=%5Cdisplaystyle+1+%5Cleq+V+%5Cleq+300))   
Rad två: tal E (antal kanter, ![tex:\displaystyle 0\le E\le 25000](https://www.kth.se/api/webtex/v1/WebTex?D=1&tex=%5Cdisplaystyle+0%5Cle+E%5Cle+25000))   
Rad tre: mål _m_ (max antal färger, ![tex:\displaystyle 1\le m\le 2^{30}](https://www.kth.se/api/webtex/v1/WebTex?D=1&tex=%5Cdisplaystyle+1%5Cle+m%5Cle+2%5E%7B30%7D))   
En rad för varje kant (E stycken) med kantens ändpunkter (hörnen numreras från 1 till V)

#### **_Hamiltonsk cykel  
_**Indata: En riktad graf.

Fråga: Finns det en tur längs kanter i grafen som börjar och slutar på samma ställe och som passerar varje hörn exakt en gång?

Indataformat:   
Rad ett: tal V (antal hörn, ![tex:\displaystyle 1\le V\le 200](https://www.kth.se/api/webtex/v1/WebTex?D=1&tex=%5Cdisplaystyle+1%5Cle+V%5Cle+200))   
Rad två: tal E (antal kanter ![tex:\displaystyle 0\le E\le 5000](https://www.kth.se/api/webtex/v1/WebTex?D=1&tex=%5Cdisplaystyle+0%5Cle+E%5Cle+5000))   
En rad för varje kant (E stycken) med kantens starthörn och sluthörn (hörnen numreras från 1 till V)
