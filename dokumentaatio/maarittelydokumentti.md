# Määrittelydokumentti

## Aihe

Tarkoituksena on toteuttaa ohjelma, joka etsii lyhimmän reitin verkon kahden solmun välillä erilaisia reitinhakualgoritmeja käyttäen. 
Ohjelman avulla voidaan vertailla algoritmien nopeuseroja reitinhaussa, sekä visualisoida niiden reitinhakuprosesseja.
Verkkona toimii ruudukko, jossa yksittäinen ruutu kuvaa yhtä solmua ja jokaisesta solmusta on linkki kaikkiin ympäröiviin solmuihin (yhdestä ruudusta pääsee siis kahdeksaan ympäröivään ruutuun).
Käyttäjä voi vapaasti piirtää ruudukkoon esteitä, jotka algoritmin tulee kiertää, ja valita halutun lähtöpisteen, sekä maalin. 
Tämän jälkeen valitaan käytettävä reitinhakualgoritmi ja ohjelma piirtää lyhimmän reitin maaliin, sekä värittää läpikäydyt ruudut ja ilmoittaa reitin hakemiseen kuluneen ajan. 

## Algoritmit

Ohjelmaan toteutetaan ainakin _A*_, sekä _Dijkstran algoritmi_, sillä ne ovat tyypillisiä reitinhakualgoritmeja. _A*_ mahdollisesti myös laajennetaan
_jump point search_:llä, jolloin päästään myös vertailemaan tavallista ja optimoitua _A*_:ä.
