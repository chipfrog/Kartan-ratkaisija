# Käyttöohjeet
Sovellus vaatii toimiakseen Java 11. Suoritettavan jar-tiedoston luominen ei onnistunut JavaFX kanssa tulleiden teknisten ongelmien vuoksi, joten repositorio on ladattava koneelle ja seurattava alla olevia ohjeita ohjelman suorittamiseksi.

## Sovelluksen käynnistäminen
Lataa zip-tiedosto GitHubista ja pura se haluamaasi sijaintiin. Siirry konsolin kautta tähän sijaintiin ja siellä kansioon _Shortest-path-visualizer-master_.
Suorita nyt konsolissa komennot:

`./gradlew build`

`./gradlew run`

## Testien ajaminen

Testien suorittaminen konsolissa: `./gradlew test`

Jacoco-raportin generointi: `./gradlew test jacocoTestReport`

Jacocon generoiman testikattavuusraportin sijainti: _/build/reports/jacoco/test/html/index.html_

## Checkstyle
Konsolissa:
`./gradlew checkstyleMain`

Raportti löytyy sijainnista: _/build/reports/checkstyle_

## Sovelluksen käyttäminen
Sovellusta käytetään visuaalisella käyttöliittymällä: 

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/final_ui.png)

## Huomio!
Kun ohjelma suorittaa animaatiota kaikki napit pitää jättää rauhaan, eikä ruudukkoon saa piirtää. Jos animaation ei anna mennä loppuun, vaan painaa esimerkiksi _Clear_-nappia, animaatio vain alkaa alusta. Animaation pitää siis antaa mennä loppuun tai jos on tullut valinneeksi liian hitaan animaationopeuden, eikä jaksa odottaa on parempi vain käynnistää sovellus uudestaan. Pahoittelut tästä!

### Piirtäminen
Vasemmasta yläkulmasta kohdan _Draw_ alta valitaan piirrettävän ruudun tyyppi. Valitsemalla _Start point_ ja klikkaamalla haluttua ruutua ruudukkoon lisätään lähtösolmu (vihreä) ja vastaavasti valitsemalla _Goal_ ruudukkoon lisätään maalisolmu (punainen). Valitsemalla _Obstacle_ karttaan voidaan piirtää esteitä. Esteitä voi yksittäisten ruutujen klikkailun lisäksi piirtää pitämällä hiiren vasenta nappia pohjassa. Jos tulee tarve kumittaa mitä tahansa ruudukosta, se onnistuu hiiren oikealla napilla.

### Reitinhaun suorittaminen
Jotta reitinhaku onnistuu, tulee ruudukkoon olla lisättynä vähintään lähtösolmu ja maalisolmu, ja suoritettavan algoritmin tulee olla valittuna. Vasempaan alareunaan ilmestyy virheilmoitus, jos jotain puuttuu. Käytettävä algoritmi valitaan _Algorithm_-valikosta. Valittavia algoritmeja ovat Dijkstra, A* ja Jump Point Search. Jos reitinhaun tulos halutaan välittömästi näkyviin, klikataan _No animation_-ruutua. Muussa tapauksessa animaation nopeutta voidaan säätää _Animation delay_-sliderin kautta. Sliderilla valitaan kuinka monta millisekuntia kunkin väritettävän ruudun välillä on, eli animaatio on nopein, kun slider on kohdassa 1. Kun asetukset ovat halutunlaiset, painetaan _Run_-painiketta ja ruudukkoon ilmestyy reitinhaun tulos. Vieraillut ruudut väritetään vaalean sinisellä ja reitti solmujen välillä keltaisella. 

Jos algoritmien toimintaa halutaan vertailla, vanha tulos (avatut solmut ja löydetty reitti) pyyhitään kartasta _Reset solution_-napilla ja reitinhaku ajetaan uudestaan valitsemalla jokin toinen algoritmi ja painamalla _Run_-nappia. Jos taas halutaan aloittaa kokonaan puhtaalta pöydältä, koko kartan ja asetukset saa nollattua painamalla _Clear_-painiketta. _Clear_ luo uuden tyhjän karttapohjan ja nollaa aikaisemmat tulokset. 

### Tulosten tarkastelu
Reitinhaun ajamisen jälkeen kohtiin _Distance_, _Nodes_ ja _Time_ ilmestyy tietoja reitinhaun tuloksista. _Nodes_ kertoo algoritmin läpikäymien solmujen määrän, _Distance_ löydetyn lyhimmän reitin pituuden lähtösolmun ja maalisolmun välillä ja _Time_ hakuun kuluneen ajan millisekunteina. Kunkin algoritmin reitinhaun tiedot ilmoitetaan sovelluksen oikeassa yläreunassa käytetyn algoritmin nimen alla. Reitinhaun tulosten alapuolelle ilmestyy myös aloitus- ja maalisolmun koordinaatit, kun ne on valittu.

