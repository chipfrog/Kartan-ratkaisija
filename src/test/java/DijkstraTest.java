import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Node;

public class DijkstraTest {
  File testMap = new File("src/test/resources/kartat/testikartta.txt");
  File specialCaseMap = new File("src/test/resources/kartat/erikoistapaus1.txt");
  File unreachableGoalMap = new File ("src/test/resources/kartat/saavuttamatonmaali.txt");
  File easyMap = new File("src/test/resources/kartat/helppokartta.txt");

  IOStub ioStub = new IOStub();
  MapReader mapReader = new MapReader(ioStub);
  char[][] mapMatrix;
  Dijkstra d;

  @Before
  public void initMatrix() throws FileNotFoundException {
    mapReader.createMatrix(testMap);
    mapMatrix = mapReader.getMapArray();
    d = new Dijkstra(ioStub);
    d.setMap(mapMatrix);

  }
  private char[][] initDijkstraWithNewMap(File file) throws FileNotFoundException{
    mapReader.createMatrix(file);
    return mapReader.getMapArray();
  }

  @Test
  public void cellHasEightNeighboursIfNoneOfThemIsObstacle() {
    Node[] neighbours = d.haeNaapurisolmut(30,4);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 8);
  }

  @Test
  public void returnsShortestDistanceInBasicSituation() {
    d.runDijkstra();
    assertTrue(d.getEtaisyysMaaliin() == 5);
  }

  @Test
  public void tracesShortestPathBackToNodeClosestToStartingNode() {
    d.runDijkstra();
    assertTrue(d.haeReitti().getEtaisyys() == 1);
  }

  @Test
  public void programDoesNotCrashWhenGoalNodeIsUnreachable() throws FileNotFoundException{
    d.setMap(initDijkstraWithNewMap(unreachableGoalMap));
    d.runDijkstra();
    assertTrue(d.getGoalNode() == null);
  }

  @Test
  public void tracesShortestPathBackToNodeClosestToStartingNodeInSpecialMap() throws FileNotFoundException {
    d.setMap(initDijkstraWithNewMap(specialCaseMap));
    d.runDijkstra();
    assertTrue(d.getEtaisyysMaaliin() == 10);
    assertTrue(d.haeReitti().getEtaisyys() == 1);
  }

  @Test
  public void visitsRightNumberOfNodes() throws FileNotFoundException {
    d.setMap(initDijkstraWithNewMap(easyMap));
    d.runDijkstra();
    DynamicArray list = d.getVisitedOrder();
    int nodes = 0;
    for (int i = 0; i < list.size(); i ++) {
      if (list.get(i) != null) {
        nodes ++;
      }
    }
    assertTrue(d.getVisitedOrder().size() == nodes);
  }


}
