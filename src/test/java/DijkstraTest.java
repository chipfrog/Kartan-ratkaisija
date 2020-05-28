import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.Dijkstra;
import shortest_path_visualizer.MapReader;
import shortest_path_visualizer.Node;

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

  }
  private Dijkstra initDijkstraWithNewMap(File file) throws FileNotFoundException{
    mapReader.createMatrix(file);
    char[][] newMapMatrix = mapReader.getMapArray();
    return new Dijkstra(ioStub, newMapMatrix);
  }

  @Test
  public void cellHasFourNeighboursIfNoneOfThemIsObstacle() {
    ArrayList<Node> neighbours = d.haeNaapurisolmut(30,4);
    assertTrue(neighbours.size() == 4);
  }

  @Test
  public void cellHasNoNeighboursIfItIsObstacle() {
    ArrayList<Node> neighbours = d.haeNaapurisolmut(0,0);
    assertTrue(neighbours.size() == 0);
  }

  @Test
  public void cellHasThreeNeighboursIfOneOfThemIsObstacle() {
    ArrayList<Node> neighbours = d.haeNaapurisolmut(10,1);
    assertTrue(neighbours.size() == 3);
  }

  @Test
  public void cellHasOneNeighbourIfThreeSidesAreBlocked() {
    ArrayList<Node> neighbours = d.haeNaapurisolmut(19,1);
    assertTrue(neighbours.size() == 1);
  }

  @Test
  public void cellHasTwoNeighboursIfTwoSidesAreBlocked() {
    ArrayList<Node> neighbours = d.haeNaapurisolmut(18,2);
    assertTrue(neighbours.size() == 2);
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









  /*@Test
  public void mapSavedAsIntegerMatrixContainsAllCells() {
    d.initVerkko();
    assertTrue(d.getSolmumatriisi().length * d.getSolmumatriisi()[0].length == 2401);
  }
*/


}
