package shortest_path_visualizer.ui;

import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import shortest_path_visualizer.IO.MapFileCreator;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.algorithms.AStar;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.IO.MapReaderIO;
import shortest_path_visualizer.algorithms.JPS;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.performanceTesting.PerformanceTest;
import shortest_path_visualizer.utils.Node;

/**
 * JavaFX-toteutus karttojen luomiseen.
 */

public class Ui extends Application {
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
  private JPS jps;
  private int nodeToPaint;
  private Label numOfVisitedNodes;
  private Label distToGoal;
  private Label runTime;
  private Text errorMessage;
  private boolean runClicked;
  private int animationSpeed;
  private MapFileCreator mapFileCreator;
  private String fileName;
  private long[] runtimes;
  private CheckBox noAnimation;
  private FileChooser fileChooser;
  private MapReader mapReader;


  public Ui() {
    this.cols = 60;
    this.rows = 60;
    this.mapArray = new char[rows][cols];
    this.rectChar = new Rectangle[rows][cols];
    this.type = DrawType.START;
    this.startDrawn = false;
    this.goalDrawn = false;
    this.pane = new Pane();
    this.nodeToPaint = 0;
    this.numOfVisitedNodes = new Label("Nodes: " + 0);
    this.distToGoal = new Label("Distance: " + 0);
    this.runTime = new Label("Time: ");
    this.errorMessage = new Text();
    this.runClicked = false;
    this.animationSpeed = 5;
    this.mapFileCreator = new MapFileCreator(new MapReaderIO());
    this.fileName = "";
    this.runtimes = new long[101];
    this.noAnimation = new CheckBox();
    this.fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("src/main/resources/maps"));
    this.mapReader = new MapReader(new MapReaderIO());

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

  public void preMadeMap(char[][] kartta, int sivu) {
    double xPikselit = 0;
    double yPikselit = 0;

    for (int y = 0; y < kartta.length; y++) {
      for (int x = 0; x < kartta[0].length; x++) {
        Rectangle rectangle = new Rectangle(sivu, sivu, Color.WHITE);
        if (kartta[y][x] == '@') {
          rectangle.setFill(Color.BLACK);
        }
        else if (kartta[y][x] == 'S') {
          rectangle.setFill(Color.GREEN);
        } else if (kartta[y][x] == 'G') {
          rectangle.setFill(Color.RED);
        }
        if (x == 0) {
          xPikselit = 0;
        } else {
          xPikselit += sivu;
        }
        yPikselit = y * sivu;
        rectangle.setLayoutX(xPikselit);
        rectangle.setLayoutY(yPikselit);
        rectChar[y][x] = rectangle;
        pane.getChildren().add(rectangle);
      }
    }
  }

  public void resetMapMatrixAndRectangleMatrix() {
    this.rectChar = new Rectangle[rows][cols];
    this.mapArray = new char[rows][cols];
  }

