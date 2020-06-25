import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.algorithms.JPS;
import shortest_path_visualizer.dataStructures.DynamicArray;


public class JPSTest {
  File testMap1 = new File("src/test/resources/kartat/testikartta.txt");
  File unreachableGoalMap = new File("src/test/resources/kartat/saavuttamatonmaali.txt");
  File obstacleInWayMap = new File("src/test/resources/kartat/testikartta2.txt");
  File emptyMap = new File("src/test/resources/kartat/emptymap.txt");
  File diagonalMap = new File("src/test/resources/kartat/diagonalPathMap.txt");
  IOStub ioStub = new IOStub();
  MapReader mapReader = new MapReader(ioStub);
  char[][] kartta;
  JPS jps = new JPS();

  private void changeMap(File file) throws FileNotFoundException {
    mapReader.createMatrix(testMap1);
    kartta = mapReader.getMapArray();
    jps.setMap(kartta);
  }

  @Test
  public void returnShortestDistanceOnAStraightLineWIthoutObstacles() throws FileNotFoundException {
    mapReader.createMatrix(testMap1);
    kartta = mapReader.getMapArray();
    jps.setMap(kartta);
    jps.runJPS();
    assertTrue(jps.getGoalNode().getG_Matka() == 5);
  }

  @Test
  public void visitsZeroNodesWhenGoalOnTheSameLineAsStart() throws FileNotFoundException {
    mapReader.createMatrix(emptyMap);
    kartta = mapReader.getMapArray();
    jps.setMap(kartta);
    jps.runJPS();
    DynamicArray nodes = jps.getVisitedNodes();
    assertTrue(jps.getVisitedNodes().size() == 0);
  }

  @Test
  public void doesNotCrashWhenGoalCannotBeReached() throws FileNotFoundException {
    mapReader.createMatrix(unreachableGoalMap);
    kartta = mapReader.getMapArray();
    jps.setMap(kartta);
    jps.runJPS();
    assertTrue(!jps.goalWasFound());
  }

  @Test
  public void findsPathWhenObstaclesInWay() throws FileNotFoundException {
    mapReader.createMatrix(obstacleInWayMap);
    kartta = mapReader.getMapArray();
    jps.setMap(kartta);
    jps.runJPS();
    assertTrue(jps.goalWasFound());
  }

  @Test
  public void getsSameResultAsDijkstraWhenObstaclesInWay() throws FileNotFoundException {
    mapReader.createMatrix(obstacleInWayMap);
    kartta = mapReader.getMapArray();
    Dijkstra dijkstra = new Dijkstra(ioStub);
    dijkstra.setMap(kartta);
    dijkstra.runDijkstra();
    double resultD = dijkstra.getEtaisyysMaaliin();
    jps.setMap(kartta);
    jps.runJPS();
    double resultJ = jps.getGoalNode().getG_Matka();
    assertTrue(resultD == resultJ);
  }

  @Test
  public void findsGoalWithDiagonalScan() throws FileNotFoundException {
    mapReader.createMatrix(diagonalMap);
    kartta = mapReader.getMapArray();
    jps.setMap(kartta);
    jps.runJPS();
    assertTrue(jps.goalWasFound());
  }

}
