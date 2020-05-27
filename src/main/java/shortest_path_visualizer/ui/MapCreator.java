package shortest_path_visualizer.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import shortest_path_visualizer.Dijkstra;

/**
 * JavaFX-toteutus karttojen luomiseen.
 */

public class MapCreator extends Application {
  private Pane pane;
  private char[][] mapArray;
  private int rows;
  private int cols;
  private DrawType type;
  private boolean startDrawn;
  private boolean goalDrawn;
  private Rectangle[][] rectChar;
  private Dijkstra dijkstra;

  public MapCreator() {
    this.cols = 50;
    this.rows = 50;
    this.mapArray = new char[rows][cols];
    this.rectChar = new Rectangle[rows][cols];
    this.type = DrawType.START;
    this.startDrawn = false;
    this.goalDrawn = false;
    this.pane = new Pane();
  }

  public void createGrid() {
    double xPikselit = 0;
    double yPikselit = 0;

    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        Rectangle rectangle = new Rectangle(20,20, Color.WHITE);
        if (x == 0) {
          xPikselit = 0;
        } else {
          xPikselit += 20;
        }
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
        yPikselit = y * 20;
        rectangle.setStyle("-fx-stroke: gray; -fx-stroke-width: 2;");
        rectangle.setLayoutX(xPikselit);
        rectangle.setLayoutY(yPikselit);
        rectChar[y][x] = rectangle;
        pane.getChildren().add(rectangle);
      }
    }
  }

  public void resetMap(Stage stage) throws Exception {
    this.type = DrawType.START;
    startDrawn = false;
    goalDrawn = false;
    start(stage);
  }

  public char[][] generateCharArray() {
    for (int i = 0; i < rectChar.length; i ++) {
      for (int j = 0; j < rectChar[0].length; j ++) {
        Rectangle rect = rectChar[i][j];
        if (rect.getFill() == Color.WHITE) {
          mapArray[i][j] = '.';
        } else if (rect.getFill() == Color.BLACK) {
          mapArray[i][j] = 'T';
        } else if (rect.getFill() == Color.GREEN) {
          mapArray[i][j] = 'S';
        } else {
          mapArray[i][j] = 'G';
        }
      }
    }
    return mapArray;
  }

  public void solveMapUsingDijkstra() {
    generateCharArray();
    this.dijkstra = new Dijkstra(mapArray);
    dijkstra.initVerkko();
    dijkstra.runDijkstra();

    drawSolvedMap(dijkstra.getSolvedMap());
  }

  public void drawSolvedMap(char[][] solvedMap) {
    for (int i = 0; i < solvedMap.length; i ++) {
      for (int j = 0; j < solvedMap[0].length; j ++) {
        if (solvedMap[i][j] == 'O') {
          rectChar[i][j].setFill(Color.TEAL);
        } else if (solvedMap[i][j] == 'X') {
          rectChar[i][j].setFill(Color.YELLOW);
        }
      }
    }
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

    Button genArray = new Button("Dijkstra");
    genArray.setOnAction(e -> solveMapUsingDijkstra());

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
    hB.getChildren().addAll(buttonMenu, pane);
    hB.setPadding(new Insets(20));

    Scene scene = new Scene(hB);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
