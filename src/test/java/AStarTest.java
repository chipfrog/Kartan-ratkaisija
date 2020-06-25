import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.algorithms.AStar;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.dataStructures.DynamicArray;

public class AStarTest {
  File testMap = new File("src/test/resources/kartat/testikartta.txt");
  File unreachableGoal = new File("src/test/resources/kartat/saavuttamatonmaali.txt");
  IOStub ioStub = new IOStub();
  MapReader mapReader = new MapReader(ioStub);
  char[][] mapMatrix;
  AStar a;

  @Before
  public void initMatrix() throws FileNotFoundException {
    mapReader.createMatrix(testMap);
    mapMatrix = mapReader.getMapArray();
    a = new AStar(ioStub);
    a.setMap(mapMatrix);
  }

  private void changeMap(File file) throws FileNotFoundException {
    mapReader.createMatrix(file);
    mapMatrix = mapReader.getMapArray();
    a = new AStar(ioStub);
    a.setMap(mapMatrix);
  }

  @Test
  public void returnsShortetsDistanceInBasicSituationWithoutObstacles() {
    a.runAStar();
    assertTrue(a.getEtaisyysMaaliin() == 5);
  }

  @Test
  public void getsSameResultAsDijkstra() throws FileNotFoundException {
    changeMap(new File("src/test/resources/kartat/virhe.txt"));
    Dijkstra dijkstra = new Dijkstra(ioStub);
    dijkstra.setMap(mapMatrix);
    dijkstra.runDijkstra();
    double resultD = dijkstra.getEtaisyysMaaliin();
    a.runAStar();
    double resultA = a.getEtaisyysMaaliin();
    System.out.println(resultA);
    System.out.println(resultD);
    assertTrue(resultA - resultD < 0.000001);
  }

  @Test
  public void visitRightNumberOfNodes() throws FileNotFoundException {
    a.runAStar();
    DynamicArray nodes = a.getVisitedOrder();
    int visitedNodes = 0;
    for (int i = 0; i < nodes.size(); i ++) {
      if (nodes.get(i) != null)
      visitedNodes ++;
    }
    assertTrue(visitedNodes == 21);
  }

  @Test
  public void doesNotCrashWhenGoalCannotBeReached() throws FileNotFoundException {
    changeMap(unreachableGoal);
    a.runAStar();
    assertFalse(a.goalWasFound());
  }

}
