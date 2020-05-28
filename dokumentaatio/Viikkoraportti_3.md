# Viikkoraportti 3
Viikko sujui melko hyvin ja sain toteutettua toimivan Dijkstran algoritmin. Toteutuksessa saattaa vielä olla turhia päällekkäisyyksiä taulukoiden ym. välillä, mutta karsin ne jatkossa pois. Tein myös sovellukselle visuaalisen 
käyttöliittymän, jossa ratkaistavan kartan voi piirtää ruudukkoon. Karttaan voi nyt siis piirtää esteitä, sekä aloitus- ja lähtösolmun, joiden
välille lyhin reitti etsitään. Kartan piirtämisen jälkeen sovellus osaa näyttää animaation Dijkstran algoritmin etenemisestä ruudukossa
solmusta solmuun
ja piirtää lopulta reitin aloitussolmusta maalisolmuun. Haastavinta tällä viikolla oli käyttöliittymän ja algoritmin yhteensovittaminen.
Paikoin oli hankala selvittää kummassa vika oli, kun ohjelma ei toiminut odotetulla tavalla. Välitulosteiden lisääminen useaan
kohtaan auttoi ongelmien ratkaisemisessa. Piirrettävän kartan toteuttaminen ruudukkoon vaati myös muutaman erilaisen toteutustavan
kokeilua ennen kuin sen sai toimimaan halutulla tavalla ja käytännöllisesti. Sain lopulta myös määriteltyä projektille CircleCI-ympäristön
laittamalla CircleCI:n käyttämään Java 8:n vanhempaa versiota. Uudempi versio ei näytä enää löytävän JavaFX:ää, enkä minä osaa sitä sille hakea.
Voiko tästä Java 8 + JavaFX -yhdistelmästä tulla ongelmia arvioinnin kanssa? Omalla koneella sovellus luonnollisesti toimii, mutta muiden koneista on hankala sanoa.

Ensi viikolla korvaan Dijkstran algoritmista javan valmiit tietorakenteet omillani ja alan toteuttamaan A*. 

Kolmannella viikolla käytetty aika: n. 15h
