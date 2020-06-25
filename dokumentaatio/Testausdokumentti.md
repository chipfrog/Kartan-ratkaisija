# Testaus

## Yleisesti
Projektin luokkien ja metodien testit toteutettiin JUnit-yksikkötesteillä. Automaattisten testien ulkopuolelle jätettiin käyttöliittymän testaus, eli kansion _ui_ -sisältö. Käyttöliittymän toimintaa on kuitenkin testattu manuaalisesti ja pyritty löytämään mahdolliset virhetilanteita aiheuttavat skenaariot. Testikattavuutta voi tarkastella [codecovissa](https://codecov.io/gh/chipfrog/Shortest-path-visualizer) tai generoimalla jacocon testikattavuusraportin [käyttöohjeissa](https://github.com/chipfrog/Shortest-path-visualizer/blob/master/dokumentaatio/manual.md) kuvatulla tavalla. Algoritmien suorituskykyä mittaava PerformanceTest-luokka ei myöskään kuulu testikattavuuteen.

## Yksikkötestaus
JUnit-yksikkötesteillä pyrittiin varmistamaan yksittäisten luokkien ja metodien oikeanlainen toiminta. Testeissä on pyritty testaamaan, että luokat toimivat oikein ainakin ns. "normaalitilanteissa" ja yleisimmissä rajatapaustilanteissa. 

### Utility-luokat
Esimerkiksi naapurisolmujen hakemisesta vastaavan luokan NeighbourFinder toimintaa on testattu syöttämällä sille char[][]-mutoinen testikartta, jossa sille on annettu erilaisia koordinaatteja, ja luokan on osattava kertoa, kuinka monta naapurisolmua kyseisellä koordinaatin pisteellä on. Eräs annettu piste on kartan rajalla, jolloin luokka ei saa etsiä naapurisolmuja kartan rajojen ulkopuolelta ja toinen annettu piste on este, jolloin sillä ei saa olla yhtään naapuria. Testeissä on myös tilanteita, jossa pisteellä on muutamalla puolella esteitä ja tilanne, jossa esteitä ei ole ympärillä ollenkaan. Oikeat vastaukset on laskettu kartasta manuaalisesti ja katsottu, että testit antavat samat vastaukset. Matemaattisista funktioista vastaava MathFunctions luokkaa on myös testattu laskemalla muutamaan tapaukseen itse oikeat vastaukset ja katsomalla, että luokka vastaa samoin.

### Minimikeko
Minimikeon toteuttavaa Keko-luokkaa on testattu varmistamalla, että keko on aluksi tyhjä ja muuttuu ei-tyhjäksi ensimmäisen solmun lisäyksen myötä. Keolle on syöytetty etäisyydeltään eriarvoisia solmuja ja tarkistettu, että ensimmäisenä ulos otettavalla solmulla todella on pienin arvo. Keon järjestyksen muuttuminen vastavaasti varmistettiin poistamalla toinenkin solmu, jolloin nähtiin, että se oli arvoltaan toisiksi pienin. Solmut annettiin keolle (pseudo)satunnaisessa järjestyksessä, jolloin voitiin varmistua, ettei keko anna solmuja oikeassa järjestyksessä vain tietyn syöttöjärjestyksen takia (esim. solmut valmiiksi pienimmästä suurimpaan). 

