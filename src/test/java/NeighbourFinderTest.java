import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.utils.Node;

public class NeighbourFinderTest {
  NeighbourFinder finder;
  IOStub io= new IOStub();
  File testMap;
  MapReader mapReader = new MapReader(io);
  Dijkstra dijkstra;


  @Before
  public void init() throws FileNotFoundException {
    this.dijkstra = new Dijkstra(io);
    this.testMap = new File("src/test/resources/kartat/testikartta.txt");
    mapReader.createMatrix(testMap);
    char[][] map = mapReader.getMapArray();
    dijkstra.setMap(map);
    dijkstra.initSolmumatriisi();
    this.finder = new NeighbourFinder(map, dijkstra.getSolmuMatriisi());
  }

  @Test
  public void cellHasNoNeighboursIfItIsObstacle() {
    Node[] neighbours = dijkstra.haeNaapurisolmut(0,0);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 0);
  }

  @Test
  public void cellHasFiveNeighboursIfThreeOfThemAreObstacles() {
    Node[] neighbours = dijkstra.haeNaapurisolmut(5,1);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 5);
  }

  @Test
  public void cellHasNoNeighbourIfItIsWall() {
    Node[]  neighbours = dijkstra.haeNaapurisolmut(0,0);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 0);
  }

  @Test
  public void cellHasTSixNeighboursIfTwoSidesAreBlocked() {
    Node[]  neighbours = dijkstra.haeNaapurisolmut(18,2);
    int nodes = 0;
    for (int i = 0; i < neighbours.length; i ++) {
      if (neighbours[i] != null) {
        nodes ++;
      }
    }
    assertTrue(nodes == 6);
  }




}
