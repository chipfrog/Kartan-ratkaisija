package shortest_path_visualizer;

import java.io.FileNotFoundException;
import javafx.application.Application;
import shortest_path_visualizer.performanceTesting.PerformanceTest;
import shortest_path_visualizer.ui.MapCreator;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {

    Application.launch(MapCreator.class);

    /*PerformanceTest test = new PerformanceTest();
    test.testAStar();
    test.getAverage();
    test.testDijkstra();
    test.getAverage();

    System.out.println(test.getVastausD());
    System.out.println(test.getVastausA());*/


  }
}
