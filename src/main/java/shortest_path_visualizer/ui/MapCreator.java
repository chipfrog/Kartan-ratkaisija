package shortest_path_visualizer.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class MapCreator extends Application {
  private char[][] cellArray;
  private GridPane grid;
  private int rows;
  private int cols;

  public MapCreator() {
    this.cols = 30;
    this.rows = 30;
  }

  public void createGrid() {
    this.cellArray = new char[rows][cols];
    this.grid = new GridPane();
    grid.setHgap(2);
    grid.setVgap(2);
    grid.setStyle("-fx-background-color: gray;");

    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        Rectangle rectangle = new Rectangle(20, 20, Color.WHITE);
        grid.add(rectangle, x, y);
      }
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    createGrid();
    HBox hBox = new HBox(15);
    hBox.setPadding(new Insets(10));
    Button btn = new Button("Push!");
    Button btn2 = new Button("Don't push!");
    hBox.getChildren().addAll(btn, btn2);

    VBox vBox = new VBox(20);
    vBox.getChildren().addAll(hBox, grid);
    vBox.setPadding(new Insets(20));

    Scene scene = new Scene(vBox);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
