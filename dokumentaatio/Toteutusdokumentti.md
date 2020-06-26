# Toteutus

Ohjelmaa käytetään visuaalisella käyttöliittymällä, jonka toiminta on erotettu sovelluksen varinaisesta toimintalogiikasta.
Algoritmeille ja karttojen käsittelylle on omat luokkansa, ja joitakin algoritmien yhteisiä toimintoja, kuten naapurisolmujen
hakeminen on eriytetty omaksi luokakseen luokakseen. Konsoliin tulostamiselle ja tiedostojen lukemiselle
on oma rajapintansa IO, jota sitä tarvitsevat luokat (kuten MapReaderIO) toteuttavat. Vain MapReaderIO:lla on pääsy javan Scanner-olioon ja MapReaderIO injektoidaan kaikille sitä tarvitseville luokille. Lopullisessa sovelluksessa konsoliin tulostamista ei kuitenkaan enää käytetä ja sen funktioksi jää lähinnä sovelluksen testaaminen kehityksen aikana. Scanneria kuitenkin tarvitaan esimerkiksi .txt-muotoisten karttatiedostojen lukemiseen.

## Pseudokoodit

### Dijkstra
```
for each node in Graph
  set distance to infinity

set distance of start to 0
add start to minHeap
  
  while minHeap is not empty
    current = node with smallest distance in minHeap
    remove current from minHeap
    
    if current has been visited
      continue
    
    if current is goal
      end
    
    mark current as visited
    
    for each neighbour of current
      oldDistance = distance[neighbour]
      newDistance = distance[current] + distance to neighbour
      
      if newDistance < oldDistance
        distance[neighbour] = newDistance
        add neighbour to minHeap
    
```
