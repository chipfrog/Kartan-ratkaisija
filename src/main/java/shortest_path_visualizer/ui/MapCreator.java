package shortest_path_visualizer.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * JavaFX-toteutus karttojen luomiseen.
 */

public class MapCreator extends Application {

  private GridPane grid;
  private char[][] mapArray;
  private int rows;
  private int cols;
  private DrawType type;
  private boolean startDrawn;
  private boolean goalDrawn;

  public MapCreator() {
    this.cols = 50;
    this.rows = 50;
    this.mapArray = new char[rows][cols];
    this.type = DrawType.START;
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
        Rectangle rectangle = new Rectangle(20,20, Color.WHITE);

        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
              if (type == DrawType.OBSTACLE) {
                rectangle.setFill(Color.BLACK);
              }
              else if (type == DrawType.START && !startDrawn) {
                rectangle.setFill(Color.GREEN);
                startDrawn = true;
              }
              else if (type == DrawType.GOAL && !goalDrawn) {
                rectangle.setFill(Color.RED);
                goalDrawn = true;
              }
            }
            else if (event.getButton().equals(MouseButton.SECONDARY)) {
              if (rectangle.getFill() == Color.GREEN) {
                startDrawn = false;
              }
              else if (rectangle.getFill() == Color.RED) {
                goalDrawn = false;
              }
              rectangle.setFill(Color.WHITE);
            }
          }
        });

        rectangle.setOnDragDetected(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            rectangle.startFullDrag();
          }
        });

        rectangle.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
          @Override
          public void handle(MouseDragEvent event) {
            if (event.getButton().equals(MouseButton.PRIMARY) && type == DrawType.OBSTACLE) {
              if (rectangle.getFill() == Color.GREEN) {
                startDrawn = false;
              } else if (rectangle.getFill() == Color.RED) {
                goalDrawn = false;
              }
              rectangle.setFill(Color.BLACK);
            }
            else if (event.getButton().equals(MouseButton.SECONDARY)) {
              if (rectangle.getFill() == Color.GREEN) {
                startDrawn = false;
              } else if (rectangle.getFill() == Color.RED) {
                goalDrawn = false;
              }
              rectangle.setFill(Color.WHITE);
            }
          }
        });
        grid.add(rectangle, x, y);
      }
    }
  }

  public void resetMap(Stage stage) throws Exception {
    this.type = DrawType.START;
    startDrawn = false;
    goalDrawn = false;
    start(stage);
  }

  public char[][] generateCharArray(GridPane gridPane) {
    for (Node node : gridPane.getChildren()) {
      int row = GridPane.getRowIndex(node);
      int column = GridPane.getColumnIndex(node);
      Rectangle rect = (Rectangle)node;
      if (rect.getFill() == Color.WHITE) {
        mapArray[row][column] = '.';
      } else if (rect.getFill() == Color.BLACK) {
        mapArray[row][column] = 'X';
      } else if (rect.getFill() == Color.GREEN) {
        mapArray[row][column] = 'S';
      } else {
        mapArray[row][column] = 'G';
      }
    }
    for (int i = 0; i < mapArray.length; i ++) {
      for (int j = 0; j < mapArray[0].length; j ++) {
        System.out.print(mapArray[i][j]);
      }
      System.out.println();
    }
    return mapArray;
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

    VBox buttonMenu = new VBox();

    Button genArray = new Button("Generate");
    genArray.setOnAction(e -> generateCharArray(grid));

    Button reset = new Button("Reset");
    reset.setOnAction(e -> {
      try {
        resetMap(primaryStage);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    });

    buttonMenu.setSpacing(15);
    buttonMenu.getChildren().addAll(startPoint, goal, obstacle, genArray, reset);

    HBox hB = new HBox(20);
    hB.getChildren().addAll(buttonMenu, grid);
    hB.setPadding(new Insets(20));

    Scene scene = new Scene(hB);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
