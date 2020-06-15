package shortest_path_visualizer.performanceTesting;

import java.io.File;
import java.io.FileNotFoundException;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.IO.MapReaderIO;
import shortest_path_visualizer.algorithms.AStar;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.utils.Node;

public class PerformanceTest {
  MapReaderIO io = new MapReaderIO();
  MapReader mapReader = new MapReader(io);
  private Dijkstra dijkstra;
  private AStar aStar;
  private char[][] map;
  private long[] times;
  private int vastausD;
  private double vastausA;
  private int kierroksia;

  public PerformanceTest(int kierroksia) {
    this.kierroksia = kierroksia;
    this.io = new MapReaderIO();
    this.mapReader = new MapReader(io);
    this.times = new long[kierroksia];
  }

  /*public char[][] initMap() throws FileNotFoundException {
    mapReader.createMatrix(file);
    return mapReader.getMapArray();
  }*/

  public void testDijkstra(File file, Node start, Node goal) throws FileNotFoundException {
    mapReader.createMatrix(file);
    this.map = mapReader.getMapArray();
    this.map[start.getY()][start.getX()] = 'S';
    this.map[goal.getY()][goal.getX()] = 'G';
    this.dijkstra = new Dijkstra(new MapReaderIO());
    dijkstra.setMap(this.map);

    dijkstra.runDijkstra();
    this.vastausD = dijkstra.getEtaisyysMaaliin();

    for (int i = 0; i < kierroksia; i ++) {
      dijkstra.setMap(map);
      long t1 = System.nanoTime();
      dijkstra.runDijkstra();
      long t2 = System.nanoTime();
      times[i] = t2 - t1;
    }
  }



  public void testAStar(File file, Node start, Node goal) throws FileNotFoundException {
    mapReader.createMatrix(file);
    this.map = mapReader.getMapArray();
    this.map = mapReader.getMapArray();
    this.map[start.getY()][start.getX()] = 'S';
    this.map[goal.getY()][goal.getX()] = 'G';

    for (int i = 0; i < map.length; i ++) {
      for (int j = 0; j < map[0].length; j ++) {
        io.printChar(map[i][j]);
      }
      System.out.println();
    }
    /*this.aStar = new AStar(new MapReaderIO());
    aStar.setMap(map);
    aStar.runAStar();
    this.vastausA = aStar.getEtaisyysMaaliin();

    for (int i = 0; i < kierroksia; i ++) {
      aStar.setMap(map);
      long t1 = System.nanoTime();
      aStar.runAStar();
      long t2 = System.nanoTime();
      times[i] = t2 - t1;
    }*/
  }

  public double getAverage() {
    long totalTime = 0;
    long divider = 0;
    for (int i = 1; i < times.length; i ++) {
      totalTime += times[i];
      divider ++;
    }
    return totalTime/1000000.0/divider;
  }

  public boolean samaVastaus() {
    return vastausA == vastausD;
  }

  public double getVastausA() {
    return this.vastausA;
  }

  public int getVastausD() {
    return this.vastausD;
  }
}