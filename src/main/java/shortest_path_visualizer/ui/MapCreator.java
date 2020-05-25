package shortest_path_visualizer.ui;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * JavaFX-toteutus karttojen luomiseen. Toteutetaan myöhemmin...
 */

public class MapCreator extends Application {

  private GridPane grid;
  private int rows;
  private int cols;
  private DrawType type;
  private int currentX;
  private int currentY;

  private int startX;
  private int startY;

  private boolean startDrawn;
  private boolean goalDrawn;

  public MapCreator() {
    this.cols = 30;
    this.rows = 30;
    this.type = DrawType.START;
    this.currentX = 0;
    this.currentY = 0;

    this.startX = 0;
    this.startY = 0;

    this.startDrawn = false;
    this.goalDrawn = false;

  }

  public void createGrid() {

    this.grid = new GridPane();
    grid.setHgap(2);
    grid.setVgap(2);
    grid.setStyle("-fx-background-color: gray;");

    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        int xCoor = x;
        int yCoor = y;
        Rectangle rectangle = new Rectangle(20,20, Color.WHITE);
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (type == DrawType.START) {
              rectangle.setFill(Color.GREEN);

            }
            if (type == DrawType.GOAL) {
              rectangle.setFill(Color.RED);
            }
          }
        });

        rectangle.setOnDragDetected(mouseEvent -> rectangle.startFullDrag());
        rectangle.setOnMouseDragEntered(mouseEvent -> rectangle.setFill(Color.BLACK));

        grid.add(rectangle, x, y);

      }
    }
  }

  public Node getNode (int row, int column, GridPane gridPane) {
    ObservableList<Node> nodes = gridPane.getChildren();
    Node rectToReturn = null;

    for (Node node : nodes) {
      if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
        rectToReturn = node;
        break;
      }
    }
    return rectToReturn;
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    createGrid();

    final ToggleGroup group = new ToggleGroup();
    RadioButton startPoint = new RadioButton("Start point");
    startPoint.setSelected(true);
    startPoint.setToggleGroup(group);
    startPoint.setOnAction(e -> this.type = DrawType.START);

    RadioButton obstacle = new RadioButton("Obstacle");
    obstacle.setToggleGroup(group);
    obstacle.setOnAction(e -> this.type = DrawType.OBSTACLE);

    RadioButton goal = new RadioButton("Goal");
    goal.setToggleGroup(group);
    goal.setOnAction(e -> this.type = DrawType.GOAL);

    VBox radioButtons = new VBox();
    radioButtons.setSpacing(15);
    radioButtons.getChildren().addAll(startPoint, obstacle, goal);

    HBox hB = new HBox(20);
    hB.getChildren().addAll(radioButtons, grid);
    hB.setPadding(new Insets(20));

    Scene scene = new Scene(hB);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
