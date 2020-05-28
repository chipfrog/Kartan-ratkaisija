package shortest_path_visualizer;

import javafx.application.Application;
import shortest_path_visualizer.ui.MapCreator;

public class Main {
  public static void main(String[] args) {

    Application.launch(MapCreator.class);
    //MapReader reader = new MapReader(new MapReaderIO());
    //reader.createMatrix(new File("src/main/resources/testikartta.txt"));
    //reader.printMap();
  }
}
