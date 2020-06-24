# Käyttöohjeet
Sovellus saattaa vaatia toimiakseen Java 11 mahdollisten JavaFX-ongelmien välttämiseksi.

## Sovelluksen käynnistäminen
Lataa zip-tiedosto GitHubista ja pura se haluaamaasi paikkaan. Siirry konsolin kautta tähän sijaintiin ja siellä kansioon _Shortest-path-visualizer-master_.
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

![](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/Ui.png)

## Huomio!
Kun ohjelma suorittaa animaatiota kaikki napit pitää jättää rauhaan, eikä ruudukkoon saa piirtää. Jos animaation ei anna mennä loppuun, vaan painaa esimerkiksi _Clear_-nappia, animaatio vain alkaa alusta. Animaation pitää siis antaa mennä loppuun tai jos on tullut valinneeksi liian hitaan animaationopeuden, eikä jaksa odottaa on parempi vain käynnistää sovellus uudestaan. Pahoittelut tästä!

### Piirtäminen
Vasemmasta yläkulmasta kohdan _Draw_ alta valitaan piirrettävän ruudun tyyppi. Valitsemalla _Start point_ ja klikkaamalla haluttua ruutua ruudukkoon lisätään lähtösolmu (vihreä) ja vastaavasti valitsemalla _Goal_ ruudukkoon lisätään maalisolmu (punainen). Valitsemalla _Obstacle_ karttaan voidaan piirtää esteitä. Esteitä voi yksittäisten ruutujen klikkailun lisäksi piirtää pitämällä hiiren vasenta nappia pohjassa. Jos tulee tarvi kumittaa mitä tahansa ruudukosta, se onnisuu hiiren oikealla napilla.

### Reitinhaun suorittaminen
Jotta reitinhaku onnistuu, tulee ruudukkoon olla lisättynä vähintään lähtösolmu ja maalisolmu ja suoritettavan algoritmin tulee olla valittuna. Vasempaan alareunaan ilmestyy virheilmoitus, jos jotain puuttuu. Käytettävä algoritmi valitaan _Algorithm_-valikosta. Valittavia algoritmeja ovat Dijkstra, A* ja JPS (Jump Point Search). Jos reitinhaun tulos halutaan välittömästi näkyviin, klikataan _No animation_-ruutua. Muussa tapauksessa animaation nopeutta voidaan säätää _Animation delay_-sliderin kautta. Sliderilla valitaan kuinka monta millisekuntia kunkin väritettävän ruudun välillä on, eli animaatio on nopein, kun slider on kohdassa 1. Kun asetukset ovat halutunlaiset, painetaan _Run_-painiketta ja ruudukkoon ilmestyy reitinhaun tulos. 

Jos sama ongelma halutaan ratkaista käyttäen jotain toista algoritmia, vanha ratkaisu pyyhitään klikkaamalla _Erase solution_-nappia. Tämän jälkeen valitaan toinen algoritmi ja ajetaan reitinhaku uudestaan _Run_-painikkeella. Jos taas halutaan aloittaa kokonaan puhtaalta pöydältä kartan ja asetukset saa nollattua painamalla _Clear_-painiketta.

### Tulosten tarkastelu
Reitinhaun ajamisen jälkeen kohtiin _Nodes_, _Distance_ ja _Time_ ilmestyy tietoja reitinhaun tuloksista. _Nodes_ kertoo algoritmin läpikäymien solmujen määrän, _Distance_ löydetyn lyhimmän reitin pituuden lähtösolmun ja maalisolmun välillä ja _Time_ hakuun kuluneen ajan millisekunteina. 

### Omien karttojen tallentaminen
Jos piirrettyn kartat haluaa säilyttää uudelleenkäyttöä varten, sen voi tallentaa kirjoittamalla kartalle nimi _Save map_-kenttään ja klikkaamalla _Save Map_-nappia. Karttaa ei voi tallentaa ilman nimeä.

### Omien tallennettujen karttojen käyttäminen
Aiemmin tallennettu kartta voidaan avata klikkaamalla _Select saved map_-nappia. Ikkuna oikeaan kansioon aukeaa ja halutun kartan voi valita. Kartta aukeaa ohjelmaan ja ohjelmaa voi käyttää normaalisti.

### Valmiiden karttojen käyttäminen
_Select map_ -nappi avaa kansion, josta voi valita valmiita karttoja. Näissä kartoissa on valmiina lähtö- ja maalisolmu, eikä niihin voi piirtää. Muuten sovelluksen toiminnot ovat samat kuin itse piirretyissä kartoissa. Kartat ovat melko isoja, joten on suositeltavaa valita _No animation_, sillä muuten animaation valmistuminen kestää melko kauan. Olen lisännyt muutaman kartan valmiiksi. Kartasta pääsee takaisin normaaliin piirtotilaan _Clear_-napilla.

Lisää karttoja voi ladata [täältä](https://www.movingai.com/benchmarks/street/index.html). Kannattaa valita kooltaan melko maltillisia karttoja, sillä kovin suuret kartat eivät mahdu ohjelman kuvaan, 256x256 koko on optimaalinen. Karttatiedostoista tulee poistaa tekstieditorilla yläreunassa oleva olevat rivit, joissa on kartan tietoja (kuten leveys, korkeus yms.) ja lisätä lähtösolmu ja maalisolmu. Kartta muodostuu pisteistä ja @-merkeistä. Lähtösolmu lisätään poistamalla jokin piste kartasta ja kirjoittamalla tilalle S. Maalisolmu lisätään vastaavasti kirjoittamalla jonkin pisteen tilalle G. Tiedosto tallennettaan .txt-muodossa ja lisätään kansioon _Shortest-path-visualizer-master/src/main/resources/maps_. Nyt siihen päästään käsiksi _Select map_-napin kautta.

### Suorituskykytestin ajaminen
_Benchmark_-nappia klikkaamalla ohjelma ajaa suorituskykytestin. Koneen tehosta riippuen testin ajamiseen voi kulua jonkin aikaa (minuutteja). Kun suoritustesti on valmis, _Benchmark_-napin alle ilmestyy kultakin algoritmilta kulunut kokonaisaika (eli kunkin skenaarion suoritusajan keskiarvojen summa). Tarkemmat tiedot suorityskykytestistä [täällä](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/Testausdokumentti.md).



