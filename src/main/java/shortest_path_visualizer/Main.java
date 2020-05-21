package shortest_path_visualizer;

import javafx.application.Application;
import shortest_path_visualizer.ui.MapCreator;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    //Application.launch(MapCreator.class, args);


    MapReader reader = new MapReader(new MapReaderIO());
    reader.createMatrix(new File("src/main/resources/testikartta.txt"));
    reader.printMap();

    /*Dijkstra d = new Dijkstra(new MapReaderIO(), reader.getMapArray());
    d.initVerkko();
    d.printSolmumatriisi();
    d.printKaaret();*/

  }
}