  public void erasePreMadeMap() {
    this.pane = new Pane();
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
    distToGoal.setText("Distance: " + 0);
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
          mapArray[i][j] = '@';
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

  public double averageRunTime() {
    long totalTime = 0;
    int divider = 0;
    for (int i = 1; i < runtimes.length; i ++) {
      totalTime += runtimes[i];
      divider ++;
    }
    return totalTime/1000000.0/divider;
  }

  /**
   * Käyttää dijkstran algoritmia ja suorittaa ruudukon värittämismetodit.
   */
  public void solveMapUsingDijkstra() {
    generateCharArray();
    if (mapHasStartAndGoal()) {
      this.dijkstra = new Dijkstra(new MapReaderIO());
      for (int i = 0; i < runtimes.length;  i++) {
        dijkstra.setMap(mapArray);
        long t1 = System.nanoTime();
        dijkstra.runDijkstra();
        long t2 = System.nanoTime();
        runtimes[i] = t2 - t1;
      }
      if (dijkstra.getGoalNode() != null) {
        distToGoal.setText("Distance: " + dijkstra.getEtaisyysMaaliin());
        runTime.setText("Time: " + averageRunTime() + "ms");
        DynamicArray visitedNodes = dijkstra.getVisitedOrder();
        if (noAnimation.isSelected()) {
          drawVisitedNodes(visitedNodes);
          drawShortestPath(dijkstra.getSolvedMap());
        } else {
          animateDijkstra(visitedNodes);
        }
      } else {
        errorMessage.setText("Goal node unreachable!");
        runClicked = false;
      }
    } else {
      errorMessage.setText("You must add start and goal!");
      runClicked = false;
    }
  }

  public void solveMapUsingJPS() {
    generateCharArray();
    if (mapHasStartAndGoal()) {
      this.jps = new JPS();

      for (int i = 0; i < runtimes.length; i ++) {
        jps.setMap(mapArray);
        long t1 = System.nanoTime();
        jps.runJPS();
        long t2 = System.nanoTime();
        runtimes[i] = t2 - t1;
      }

      if (jps.getGoalNode() != null) {
        distToGoal.setText("Distance: " + jps.getGoalNode().getG_Matka());
        runTime.setText("Time: " + averageRunTime() + "ms");
        DynamicArray visitedNodes = jps.getVisitedNodes();
        if (noAnimation.isSelected()) {
          drawVisitedNodes(visitedNodes);
        } else {
          animateJPS(visitedNodes);
        }
      } else {
        errorMessage.setText("Goal node unreachable!");
        runClicked = false;
      }
    } else {
      errorMessage.setText("You must add start and goal!");
      runClicked = false;
    }
  }

  public void solveMapUsingAStar() {
    generateCharArray();
    if (mapHasStartAndGoal()) {
      this.aStar = new AStar(new MapReaderIO());

      for (int i = 0; i < runtimes.length; i ++) {
        aStar.setMap(mapArray);
        long t1 = System.nanoTime();
        aStar.runAStar();
        long t2 = System.nanoTime();
        runtimes[i] = t2 - t1;
      }

      if (aStar.goalWasFound()) {
        distToGoal.setText("Dist: " + aStar.getEtaisyysMaaliin());
        runTime.setText("Time: " + averageRunTime() + "ms");
        DynamicArray visitedNodes = aStar.getVisitedOrder();

        if (noAnimation.isSelected()) {
          drawVisitedNodes(visitedNodes);
          drawShortestPath(aStar.getReitti());
        } else {
          animateAStar(visitedNodes);
        }
      } else {
        errorMessage.setText("Goal node unreachable!");
        runClicked = false;
      }
    } else {
      errorMessage.setText("You must add start and goal!");
      runClicked = false;
    }
  }

  /**
   * Dijkstran algoritmin animaatio. Värittää uuden vieraillun solmun/ruudun tasaisin aikavälein. Värittää lopuksi lyhimmän reitin maalisolmusta aloitussolmuun.
   *
   * @param visitedNodes Vieraillut solmut.
   */

  public void animateAStar(DynamicArray visitedNodes) {
    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(animationSpeed),
        event -> {
          Node node = visitedNodes.get(nodeToPaint);
          if (node != null) {
            paintSquare(visitedNodes.get(nodeToPaint));
            numOfVisitedNodes.setText("Nodes: " + (nodeToPaint + 1));
            nodeToPaint++;
          }
        }
    ));
    timeline.setCycleCount(visitedNodes.size() - 1);
    timeline.play();

