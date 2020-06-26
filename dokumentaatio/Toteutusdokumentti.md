# Toteutus

Ohjelmaa käytetään visuaalisella käyttöliittymällä, jonka toiminta on erotettu sovelluksen varinaisesta toimintalogiikasta.
Algoritmeille ja karttojen käsittelylle on omat luokkansa, ja joitakin algoritmien yhteisiä toimintoja, kuten naapurisolmujen
hakeminen on eriytetty omaksi luokakseen luokakseen. Konsoliin tulostamiselle ja tiedostojen lukemiselle
on oma rajapintansa IO, jota sitä tarvitsevat luokat (kuten MapReaderIO) toteuttavat. Vain MapReaderIO:lla on pääsy javan Scanner-olioon ja MapReaderIO injektoidaan kaikille sitä tarvitseville luokille. Lopullisessa sovelluksessa konsoliin tulostamista ei kuitenkaan enää käytetä ja sen funktioksi jää lähinnä sovelluksen testaaminen kehityksen aikana. Scanneria kuitenkin tarvitaan esimerkiksi .txt-muotoisten karttatiedostojen lukemiseen.
