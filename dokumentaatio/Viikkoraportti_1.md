# Viikkoraportti 1

Ensimmäisellä viikolla valitsin harjoitustyölle aiheen ja aikaa kuluikin eniten eri vaihtoehtojen punnitsemiseen.
Toisaalta halusin itseäni kiinnostavan aiheen, mutta kuitenkin sellaisen, josta uskon selviytyväni ja jota pystyisi tarvittaessa kehittämään
riittävän laajaksi hyvään arvosanaan, jos osaaminen vain riittää.
Kirjoitin myös määrittelydokumentin ja loin repositorion, sekä Gradle-projektin sovellukselle. Varsinaista
ohjelmakoodia en vielä kirjoittanut. Mieleen täytyi palauttaa joitakin tiran teemoja, kuten aikavaativuuteen liittyviä asioita.
Hain myös netistä hieman tietoa sovelluksessa tarvitsemistani algoritmeista (Dijkstra, A*, JPS) ja tutustuin reitinhakuongelmiin yleisesti.

Seuraavaksi lähden koodaamaan itse sovellusta. Yritän aluksi varmaan toteuttaa javan valmiilla tietorakenteilla Dijkstran algoritmin, joka
osaisi hakea lyhimmän reitin kahden solmun välillä sille syötetystä ascii-merkkisestä kartasta/labyrintista. Ruudukosta koostuvan visuaalisen kartan luominen
ja siihen esteiden piirtäminen lienevät ainakin aluksi toissijaisia ominaisuuksia, joten palaan niiden toteutukseen vasta myöhemmässä vaiheessa.

## Kysymyksiä

* Ovatko ruudukosta koostuvat käyttäjän sovelluksessa piirtämät kartat (esim. 50x50 ruutua) liian pieniä/yksinkertaisia ongelmia ratkaistaviksi vai onko parempi käyttää esim. valmiita [Moving Ai Lab -pelikarttoja](https://www.movingai.com/benchmarks/grids.html)? Muuttuukko tilanne jos melko suurten ruutujen sijaan kartat piirretäisiinkin pikselin tarkkuudella?

* Jos syötteet sovellukselle olisivat käyttäjän piirtämiä karttoja tai valmiita kuvia kartoista, kannattaako ne muuntaa ascii-merkeiksi ennen reitinhakua vai luetaanko niitä suoraan kuvadatasta?
