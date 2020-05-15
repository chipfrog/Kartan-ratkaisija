# Määrittelydokumentti

## Aihe

Tarkoituksena on toteuttaa ohjelma, joka etsii lyhimmän reitin verkon kahden solmun välillä erilaisia reitinhakualgoritmeja käyttäen. 
Ohjelman avulla voidaan vertailla algoritmien nopeuseroja ja läpikäytyjä solmuja reitinhaussa, sekä visualisoida niiden reitinhakuprosesseja.
Verkkona toimii ruudukko, jossa yksittäinen ruutu kuvaa yhtä solmua ja jokaisesta solmusta on kaari kaikkiin ympäröiviin solmuihin (yhdestä ruudusta pääsee siis kahdeksaan ympäröivään ruutuun). Kaikki kaaret solmujen välillä ovat kustannukseltaan 1 arvoisia.
Käyttäjä voi vapaasti piirtää ruudukkoon esteitä, jotka algoritmin tulee tarvittaessa kiertää, ja valita halutun lähtöpisteen, sekä maalin. 
Tämän jälkeen valitaan käytettävä reitinhakualgoritmi ja ohjelma piirtää lyhimmän reitin maaliin, sekä värittää ja laskee läpikäydyt ruudut ja ilmoittaa reitin hakemiseen kuluneen ajan. Sitten saman tehtävän voi antaa toiselle algoritmille, jolloin tuloksia pääsee vertailemaan. Ratkaistun tehtävän voi halutessaan myös tallentaa kuvana. 

## Algoritmit ja toteutus

Ohjelmaan toteutetaan ainakin _A*_, sekä _Dijkstran algoritmi_, sillä ne ovat tyypillisiä reitinhakualgoritmeja lyhimmän reitin etsimiseen. _A*_ mahdollisesti myös laajennetaan
_jump point search_:llä, jolloin päästään myös vertailemaan tavallista ja optimoitua _A*_:ä. _JPS_:n tulisi ainakin oikeanlaisissa olosuhteissa tehostaa _A*_:n toimintaa. Aikavaativuuksissa pyritään algoritmien tunnettuihin aikavaativuuksiin, eli _Dijkstran_ osalta O=(n + m log n) ja _A*_ O=(n<sup>k</sup>). Vaikka ohjelmalle piirretään aluksi ruudukosta muodostuva visuaalinen kartta/labyrintti ratkaistavaksi, käännetään ruudukko algoritmille ascii-merkeistä muodostuvaksi taulukoksi. Ascii-merkkien käsittely lienee ohjelmalle (ja toteuttajalle) helpompaa kuin pikselien tarkastelu. Tällöin ohjelmaa voidaan käyttää myös valmiilla ascii-merkeistä tehdyillä kartoilla.
