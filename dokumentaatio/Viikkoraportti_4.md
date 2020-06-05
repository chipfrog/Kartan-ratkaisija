# Viikkoraportti 4

Kuluneella viikolla korvasin valtaosan Javan valmiista tietorakenteista Dijkstran algoritmissa. Loin mm. oman toteutuksen minimikeosta
korvaamaan PriorityQueuen. Hain netistä tietoa A*:n toiminnasta ja toteutin myös alustavan A*-algorimin, mutta se ei vielä jostain syystä löydä lyhintä reittiä kaikissa tapauksissa.
Välillä reittiin tulee turhia mutkia, enkä vielä yhtään osaa sanoa missä skenaarioissa ongelma syntyy. Sekin on vielä hämärän peitossa 
onko ongelma itse algoritmissa vai ainoastaan reitin piirtämisessä. Kokeilin muutamaa erilaista toteutustapaa, mutta sain kaikissa
aikaan saman ongelman. Lisäsin käyttöliittymään myös mahdollisuuden ajaa A*. Tällä hetkellä A* sisältää vielä paljon copy pastea Dijkstran algoritmista, enkä ole kirjoittanut sille omia testejä, koska
se on vasta kokeiluasteella ja toteutus elää jatkuvasti. 

Ensi viikolla yritän saada A*:n toimimaan oikein ja lisään sovellukseen mahdollisuuden hakea lyhin reitti samasta kartasta eri algoritmeilla, sekä
tallentaa reitinhakemiseen kuluneet ajat ja avattujen solmujen määrät näkyviin. Siistin myös A*:n toteutusta ja mahdollisesti eriytän joitain
algoritmien yhteisiä toimintoja omaksi luokakseen. 

Neljännellä viikolla käytetty aika: n. 10h
