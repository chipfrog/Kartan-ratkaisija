import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.utils.Node;

public class DijkstraTest {
  File testMap = new File("src/test/resources/kartat/testikartta.txt");
  File specialCaseMap = new File("src/test/resources/kartat/erikoistapaus1.txt");
  File unreachableGoalMap = new File ("src/test/resources/kartat/saavuttamatonmaali.txt");

  IOStub ioStub = new IOStub();
  MapReader mapReader = new MapReader(ioStub);
  char[][] mapMatrix;
  Dijkstra d;

  @Before
  public void initMatrix() throws FileNotFoundException {
    mapReader.createMatrix(testMap);
    mapMatrix = mapReader.getMapArray();
    d = new Dijkstra(ioStub, mapMatrix);
    d.initVerkko();

  }
  private Dijkstra initDijkstraWithNewMap(File file) throws FileNotFoundException{
    mapReader.createMatrix(file);
    char[][] newMapMatrix = mapReader.getMapArray();
    return new Dijkstra(ioStub, newMapMatrix);
  }

  @Test
  public void cellHasFourNeighboursIfNoneOfThemIsObstacle() {
    Node[] neighbours = d.haeNaapurisolmut(30,4);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 4);
  }

  @Test
  public void cellHasNoNeighboursIfItIsObstacle() {
    Node[] neighbours = d.haeNaapurisolmut(0,0);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 0);
  }

  @Test
  public void cellHasThreeNeighboursIfOneOfThemIsObstacle() {
    Node[] neighbours = d.haeNaapurisolmut(10,1);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 3);
  }

  @Test
  public void cellHasOneNeighbourIfThreeSidesAreBlocked() {
    Node[]  neighbours = d.haeNaapurisolmut(19,1);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 1);
  }

  @Test
  public void cellHasTwoNeighboursIfTwoSidesAreBlocked() {
    Node[]  neighbours = d.haeNaapurisolmut(18,2);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 2);
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
    Dijkstra d = initDijkstraWithNewMap(unreachableGoalMap);
    d.runDijkstra();
    assertTrue(d.getGoalNode() == null);
  }

  @Test
  public void tracesShortestPathBackToNodeClosestToStartingNodeInSpecialMap() throws FileNotFoundException {
    Dijkstra d = initDijkstraWithNewMap(specialCaseMap);
    d.runDijkstra();
    assertTrue(d.getEtaisyysMaaliin() == 10);
    assertTrue(d.haeReitti().getEtaisyys() == 1);
  }
}
