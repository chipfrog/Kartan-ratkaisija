# Toteutus

Ohjelmaa käytetään visuaalisella käyttöliittymällä, jonka toiminta on erotettu sovelluksen varinaisesta toimintalogiikasta.
Kullekin algoritmille ja karttojen käsittelylle on omat luokkansa, ja joitakin algoritmien yhteisiä toimintoja, kuten naapurisolmujen
hakeminen (NeighbourFinder) ja jotkin matemaattiset funktiot (MathFunctions) on eriytetty omiksi luokikseen. Konsoliin tulostamiselle ja tiedostojen lukemiselle on oma rajapintansa IO, jota kuten MapReaderIO toteuttaaa. Vain MapReaderIO:lla on pääsy javan Scanner-olioon ja MapReaderIO injektoidaan kaikille sitä tarvitseville luokille, kuten BenchmarkFileReaderille. Lopullisessa sovelluksessa konsoliin tulostamista ei enää juuri käytetä ja sen funktioksi jää lähinnä sovelluksen testaaminen kehityksen aikana. MapReaderIO:n mukana injektoituvaa Scanneria kuitenkin tarvitaan esimerkiksi .txt-muotoisten karttatiedostojen ja karttaskenaariotiedostojen (scen) lukemiseen. Omina tietorakenteina on toteutettu dynaamisesti kokoaan kasvattava taulukko (DynamicArray) ja minimikeko (Keko). 

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

### A*
```
for each node in Graph
  set distance to infinity

set distance of start to 0
add start to minHeap

  while minHeap is not empty
    current = node with smallest f_distance in minHeap
    remove current from minHeap
  
  if current is goal
    end
  
  for each neighbour of current
    newGDistance = current.g_distance
    
    if neighbour not in closedList or newGDistance < neighbour.g_distance
      add neighbour to closedList
      set neighbour.f_distance to newGDistance + heuristic_distance
      add neighbour to minHeap

```

### Jump point search
```
for each node in Graph
  set distance to infinity
  
func JPS()
  create 8 start_nodes with different scan directions
  add start_nodes to minHeap
  
  while minHeap is not empty
    current = node with smallest f_distance
    remove current from minHeap
    
    if goal is found
      end
    
    if current.direction_x == 0 and current.direction_y == 0
      diagonalScan(current)
    else if current.direction_x is not 0 and current.direction_y is 0
      horizontalScan(current)
    else if current.direction_y is not 0 and current.direction_x is 0
      verticalScan(current)
      
  
  func horizontalScan(parent)
  while true
    x_step += parent.diretion_x
    
    if map[parent.y][x_step] = OBSTACLE
      end
    
    distance_travelled = parent.distance + 1
    x_next_step = x_step + parent.direction_x
    
    if map[parent.y][x_step] = goal
      end
    
    node = map[parent.y][x_step]
    if node has not been visited or node.g_distance > distance_travelled
      if node is JumpPoint relative to direction -y
        node.parent = parent
        node.direction_y = -1
        node.distance = distance_travelled
        add node to minHeap
    
    if node has not been visited or node.g_distance > distance_travelled
      if node is JumpPoint relative to direction +y
        node.parent = parent
        node.direction_y = 1
        node.distance = distance_travelled
        add node to minHeap
        
func verticalScan(parent)
  while true
  
    y_step += parent.diretion_y
    
    if map[y_step][parent.x] = OBSTACLE
      end
    
    distance_travelled = parent.distance + 1
    y_next_step = y_step + parent.direction_y
    
    if map[y_next_step][parent.x] = goal
      end
    
    node = map[y_next_step][parent.x]
    if node has not been visited or node.g_distance > distance_travelled
      if node is JumpPoint relative to direction -x
        node.parent = parent
        node.direction_x = -1
        node.distance = distance_travelled
        add node to minHeap
    
    if node has not been visited or node.g_distance > distance_travelled
      if node is JumpPoint relative to direction +x
        node.parent = parent
        node.direction_x = 1
        node.distance = distance_travelled
        add node to minHeap
        
func diagonalScan(paren)
  while true
    similar checks like in horizontalScan and verticlScan
    distance_travelled += parent.distance + sqrt(2)
    
    do horizontalScan(parent, direction_x = 0)
    do verticalScan(parent, direction_y = 0)
    
    diagonal_step += 1
    
    if node has not been visited or node.g_distance > distance_travelled
      if node is JumpPoint relative to direction (-1) * x
        node.parent = parent
        node.direction_x = (-1) * x
        node.distance = distance_travelled
        add node to minHeap
        
    if node has not been visited or node.g_distance > distance_travelled
      if node is JumpPoint relative to direction (-1) * y
        node.parent = parent
        node.direction_y = (-1) * x
        node.distance = distance_travelled
        add node to minHeap       
```

## Puutteet
Sovellus ei ole toiminnaltaan aivan niin viimeistelty kuin voisi toivoa. Reitinhaun animaatiota ei voi esimerkiksi keskeytää, vaan sen on pakko antaa mennä loppuun tai käynnistää sovellus uudestaan, ennen kuin muita toimintoja voi käyttää. Algoritmit eivät myöskään aina anna täysin samoja tuloksia reitinhaussa. Erityisesti A* antaa joissakin spesifeissä tilanteissa hieman Dijkstraa ja JPS:ää suurempia tuloksia. Erot ovat kuitenkin olleet minimaalisia, pahimmillaan muutaman desimaalin luokkaa. Koodi sisältää myös jonkin verran toisteisuutta algoritmien osalta. Näille voisi olla jokin yhteinen yläluokka, joka sisältäisi yhteisiä metodeja, kuten esimerkiksi heuristisen etäisyyden laskeminen, maalisolmun hakeminen ja kartan alustaminen.
