# Testaus

## Yleisesti
Projektin luokkien ja metodien testit toteutettiin JUnit-yksikkötesteillä. Automaattisten testien ulkopuolelle jätettiin käyttöliittymän testaus, eli kansion _ui_ -sisältö. Käyttöliittymän toimintaa on kuitenkin testattu manuaalisesti ja pyritty löytämään mahdolliset virhetilanteita aiheuttavat skenaariot. Testikattavuutta voi tarkastella [codecovissa](https://codecov.io/gh/chipfrog/Shortest-path-visualizer) tai generoimalla jacocon testikattavuusraportin [käyttöohjeissa](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/manual.md) kuvatulla tavalla. Algoritmien suorituskykyä mittaava PerformanceTest-luokka ei myöskään kuulu testikattavuuteen ja on erillinen JUnit-testeistä. Suorituskykyteisteistä ja niiden tuloksista dokumentin lopussa.

## Yksikkötestaus
JUnit-yksikkötesteillä pyrittiin varmistamaan yksittäisten luokkien ja metodien oikeanlainen toiminta. Testeissä on pyritty testaamaan, että luokat toimivat oikein ainakin ns. "normaalitilanteissa" ja yleisimmissä rajatapaustilanteissa. 

### Utility-luokat
Esimerkiksi naapurisolmujen hakemisesta vastaavan luokan NeighbourFinder toimintaa on testattu syöttämällä sille char[][]-mutoinen testikartta, jossa sille on annettu erilaisia koordinaatteja, ja luokan on osattava kertoa, kuinka monta naapurisolmua kyseisellä koordinaatin pisteellä on. Eräs annettu piste on kartan rajalla, jolloin luokka ei saa etsiä naapurisolmuja kartan rajojen ulkopuolelta ja toinen annettu piste on este, jolloin sillä ei saa olla yhtään naapuria. Testeissä on myös tilanteita, jossa pisteellä on muutamalla puolella esteitä ja tilanne, jossa esteitä ei ole ympärillä ollenkaan. Oikeat vastaukset on laskettu kartasta manuaalisesti ja katsottu, että testit antavat samat vastaukset. Matemaattisista funktioista vastaava MathFunctions luokkaa on myös testattu laskemalla muutamaan tapaukseen itse oikeat vastaukset ja katsomalla, että luokka vastaa samoin.

### Minimikeko
Minimikeon toteuttavaa Keko-luokkaa on testattu varmistamalla, että keko on aluksi tyhjä ja muuttuu ei-tyhjäksi ensimmäisen solmun lisäyksen myötä. Keolle on syöytetty etäisyydeltään eriarvoisia solmuja ja tarkistettu, että ensimmäisenä ulos otettavalla solmulla todella on pienin arvo. Keon järjestyksen muuttuminen vastavaasti varmistettiin poistamalla toinenkin solmu, jolloin nähtiin, että se oli arvoltaan toisiksi pienin. Solmut annettiin keolle (pseudo)satunnaisessa järjestyksessä, jolloin voitiin varmistua, ettei keko anna solmuja oikeassa järjestyksessä vain tietyn syöttöjärjestyksen takia (esim. solmut valmiiksi pienimmästä suurimpaan).

### Algoritmit
Algoritmien perustoiminta testattiin keskenään lähes identtisellä tavalla, joten en erittele tähän jokaisen algoritmin JUnit-testejä. Algoritmeille annettiin eri tilanteita kuvaavia char[][]-muotoisia testikarttoja ja varmistettiin algoritmien toimivuus näissä tilanteissa. Karttoihin oli sijoitettuna valmiiksi lähtö- ja maalisolmu. Etäisyys pisteiden välillä laskettiin selkeissä tilanteissa (esim. 5 ruutua suoraan eteenpäin x-akselilla) manuaalisesti ja katsottiin, että algoritmi saa saman tuloksen. Vastaavasti solmut, jotka algoritmin tulisi avata, laskettiin manuaalisesti ja katsottiin, että algoritmi ilmoittaa saman solmujen lukumäärän. Testejä toki rajoitti se, ettei kovin monumtkaisia tilanteita voinut testata, sillä reitin kasvaessa manuaalisesti laskettavien solmujen määrästä tulee suuri. Osa kartoista oli erikoistapauksia, kuten tilanne, jossa maalisolmua ympäröi esteet joka puolella, ja algoritmin tuli tällöin ilmoittaa ettei maaliin pääsee sen sijaan, että ohjelma kaatuisi. Hieman monmimutkaisemmassa reitissä  A*:n ja JPS:n testikartassa saamaa etäisyyttä verrattiin Dijkstran saamaan etäisyyteen, sillä Dijkstran toiminta on todettu luotettavaksi [Berlin_0_256.map](https://www.movingai.com/benchmarks/street/index.html) -kartan skenaarioissa (Dijkstra sai mallivastauksen jokaisessa skenaarion testissä). Ohjelmani ei kuitenkaan enää noudata samanlaisia liikkumissääntöjä, joten en ole käyttänyt itse skenaarioita varsinaisessa optimaalisen reitin testauksessa enää muiden algoritmien kohdalla.

## Suorituskykytestit
Algoritmien tehokkuutta voi testata jo ohjelman tavallisen käytön aikana. Kun käyttäjä esimerkiksi piirtää kartan ruudukkoon tai valitsee jonkun valmista kartoista ja ajaa halutun algoritmin, ohjelma suorittaa reitinhaun 100 kertaa ja tallentaa kuhunkin ajoon kuluneen ajan System.nanoTime():n avulla seuraavasti:

```
aStar.setMap(mapArray);
long t1 = System.nanoTime();
aStar.runAStar();
long t2 = System.nanoTime();
 ```
Algoritmin muuttujien, taulukoiden yms. alustustoimenpiteet on jätetty ajanoton ulkopuolelle (tässä tapauksessa metodiin `aStar.setMap(mapArray))` ja ainoastaan reitinhakuun kuluva aika mitataan. Mitatut ajat tallennetaan taulukkoon, ensimmäinen aika jätetään huomioimatta ja muista lasketaan keskiarvo. Aika muutetaan millisekuneiksi ja ilmoitetaan käyttöliittymässä käyttäjälle. Tavallinen ajo ilmoittaa myös löydetyn reitin pituuden ja avattujen solmujen määrän. Pyyhkimällä vastauksen _Reset solution_-napilla ja ajamalla reitinhaun jollain toisella algoritmilla, voi verrata saatuja tuloksia. Algoritmien tulokset tulevat sovelluksen oikeaan yläkulmaan näkyviin. Lisäksi ohjelma näyttää lähtö- ja maalisolmujen koordinaatit. Olen testannut algoritmeja erilaisten karttojen, itse piirrettyjen ja valmiiden kanssa, sekä käyttänyt erilaisia sijainteja lähtö- ja maalisolmulle. 

Alla taulukossa kuvatut testit ovat täysin toistettavissa, kun valitaan samat maali -ja lähtöpisteet testeissäni käyttämille kartoille ja ajetaan reitinhaku kullakin algoritmilla. Kuitenkin koska pisteet valitaan kartasta klikkaamalla, on tismalleen samoja pisteitä hankala valita suurissa kartoissa ruutujen pienten koon ja koordinaattien hankaluuden takia. Tästä syystä olen lisännyt käyttämiini testikarttoihin lähtö- ja maalipisteen valmiiksi. Riittää siis, että avaa kunkin testikartan _Select test_-napin kautta avautuvasta kansiosta ja ajaa reitinhaun kaikilla algoritmeilla. Näin voi verrata ohjelman antamia tuloksia taulukkoon (dokumentin alaosassa) kirjattuihin tuloksiin.

Raskaampi suorituskykytesti voidaan tehdä aiemmin mainitulle 256x256 kokoiselle [Berlin_0_256](https://www.movingai.com/benchmarks/street/index.html) -kartalle, sekä samankokoiselle Moscow_1_256-kartalle. Testauksessa käytetään karttoihin liittyviä Berlin_0_256.scen- ja Moscow_1_256.scen-tiedostoja. Berlin sisältää 930 erilaista skenaariota, eli tilannetta, joissa jokaisessa on eri lähtö- ja maalisolmu ja Moscow 1100 tilannetta. Sovellus ajaa kunkin skenaarion jokaisella algoritmilla (Dijkstra, A*, JPS) 5 kertaa ja tallentaa ajoaikojen keskiarvon ylös yllä aiemmin kuvatulla tavalla. Ensimmäistä ajoa 5:stä ei tässäkään huomioida, sillä se on usein muita hitaampi ja vääristää tulosta. Lopulta ohjelma laskee yhteen kullekin algoritmille jokaisen skenaarion keskiarvoaikojen summan, eli kuinka kauan algoritmilla meni keskimäärin yhteensä kaikkien skenaarioiden läpikäymiseen. Suorituskykytesti ajetaan _Benchmark_-napista ja se saattaa viedä joitakin minuutteja käytettävän tietokoneen prosessorin tehosta riippuen. Kullekin skenaariolle ajoja tehdään vain 5, sillä muuten testin suoritusaika kasvaa todella pitkäksi. Testasin muutaman kerran 20 suorituskertaa per skenaario, mutta tällä ei ollut merkittävää vaikutusta saatuihin keskiarvoihin, joten 5 vaikuttaa riittävältä.

## Testien tuloksia

### Raskaampi testi

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/berlin.png)

_Kaikkien skenaarioiden läpikäymiseen kulunut kokonaisaika millisekunteina Berlin_0_256 -kartassa_

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/moscow.png)

_Kaikkien skenaarioiden läpikäymiseen kulunut kokonaisaika millisekunteina Moscow_1_256 -kartassa_

Kuten kuvaajista nähdään JPS suoriutuu reitinhakutehtävistä selkeästi nopeiten ja A*:kin selkeästi Dijkstraa nopeammin. Skenaariot sisältävät monia erilaisia tilanteita; pisteiden välillä on esteitä vaihtelevia määriä ja pisteiden etäisyys toisiinsa vaihtelee merkittävästi. Tietynlaisissa spesifeissä tilanteissa erot algoritmien välillä voivatkin olla huomattavasti suuremmat tai vastaavasti kaventua hyvin pieniksi. Yllä kuvatut skenaariot voidaan ajaa ohjelmalla valitsemalla _Berlin_ tai _Moscow_ ja painamalla _Benchmark_ -nappia. Tulokset ilmaantuvat sovelluksen oikeaan yläreunaan, kun ajo on valmis.

### Tavallisia reitinhakuja

Huom! Koordinaatit muodossa (y, x)

| Algoritmi | Aika | Avatut solmut | Matka | Aloituspiste | Maalipiste | Kartta |
| --------- | ---- | ------------- | ----- | ------------ | ---------- | ------ |
| Dijkstra  | 11.008 ms | 27498 | 156.06601 | 64, 87 | 164, 169 | NewYork_1_256.map.txt |
| A* | 1.074 ms | 1807 | 156.06601 | 64, 87 | 164, 169 | NewYork_1_256.map.txt |
| JPS | 0.3848 | 25 | 156.06601 | 64, 87 | 164, 169 | NewYork_1_256.map.txt |
| Dijkstra | 9.977 ms | 28915 | 173.97056 | 141, 231 | 141, 64 | Milan_1_256.map.txt |
| A* | 0.176 ms | 635 | 173.97056 | 141, 231 | 141, 64 | Milan_1_256.map.txt |
| JPS | 0.056 ms | 6 | 173.97056 | 141, 231 | 141, 64 | Milan_1_256.map.txt |
| Dijkstra | 7.787 ms | 24614 | 193.30865 | 38, 66 | 108, 223 | Lodon_0_256.xt |
| A* | 3.178 ms | 3691 | 193.30865 | 38, 66 | 108, 223 | Lodon_0_256.xt |
| JPS | 0.954 ms | 39 | 193.30865 | 38, 66 | 108, 223 | Lodon_0_256.xt |
| Dijkstra | 10.990 ms | 33329 | 186.49747 | 62, 70 | 103, 204 | Berlin_1_256.txt |
| A* | 5.071 ms | 8505 | 186.49747 | 62, 70 | 103, 204 | Berlin_1_256.txt |
| JPS | 1.177 ms | 174 | 186.49747 | 62, 70 | 103, 204 | Berlin_1_256.txt |
| Dijkstra | 10.637 ms | 31162 | 202.28427 | 176, 16 | 148, 202| London_0_256.txt |
| A* | 4.301 ms | 5656 | 202.28427 | 176, 16 | 148, 202| London_0_256.txt |
| JPS | 0.310 ms | 26 | 202.28427|  176, 16 | 148, 202| London_0_256.txt |
| Dijkstra | 15.500 ms | 44482 | 352.014285 | 186, 32 | 40, 144 | London_0_256.txt |
| A* | 18.919 ms | 35410 | 352.014285 | 186, 32 | 40, 144 | London_0_256.txt |
| JPS | 3.976 ms | 531 | 352.014285 | 186, 32 | 40, 144 | London_0_256.txt |


### Kuvia reitinhauista erilaisissa tilanteissa

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/dijkstra_ei_esteita.png)
_Kuva 1: Dijkstra ilman suurta määrää esteitä lähtö- ja maalisolmun välillä_

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/aStar_ei_esteita.png)
_Kuva 2: A* ilman suurta määrää esteitä_

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/dijkstra_este.png)
_Kuva 3: Dijkstra esteiden kanssa_

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/aStar_este.png)
_Kuva 4: A* esteiden kanssa_

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/jps_ei_esteita.png)
_Kuva 5: JPS ilman esteitä_
