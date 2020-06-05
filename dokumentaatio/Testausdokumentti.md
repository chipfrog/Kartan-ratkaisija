# Testaus

## Yleisesti
Ohjelmassa käytetään pääasiassa JUnit-yksikkötestejä luokkien ja metodien toiminnan testaukseen. 
Käyttöliittymä testataan vain manuaalisesti, eikä sille tehdä erillisiä automatisoituja testejä.
Algoritmien toimintaa testataan / tullaan testaamaan valmiilla char-muotoisilla kartoilla, jollaisia ohjelma tavallisenkin toiminnan
aikana käsittelee. Valmiiksi tehdyistä kartoista tiedetään ennakkoon lyhimmät reitit kahden pisteen välillä ja voidaan katsoa
löytääkö algoritmi oikean vastauksen. Testikartoista pyritään tekemään monipuolisia ja sellaisia, joissa ilmeiset rajatapaukset
käydään läpi. Algoritmit voidaan testata samoilla kartoilla, jolloin nähdään mahdolliset erot suoritusajassa
ja läpikäytyjen solmujen määrissä. Testit voidaan ajaa komentoriviltä. 

Tällä hetkellä ohjelmassa on vain ohjelman oikeanlaista toimintaa testaavia testejä, eikä vielä suorituskykytestejä.
