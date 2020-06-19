# Viikkoraportti 6

Tällä viikolla lisäsin vinottaisen liikkumiseen karttaan ja vaihdoin sitä myötä myös Manhattan-heuristiikan diagonaaliseen.
Loin dynaamisen taulukkoluokan (koko tuplautuu aina tilan loppuessa) korvaamaan ArrayListin käytön algoritmeissa ja korvasin myös
muutaman Javan Math-luokan metodin omilla toteutuksilla. Lisäsin sovellukseen myös mahdollisuuden ajaa valmiiden karttojen 
erilaisia [reitinhakuskenaarioita](https://www.movingai.com/benchmarks/street/index.html). Testiskenaarioissa ilmeni edelleen, että A*
löytää vieläkin muutamassa tilanteessa aavistuksen pidemmän reitin (ero optimaaliseen tulokseen < 1). Eron koko ei näyttänyt kasvavan vaikka testattavien reittien koko
testeissä kasvoikin. En tällä viikolla kuitenkaan ehtinyt A* juuri optimoimaan. Aloitin myös kokeilemaan JPS:n toteutusta. Aikaa kului pitkälti aiheeseen 
perehtymiseen. Toisin kuin Dijkstran ja A*:n kanssa, JPS:stä ei alkuperäisen [julkaisun](http://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf)
ja muutaman simppelin blogitekstin lisäksi löytynyt juuri materiaalia, joten alkuun pääseminen on ollut huomattavasti hankalampaa. Tällä hetkellä lyhin reitti löytyy vain
jos kartassa ei ole esteitä ja toteutus tuskin vastaa vielä muiltakaan osin JPS:ää. 

Ennen lopullista palautusta yritän saada JPS:n toimimaan ja korjata bugin A*:ssä. Tavallisia ja tehokkuustestejä täytyy myös kirjoitella
melko paljon ja korjata ohjelmaan kertyneitä pikkubugeja. Monia asioita voisi koodissa myös varmasti siistiä ja toteuttaa fiksummin, 
mutta aika ei tässä kohtaa taida enää riittää. Kiire tulee.

Käytetty aika n. 10 h
