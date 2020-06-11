# Viikkoraportti 5

Tällä viikolla parantelin ohjelman toiminnallisuutta ja poistin joitakin bugeja. Siistin käyttöliittyymää ja lisäsin mahdollisuuden
tallentaa piirrettyjä karttoja ja säätää animaation nopeutta. Eriytin naapurisolmujen hakemisen omaksi luokakseen, koska toiminto oli
Dijkstran algoritmissa ja A*:ssä identtinen. Aloin myös testaamaan algoritmien tehokkuutta. Alunperin sovellus haki ennakkoon jokaisen solmun
naapurisolmut talteen, mutta muutin sovellusta siten, että naapurisolmut haetaan vain tarvittaessa. Tämä nopeutti suoritusaikaa huomattavasti.
Luulin saaneeni A*:n toimimaan oikein, mutta edelleen joissakin satunnaisissa tilanteissa löydetty reitti ei ole lyhin. Myöskään suoritusaika ei aina
merkittävästi eroa Dijkstrasta, joten parannettavaa selkeästi on edelleen. Käytin suorituskyvyn testaukseen muutamia [Moving AI Lab -karttoja](https://www.movingai.com/benchmarks/grids.html),
sillä sovelluksessa piirretyt kartat ovat niin pieniä, että tehokkuuserojen hakeminen ei tunnu yhtä luotettavalta.


