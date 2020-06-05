# Toteutus

Ohjelmaa käytetään visuaalisella käyttöliittymällä, jonka toiminta on erotettu sovelluksen varinaisesta toimintalogiikasta.
Algoritmeille ja karttojen käsittelylle on omat luokkansa, ja joitakin algoritmien yhteisiä toimintoja, kuten naapurisolmujen
hakeminen, tullaan todennäköisesti yhdistämään omaksi perittäväksi luokakseen. Konsoliin tulostamiselle ja tiedostojen lukemiselle
on oma rajapintansa IO, jota sitä tarvitsevat luokat (kuten MapReaderIO) toteuttavat. Tällä hetkellä solmuja käsitellään omana Node-luokkanaan, joka sisältää mm. tiedot
solmun sijainnista ja etäisyyksistä, mutta jatkossa se saatetaan korvata taulukoilla.
