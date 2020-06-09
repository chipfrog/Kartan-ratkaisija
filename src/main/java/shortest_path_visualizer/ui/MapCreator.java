package shortest_path_visualizer.ui;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
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
import javafx.util.Duration;
import shortest_path_visualizer.algorithms.AStar;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.IO.MapReaderIO;
import shortest_path_visualizer.utils.Node;

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
  private AStar aStar;
  private int nodeToPaint;
  private Label numOfVisitedNodes;
  private Label distToGoal;
  private boolean runClicked;
  private int animationSpeed;

  public MapCreator() {
    this.cols = 50;
    this.rows = 50;
    this.mapArray = new char[rows][cols];
    this.rectChar = new Rectangle[rows][cols];
    this.type = DrawType.START;
    this.startDrawn = false;
    this.goalDrawn = false;
    this.pane = new Pane();
    this.nodeToPaint = 0;
    this.numOfVisitedNodes = new Label("Nodes: " + 0);
    this.distToGoal = new Label("Distance: ");
    this.runClicked = false;
    this.animationSpeed = 5;
  }

  /**
   * Luo ruudukon, jonka ruudut voi värittää. Musta = este, vihreä = aloitussolmu, punainen = maalisolmu, valkoinen = tavallinen solmu.
   */
  public void createGrid(int sivu) {
    double xPikselit = 0;
    double yPikselit = 0;

    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        Rectangle rectangle = new Rectangle(sivu, sivu, Color.WHITE);
        if (x == 0) {
          xPikselit = 0;
        } else {
          xPikselit += sivu;
        }
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
              if (type == DrawType.OBSTACLE && rectangle.getFill() != Color.GREEN) {
                rectangle.setFill(Color.BLACK);
              } else if (type == DrawType.START && !startDrawn && rectangle.getFill() != Color.RED) {
                rectangle.setFill(Color.GREEN);
                startDrawn = true;
              } else if (type == DrawType.GOAL && !goalDrawn && rectangle.getFill() != Color.GREEN) {
                rectangle.setFill(Color.RED);
                goalDrawn = true;
              }
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
              if (rectangle.getFill() == Color.GREEN) {
                startDrawn = false;
              } else if (rectangle.getFill() == Color.RED) {
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
              if (rectangle.getFill() != Color.GREEN && rectangle.getFill() != Color.RED) {
                rectangle.setFill(Color.BLACK);
              }
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
              if (rectangle.getFill() == Color.GREEN) {
                startDrawn = false;
              } else if (rectangle.getFill() == Color.RED) {
                goalDrawn = false;
              }
              rectangle.setFill(Color.WHITE);
            }
          }
        });
        yPikselit = y * sivu;
        rectangle.setStyle("-fx-stroke: lightgray; -fx-stroke-width: 2;");
        rectangle.setLayoutX(xPikselit);
        rectangle.setLayoutY(yPikselit);
        rectChar[y][x] = rectangle;
        pane.getChildren().add(rectangle);
      }
    }
  }

  /**
   * Pyyhkii ruudukon tyhjäksi
   *
   * @param stage
   * @throws Exception
   */
  public void resetMap(Stage stage) throws Exception {
    this.type = DrawType.START;
    startDrawn = false;
    goalDrawn = false;
    nodeToPaint = 0;
    numOfVisitedNodes.setText("Nodes: " + 0);
    distToGoal.setText("Distance: ");
    start(stage);
  }

  /**
   * Luo char-matriisin piirretystä ruudukosta.
   *
   * @return char-matriisi / kartta
   */
  public char[][] generateCharArray() {
    for (int i = 0; i < rectChar.length; i++) {
      for (int j = 0; j < rectChar[0].length; j++) {
        Rectangle rect = rectChar[i][j];
        if (rect.getFill() == Color.WHITE) {
          mapArray[i][j] = '.';
        } else if (rect.getFill() == Color.BLACK) {
          mapArray[i][j] = 'T';
        } else if (rect.getFill() == Color.GREEN) {
          mapArray[i][j] = 'S';
        } else if (rect.getFill() == Color.RED) {
          mapArray[i][j] = 'G';
        }
      }
    }
    return mapArray;
  }

  private boolean mapHasStartAndGoal() {
    boolean startAdded = false;
    boolean goalAdded = false;
    for (int i = 0; i < rectChar.length; i++) {
      for (int j = 0; j < rectChar[0].length; j++) {
        if (mapArray[i][j] == 'S') {
          startAdded = true;
        } else if (mapArray[i][j] == 'G') {
          goalAdded = true;
        }
      }
    }
    return (startAdded && goalAdded);
  }

  /**
   * Käyttää dijkstran algoritmia ja suorittaa ruudukon värittämismetodit.
   */
  public void solveMapUsingDijkstra() {
    generateCharArray();
    if (mapHasStartAndGoal()) {
      this.dijkstra = new Dijkstra(new MapReaderIO(), mapArray);
      dijkstra.runDijkstra();
      if (dijkstra.getGoalNode() != null) {
        distToGoal.setText("Dist: " + dijkstra.getEtaisyysMaaliin());
        ArrayList<Node> visitedNodes = dijkstra.getVisitedOrder();
        animateDijkstra(visitedNodes);
      } else {
        System.out.println("Goal node unreachable!");
      }
    } else {
      System.out.println("You must add start and goal!");
    }
  }

  public void solveMapUsingAStar() {
    generateCharArray();
    if (mapHasStartAndGoal()) {
      this.aStar = new AStar(new MapReaderIO(), mapArray);
      aStar.runAStar();
      if (aStar.goalWasFound()) {
        distToGoal.setText("Dist: " + aStar.getEtaisyysMaaliin());
        ArrayList<Node> visitedNodes = aStar.getVisitedOrder();
        animateAStar(visitedNodes);
      } else {
        System.out.println("Goal node unreachable!");
      }
    } else {
      System.out.println("You must add start and goal!");
    }
  }

  /**
   * Dijkstran algoritmin animaatio. Värittää uuden vieraillun solmun/ruudun tasaisin aikavälein. Värittää lopuksi lyhimmän reitin maalisolmusta aloitussolmuun.
   *
   * @param visitedNodes Vieraillut solmut.
   */
  public void animateAStar(ArrayList<Node> visitedNodes) {
    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(animationSpeed),
        event -> {
          paintSquare(visitedNodes.get(nodeToPaint));
          numOfVisitedNodes.setText("Nodes: " + nodeToPaint);
          nodeToPaint++;
        }
    ));
    timeline.setCycleCount(visitedNodes.size() - 1);
    timeline.play();

    timeline.setOnFinished(e -> {
      drawShortestPath(aStar.getReitti());
    });
  }

  public void animateDijkstra(ArrayList<Node> visitedNodes) {
    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(animationSpeed),
        event -> {
          paintSquare(visitedNodes.get(nodeToPaint));
          numOfVisitedNodes.setText("Nodes: " + nodeToPaint);
          nodeToPaint++;
        }
    ));
    timeline.setCycleCount(visitedNodes.size() - 1);
    timeline.play();

    timeline.setOnFinished(e -> {
      drawShortestPath(dijkstra.getSolvedMap());
    });
  }

  /**
   * Värittää parametrina annettavan solmun.
   *
   * @param node Väritettävä solmu
   */
  public void paintSquare(Node node) {
    if (!node.isGoal() && !node.isStart()) {
      rectChar[node.getY()][node.getX()].setFill(Color.AQUA);
    }
  }

  /**
   * Värittää lyhimmän reitin maalisolmun ja aloitussolmun välille.
   *
   * @param solvedMap ratkaistu kartta/ char-matriisi
   */
  public void drawShortestPath(char[][] solvedMap) {
    for (int i = 0; i < solvedMap.length; i++) {
      for (int j = 0; j < solvedMap[0].length; j++) {
        if (solvedMap[i][j] == 'X') {
          rectChar[i][j].setFill(Color.YELLOW);
        }
      }
    }
  }

  private void resetSolution() {
    for (int i = 0; i < rectChar.length; i ++) {
      for (int j = 0; j < rectChar[0].length; j ++) {
        if (rectChar[i][j].getFill() == Color.AQUA || rectChar[i][j].getFill() == Color.YELLOW) {
          rectChar[i][j].setFill(Color.WHITE);
        }
      }
    }
    nodeToPaint = 0;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    createGrid(20);

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

    ComboBox <String> comboBox = new ComboBox();
    comboBox.getItems().addAll("A*", "Dijkstra");

    Button run = new Button("Run");
    run.setOnAction(e -> {
      if (!runClicked) {
        if (comboBox.getValue() == null) {
          System.out.println("Valitse algoritmi!");
        }
        else if (comboBox.getValue().equals("A*")) {
          runClicked = true;
          solveMapUsingAStar();
        }
        else if (comboBox.getValue().equals("Dijkstra")) {
          runClicked = true;
          solveMapUsingDijkstra();
        }
      }
    });

    Button tryAgain = new Button("Erase solution");
    tryAgain.setOnAction(e -> {
      resetSolution();
      runClicked = false;
    });

    Button clear = new Button("Clear");
    clear.setOnAction(e -> {
      try {
        runClicked = false;
        resetMap(primaryStage);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    });
    Label speedSlider = new Label("Animation speed: ");

    Slider slider = new Slider();
    slider.setMin(1);
    slider.setMax(50);
    slider.setValue(5);
    slider.setBlockIncrement(5);
    slider.setMinorTickCount(5);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider.setSnapToTicks(true);

    slider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                          Number newValue) {
        animationSpeed = newValue.intValue();
      }
    });

    VBox drawChoice = new VBox();
    Label draw = new Label("Draw:");
    drawChoice.getChildren().addAll(draw, startPoint, goal, obstacle);
    drawChoice.setSpacing(10);

    VBox configurations = new VBox();
    Label algo = new Label("Algorithm:");
    configurations.getChildren().addAll(algo, comboBox, speedSlider, slider);
    configurations.setSpacing(10);

    VBox otherOptions = new VBox();
    otherOptions.getChildren().addAll(run, tryAgain, clear, numOfVisitedNodes, distToGoal);
    otherOptions.setSpacing(10);

    VBox controls = new VBox();
    controls.setSpacing(40);
    controls.getChildren().addAll(drawChoice, configurations, otherOptions);

    HBox hB = new HBox(20);
    hB.getChildren().addAll(controls, pane);
    hB.setSpacing(30);
    hB.setPadding(new Insets(20));

    Scene scene = new Scene(hB);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
