# Viikkoraportti 5

Tällä viikolla parantelin ohjelman toiminnallisuutta ja poistin joitakin bugeja. Siistin käyttöliittyymää ja lisäsin mahdollisuuden
tallentaa piirrettyjä karttoja ja säätää animaation nopeutta. Eriytin naapurisolmujen hakemisen omaksi luokakseen, koska toiminto oli
Dijkstran algoritmissa ja A*:ssä identtinen. Aloin myös testaamaan algoritmien tehokkuutta. Alunperin sovellus haki ennakkoon jokaisen solmun
naapurisolmut talteen, mutta muutin sovellusta siten, että naapurisolmut haetaan vain tarvittaessa. Tämä nopeutti suoritusaikaa huomattavasti.
Luulin saaneeni A*:n toimimaan oikein, mutta edelleen joissakin satunnaisissa tilanteissa löydetty reitti ei ole lyhin. Myöskään suoritusaika ei
merkittävästi eroa Dijkstrasta, joten parannettavaa selkeästi on edelleen. Käytin suorituskyvyn testaukseen muutamia [Moving AI Lab -karttoja](https://www.movingai.com/benchmarks/grids.html),
sillä sovelluksessa piirretyt kartat ovat niin pieniä, että tehokkuuserojen hakeminen ei tunnu yhtä luotettavalta.

## Kysymyksiä

* Vertaisarvioija ei saanut sovellusta suoritettua JavaFX:stä johtuen. Omassa fuksiläppärissä käytössä Java 8, jossa javafx vielä ilmeisesti tuli javan mukana. Yritin testata sovellusta yliopiston Virtual Desktopin kautta, mutta näköjään sielläkin nykyään käytössä Java 11, eikä ollut oikeuksia kokeilla version vaihtamista VD:stä valmiiksi löytyvään Java 8:aan. En siis tiedä toimiiko sovellus muulla kuin omalla koneella. Onnistuuko projektin loppuarviointi ilman, että saan vaihdettua projektin käyttämään uudempaa javaa?

## Ensi viikolla
Koitan edelleen korjata A* ja alan tehdä kattavampia tehokkuustestejä. Jos ehdin, yritän aloittaa JPS:n toteuttamisen.

Käytetty aika n. 10h