### Omien karttojen tallentaminen
Jos piirrettyn kartat haluaa säilyttää uudelleenkäyttöä varten, sen voi tallentaa kirjoittamalla kartalle nimi _Save map_-kenttään ja klikkaamalla _Save Map_-nappia. Karttaa ei voi tallentaa ilman nimeä.

### Omien tallennettujen karttojen käyttäminen
Aiemmin tallennettu kartta voidaan avata klikkaamalla _Select saved map_-nappia. Ikkuna oikeaan kansioon aukeaa ja halutun kartan voi valita. Kartta aukeaa ohjelmaan ja ohjelmaa voi käyttää normaalisti.

### Valmiiden karttojen käyttäminen
_Select map_ -nappi avaa kansion, josta voi valita valmiita karttoja. Karttoihin lisätään klikkaamalla lähtö- ja maaliruudut haluttuihin paikkoihin ja vastaavasti ne voi poistaa klikkaamalla hiiren oikealla painikkeella. Karttoihin ei voi piirtää esteitä, kuten ruudukkokarttaan, sillä isommissa kartoissa piirtämisestä tulee liian hidasta ollaakseen käyttökelpoista. Kartat ovat melko isoja, joten on suositeltavaa valita _No animation_, jos haluaa reitinhaun tuloksen nopeasti. Olen lisännyt muutaman kartan valmiiksi. Kartasta pääsee takaisin normaaliin piirtotilaan _Clear_-napilla ja _Reset solution_ pyyhkii valmiistakin kartoista algoritmien hakemat reitit. Lisää karttoja voi ladata [täältä](https://www.movingai.com/benchmarks/street/index.html). Anoastaan "City/Street maps" alla olevat kartat soveltuvat sovelluksen käyttöön, koska ne muodostuvat vain '@' ja '.' merkeistä, joita sovellus ymmärtää. Kannattaa valita kooltaan vain 256x256 karttoja, sillä isommat kartat eivät mahdu ohjelman kuvaan. Karttatiedostoista tulee poistaa tekstieditorilla yläreunassa oleva olevat rivit, joissa on kartan tietoja (kuten leveys, korkeus yms.)  Tiedosto tallennettaan tämän jälkeen.txt-muodossa ja lisätään kansioon _Shortest-path-visualizer-master/src/main/resources/maps_. Nyt siihen päästään käsiksi _Select map_-napin kautta.

### Suorituskykytestin ajaminen
_Benchmark_-nappia klikkaamalla ohjelma ajaa suorituskykytestin. Koneen tehosta riippuen testin ajamiseen voi kulua jonkin aikaa (minuutteja). Kun suoritustesti on valmis, oikeaan yläkulmaan ilmestyy kultakin algoritmilta kulunut kokonaisaika (eli kunkin skenaarion suoritusajan keskiarvojen summa). Tarkemmat tiedot suorityskykytestistä [täällä](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/Testausdokumentti.md). Suorituskykytesti on suoritettavissa kahdelle eri kartalle; _Berlin_0_256_ ja _Moscow_1_256_. Kartan voi valita Benchmark-napin alla klikkaamalla joko Berlin ja Moscow. Oletuksena valintana on Berlin. Suorituskykytestin aikana konsoliin tulostuu kunkin suoritetun skenaarion numero.

### Valmiiden karttaongelmien testaaminen
Vasemman alareunan _Select test map_ -napista avautuu kansio, josta pääsee avaamaan samoja valmiita karttoja, mutta näihin on jo lisätty valmiiksi lähtö- ja maalisolmu. Tämä siksi, että samat reitinhaun testit, joiden tulokset olen laittanut [testausdokumentin](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/Testausdokumentti.md) taulukkoon, on mahdollista toistaa uudestaan sovelluksessa. Samojen aloitus- ja maalisolmun asettaminen klikkaamalla karttaa olisi hankalaa pienten ruutujen ja pelkkien koordinaattitietojen takia. Testit ajetaan, kuten muutkin, mutta maali- tai lähtösolmua ei nyt lisätä. Kartta avataan ja reitinhaku suoritetaan kullakin algoritmilla. Jokaisen algoritmin välissä kartta "puhdistetaan" _Reset_solution_-napilla. Tulokset ilmestyvät sovelluksen oikeaan reunaan ja niitä voi sitten verrata testausdokumentin taulukkoon.