    timeline.setOnFinished(e -> {
      drawShortestPath(aStar.getReitti());
    });
  }

  public void animateJPS(DynamicArray visitedNodes) {
    if (visitedNodes.size() != 0) {
      Timeline timeline = new Timeline(new KeyFrame(
          Duration.millis(animationSpeed),
          event -> {
            Node node = visitedNodes.get(nodeToPaint);
            if (node != null) {
              paintSquare(visitedNodes.get(nodeToPaint));
              numOfVisitedNodes.setText("Nodes: " + (nodeToPaint + 1));
              nodeToPaint++;
            }
          }
      ));
      timeline.setCycleCount(visitedNodes.size() - 1);
      timeline.play();
    }
  }

  public void animateDijkstra(DynamicArray visitedNodes) {
    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(animationSpeed),
        event -> {
          Node node = visitedNodes.get(nodeToPaint);
          if (node != null) {
            paintSquare(visitedNodes.get(nodeToPaint));
            numOfVisitedNodes.setText("Nodes: " + (nodeToPaint + 1));
            nodeToPaint++;
          }
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

  public void drawVisitedNodes(DynamicArray visitedNodes) {
    for (int i = 0; i < visitedNodes.size(); i ++) {
      Node node = visitedNodes.get(i);
      if (node != null) {
        paintSquare(node);
        nodeToPaint ++;
      }
    }
    numOfVisitedNodes.setText("Nodes: " + nodeToPaint);
  }

  private void resetSolution() {
    for (int i = 0; i < rectChar.length; i ++) {
      for (int j = 0; j < rectChar[0].length; j ++) {
        if (rectChar[i][j].getFill() == Color.AQUA || rectChar[i][j].getFill() == Color.YELLOW) {
          rectChar[i][j].setFill(Color.WHITE);
        }
      }
    }
    numOfVisitedNodes.setText("Nodes: " + 0);
    distToGoal.setText("Distance: " + 0);
    nodeToPaint = 0;
  }

  private void runBenchmark() throws FileNotFoundException {
  }

  private void chooseMap(Stage primaryStage) throws FileNotFoundException {
    File selectedFile = fileChooser.showOpenDialog(primaryStage);
    mapReader.createMatrix(selectedFile);
    mapArray = mapReader.getMapArray();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    resetMapMatrixAndRectangleMatrix();
    createGrid(15);

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
    comboBox.getItems().addAll("Dijkstra", "A*", "JPS");

    Button run = new Button("Run");
    run.setOnAction(e -> {
      errorMessage.setText("");
      if (!runClicked) {
        if (comboBox.getValue() == null) {
          errorMessage.setText("Valitse algoritmi!");
        }
        else if (comboBox.getValue().equals("Dijkstra")) {
          runClicked = true;
          solveMapUsingDijkstra();
        }
        else if (comboBox.getValue().equals("A*")) {
          runClicked = true;
          solveMapUsingAStar();
        }
        else if (comboBox.getValue().equals("JPS")) {
          runClicked = true;
          solveMapUsingJPS();
        }

      }
    });

    Button tryAgain = new Button("Erase solution");
    tryAgain.setOnAction(e -> {
      errorMessage.setText("");
      resetSolution();
      runClicked = false;
    });

    Button clear = new Button("Clear");
    clear.setOnAction(e -> {
      errorMessage.setText("");
      try {
        runClicked = false;
        erasePreMadeMap();
        resetMap(primaryStage);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    });



    Label saveMap = new Label("Save map");
    TextField nameField = new TextField();

    Button selectMap = new Button("Select map");
    selectMap.setOnAction(e -> {
      try {
        chooseMap(primaryStage);
        rectChar = new Rectangle[mapArray.length][mapArray.length];
        preMadeMap(mapArray, 4);
      } catch (FileNotFoundException exception) {
      }
    });

    Button save = new Button("Save Map");
    save.setOnAction(e -> {
      generateCharArray();
      mapFileCreator.WriteMapToFile(mapArray, nameField.getText());
      nameField.setText("");
    });

    VBox mapSaving = new VBox();
    mapSaving.getChildren().addAll(saveMap, nameField, save, selectMap);
    mapSaving.setSpacing(10);

    Label speedSlider = new Label("Animation delay: ");

    Slider slider = new Slider();
    slider.setMin(1);
    slider.setMax(50);
    slider.setValue(1);
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

    noAnimation.setText("No animation");
    noAnimation.setSelected(false);

    VBox drawChoice = new VBox();
    Label draw = new Label("Draw:");
    drawChoice.getChildren().addAll(draw, startPoint, goal, obstacle);
    drawChoice.setSpacing(10);

    VBox configurations = new VBox();
    Label algo = new Label("Algorithm:");
    configurations.getChildren().addAll(algo, comboBox, speedSlider, slider, noAnimation);
    configurations.setSpacing(10);

    VBox otherOptions = new VBox();
    otherOptions.getChildren().addAll(run, tryAgain, clear, numOfVisitedNodes, distToGoal, runTime);
    otherOptions.setSpacing(10);

    Label avgD = new Label();
    Label avgA = new Label();
    Label distA = new Label();
    Label distD = new Label();

    Button runTest = new Button("Benchmark");
    runTest.setOnAction(e -> {
      /*try {
        PerformanceTest test = new PerformanceTest();

        test.testAStar(new File("src/test/resources/kartat/Brushfire.txt"));
        avgA.setText("A* average: " + test.getAverage() + "ms");
        distA.setText("Path length: " + test.getVastausA());

        test.testDijkstra(new File("src/test/resources/kartat/Brushfire.txt"));
        avgD.setText("Dijkstra average: " + test.getAverage() + "ms");
        distD.setText("Path length: " + test.getVastausD());

      } catch (FileNotFoundException exception) {
      }*/
    });

    VBox controls = new VBox();
    controls.setSpacing(40);
    controls.getChildren().addAll(drawChoice, configurations, otherOptions, mapSaving, runTest, avgA, distA, avgD, distD);

    VBox layout = new VBox();
    layout.setSpacing(20);
    layout.setPadding(new Insets(20));

    HBox hB = new HBox(20);
    hB.getChildren().addAll(controls, pane);
    hB.setSpacing(30);
    hB.setPadding(new Insets(20));

    errorMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
    errorMessage.setFill(Color.RED);

    layout.getChildren().addAll(hB, errorMessage);

    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
