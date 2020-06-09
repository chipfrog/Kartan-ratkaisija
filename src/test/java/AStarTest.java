import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.algorithms.AStar;
import shortest_path_visualizer.IO.MapReader;

public class AStarTest {
  File testMap = new File("src/test/resources/kartat/testikartta.txt");
  IOStub ioStub = new IOStub();
  MapReader mapReader = new MapReader(ioStub);
  char[][] mapMatrix;
  AStar a;

  @Before
  public void initMatrix() throws FileNotFoundException {
    mapReader.createMatrix(testMap);
    mapMatrix = mapReader.getMapArray();
    a = new AStar(ioStub, mapMatrix);
    a.initVerkko();
  }

  @Test
  public void returnsShortetsDistanceInBasicSituation() {
    a.runAStar();
    assertTrue(a.getEtaisyysMaaliin() == 5);
  }

}
