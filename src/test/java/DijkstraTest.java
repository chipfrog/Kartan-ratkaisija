import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.Dijkstra;
import shortest_path_visualizer.MapReader;

public class DijkstraTest {
  File testMap = new File("src/test/resources/testikartta.txt");
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

  @Test
  public void cellHasFourNeighboursIfNoneOfThemIsObstacle() {
    ArrayList<Integer> neighbours = d.haeNaapurisolmut(30,4);
    assertTrue(neighbours.size() == 4);
  }

  @Test
  public void cellHasNoNeighboursIfItIsObstacle() {
    ArrayList<Integer> neighbours = d.haeNaapurisolmut(0,0);
    assertTrue(neighbours.size() == 0);
  }

  @Test
  public void cellHasThreeNeighboursIfOneOfThemIsObstacle() {
    ArrayList<Integer> neighbours = d.haeNaapurisolmut(10,1);
    assertTrue(neighbours.size() == 3);
  }

  @Test
  public void cellHasOneNeighbourIfThreeSidesAreBlocked() {
    ArrayList<Integer> neighbours = d.haeNaapurisolmut(19,1);
    assertTrue(neighbours.size() == 1);
  }

  @Test
  public void cellHasTwoNeighboursIfTwoSidesAreBlocked() {
    ArrayList<Integer> neighbours = d.haeNaapurisolmut(18,2);
    assertTrue(neighbours.size() == 2);
  }

  @Test
  public void mapSavedAsIntegerMatrixContainsAllCells() {
    d.initVerkko();
    assertTrue(d.getSolmumatriisi().length * d.getSolmumatriisi()[0].length == 2401);
  }



}
