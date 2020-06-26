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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import shortest_path_visualizer.IO.BenchmarkFileReader;
import shortest_path_visualizer.IO.MapFileCreator;
import shortest_path_visualizer.IO.MapReader;
import shortest_path_visualizer.algorithms.AStar;
import shortest_path_visualizer.algorithms.Dijkstra;
import shortest_path_visualizer.IO.MapReaderIO;
import shortest_path_visualizer.algorithms.JPS;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.performanceTesting.PerformanceTest;
import shortest_path_visualizer.dataStructures.Node;

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
  private Text errorMessage;
  private boolean runClicked;
  private int animationSpeed;
  private MapFileCreator mapFileCreator;
  private String fileName;
  private long[] runtimes;
  private CheckBox noAnimation;
  private FileChooser fileChooser;
  private FileChooser fileChooserOwnMaps;
  private MapReader mapReader;
  private ArrayList<Line> lines;

  private Label labelD;
  private Label distD;
  private Label nodesD;
  private Label timeD;

  private Label labelA;
  private Label distA;
  private Label nodesA;
  private Label timeA;

  private Label labelJ;
  private Label distJ;
  private Label nodesJ;
  private Label timeJ;

  private File benchmarkFile;
  private File benchmarkMapFile;

  private int testNumber;

  private MapReaderIO io;


  public Ui() {
    this.cols = 60;
    this.rows = 60;
    this.io = new MapReaderIO();
    this.mapArray = new char[rows][cols];
    this.rectChar = new Rectangle[rows][cols];
    this.type = DrawType.START;
    this.startDrawn = false;
    this.goalDrawn = false;
    this.pane = new Pane();
    this.nodeToPaint = 0;
    this.errorMessage = new Text();
    this.runClicked = false;
    this.animationSpeed = 5;
    this.mapFileCreator = new MapFileCreator(io);
    this.fileName = "";
    this.runtimes = new long[101];
    this.noAnimation = new CheckBox();
    this.fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("src/main/resources/maps"));

    this.fileChooserOwnMaps = new FileChooser();
    fileChooserOwnMaps.setInitialDirectory(new File("src/main/resources/ownMaps"));

    this.benchmarkFile = new File("src/main/resources/BenchmarkScenarios/Berlin_0_256.scen.txt");
    this.benchmarkMapFile = new File("src/main/resources/BenchmarkMaps/Berlin_0_256.txt");

    this.mapReader = new MapReader(io);
    this.lines = new ArrayList<>();

    this.labelA = new Label("A*:");
    this.nodesA = new Label("Nodes: ");
    this.timeA = new Label("Time: ");
    this.distA = new Label("Distance: ");

    this.labelD = new Label("Dijkstra:");
    this.nodesD = new Label("Nodes: ");
    this.timeD = new Label("Time: ");
    this.distD = new Label("Distance: ");

    this.labelJ = new Label("JPS:");
    this.nodesJ = new Label("Nodes: ");
    this.timeJ = new Label("Time: ");
    this.distJ = new Label("Distance: ");

    this.testNumber = 0;

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

        if (kartta[y][x] == '@') {
          rectangle.setFill(Color.BLACK);
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

  public void generatePreviouslyMadeMap(char[][] kartta) {
    for (int i = 0; i < rectChar.length; i ++) {
      for (int j = 0; j < rectChar[0].length; j ++) {
        if (kartta[i][j] == '@') {
          rectChar[i][j].setFill(Color.BLACK);
        }
        else if (kartta[i][j] == 'S') {
          rectChar[i][j].setFill(Color.GREEN);
        } else if (kartta[i][j] == 'G') {
          rectChar[i][j].setFill(Color.RED);
        } else {
          rectChar[i][j].setFill(Color.WHITE);
        }
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

    distD.setText("Distance: ");
    distA.setText("Distance: ");
    distJ.setText("Distance: ");

    nodesD.setText("Nodes: ");
    nodesA.setText("Nodes: ");
    nodesJ.setText("Nodes: ");

    timeD.setText("Time: ");
    timeA.setText("Time: ");
    timeJ.setText("Time: ");
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
      this.dijkstra = new Dijkstra();
      for (int i = 0; i < runtimes.length;  i++) {
        dijkstra.setMap(mapArray);
        long t1 = System.nanoTime();
        dijkstra.runDijkstra();
        long t2 = System.nanoTime();
        runtimes[i] = t2 - t1;
      }
      if (dijkstra.getGoalNode() != null) {
        distD.setText("Distance: " + dijkstra.getEtaisyysMaaliin());
        timeD.setText("Time: " + averageRunTime() + " ms");
        DynamicArray visitedNodes = dijkstra.getVisitedOrder();
        if (noAnimation.isSelected()) {
          drawVisitedNodes(visitedNodes, nodesD);
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

      if (jps.goalWasFound()) {
        distJ.setText("Distance: " + jps.getGoalNode().getG_Matka());
        timeJ.setText("Time: " + averageRunTime() + " ms");
        DynamicArray visitedNodes = jps.getVisitedNodes();
        if (noAnimation.isSelected()) {
          drawVisitedNodes(visitedNodes, nodesJ);
          drawJPSPath(jps.getReitti());
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
      this.aStar = new AStar();

      for (int i = 0; i < runtimes.length; i ++) {
        aStar.setMap(mapArray);
        long t1 = System.nanoTime();
        aStar.runAStar();
        long t2 = System.nanoTime();
        runtimes[i] = t2 - t1;
      }

      if (aStar.goalWasFound()) {
        distA.setText("Distance: " + aStar.getEtaisyysMaaliin());
        timeA.setText("Time: " + averageRunTime() + " ms");
        DynamicArray visitedNodes = aStar.getVisitedOrder();

        if (noAnimation.isSelected()) {
          drawVisitedNodes(visitedNodes, nodesA);
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
            nodeToPaint++;
            nodesA.setText("Nodes: " + nodeToPaint);
            nodesA.setText("Nodes: " + nodeToPaint);
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
              nodeToPaint++;
              nodesJ.setText("Nodes: " + nodeToPaint);
            }
          }
      ));
      timeline.setCycleCount(visitedNodes.size() - 1);
      timeline.play();

      timeline.setOnFinished(e -> {
        drawJPSPath(jps.getReitti());
      });
    } else {
      drawJPSPath(jps.getReitti());
    }
  }

  public void animateDijkstra(DynamicArray visitedNodes) {
    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(animationSpeed),
        event -> {
          Node node = visitedNodes.get(nodeToPaint);
          if (node != null) {
            paintSquare(visitedNodes.get(nodeToPaint));
            nodeToPaint++;
            nodesD.setText("Nodes: " + nodeToPaint);
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

  public void drawJPSPath(DynamicArray jumpPoints) {
    for (int i = 0; i < jumpPoints.size() - 1; i ++) {
      Node n1 = jumpPoints.get(i);
      Node n2 = jumpPoints.get(i + 1);

      Rectangle r1 = rectChar[n1.getY()][n1.getX()];
      Rectangle r2 = rectChar[n2.getY()][n2.getX()];

      if (!n1.isStart() && !n1.isGoal()) {
        rectChar[n1.getY()][n1.getX()].setFill(Color.YELLOW);
      }
      if (!n2.isStart() && !n2.isGoal()) {
        rectChar[n2.getY()][n2.getX()].setFill(Color.YELLOW);
      }

      Line line = new Line(r1.getLayoutX() + r1.getWidth()/2, r1.getLayoutY() + r1.getHeight()/2, r2.getLayoutX() + r2.getWidth()/2, r2.getLayoutY() + r2.getHeight()/2);
      lines.add(line);
      pane.getChildren().add(line);
    }
  }

  public void drawVisitedNodes(DynamicArray visitedNodes, Label label) {
    for (int i = 0; i < visitedNodes.size(); i ++) {
      Node node = visitedNodes.get(i);
      if (node != null) {
        paintSquare(node);
        nodeToPaint ++;
      }
    }
    label.setText("Nodes: " + nodeToPaint);
  }

  private void resetSolution() {
    for (int i = 0; i < rectChar.length; i ++) {
      for (int j = 0; j < rectChar[0].length; j ++) {
        if (rectChar[i][j].getFill() == Color.AQUA || rectChar[i][j].getFill() == Color.YELLOW) {
          rectChar[i][j].setFill(Color.WHITE);
        }
      }
    }
    for (Line line : lines) {
      pane.getChildren().remove(line);
    }
    nodeToPaint = 0;
  }

  private void runBenchmark() throws FileNotFoundException {
  }

  private void chooseMap(Stage primaryStage) throws FileNotFoundException {
    File selectedFile = fileChooser.showOpenDialog(primaryStage);
    mapReader.createMatrix(selectedFile);
    mapArray = mapReader.getMapArray();
  }

  private void chooseOwnMap(Stage primaryStage) throws FileNotFoundException {
    File selectedFile = fileChooserOwnMaps.showOpenDialog(primaryStage);
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

    final ToggleGroup benchmarks = new ToggleGroup();
    RadioButton berlin = new RadioButton("Berlin");
    berlin.setSelected(true);
    berlin.setToggleGroup(benchmarks);
    berlin.setOnAction(e -> {
      this.benchmarkFile = new File("src/main/resources/BenchmarkScenarios/Berlin_0_256.scen.txt");
      this.benchmarkMapFile = new File("src/main/resources/BenchmarkMaps/Berlin_0_256.txt");
    });

    RadioButton moscow = new RadioButton("Moscow");
    moscow.setToggleGroup(benchmarks);
    moscow.setOnAction(e -> {
      this.benchmarkFile = new File("src/main/resources/BenchmarkScenarios/Moscow_1_256.map.scen.txt");
      this.benchmarkMapFile = new File("src/main/resources/BenchmarkMaps/Moscow_1_256.map.txt");
    });

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
        startDrawn = false;
        goalDrawn = false;
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

    Button openUserMadeMap = new Button("Select saved map");
    openUserMadeMap.setOnAction(e -> {
      try {
        erasePreMadeMap();
        resetMap(primaryStage);
        createGrid(15);
        chooseOwnMap(primaryStage);
        generatePreviouslyMadeMap(mapArray);
      } catch (Exception exception) {
      }

    });

    VBox mapSaving = new VBox();
    mapSaving.getChildren().addAll(saveMap, nameField, save, selectMap, openUserMadeMap);
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

    HBox benchmarkChoice = new HBox();
    benchmarkChoice.getChildren().addAll(berlin, moscow);
    benchmarkChoice.setSpacing(5);

    VBox configurations = new VBox();
    Label algo = new Label("Algorithm:");
    configurations.getChildren().addAll(algo, comboBox, speedSlider, slider, noAnimation);
    configurations.setSpacing(10);

    VBox otherOptions = new VBox();
    otherOptions.getChildren().addAll(run, tryAgain, clear);
    otherOptions.setSpacing(10);

    Label avgD = new Label();
    Label avgA = new Label();
    Label avgJ = new Label();

    Button runTest = new Button("Benchmark");
    runTest.setOnAction(e -> {
      System.out.println("Benchmark started!");
      try {
        BenchmarkFileReader b = new BenchmarkFileReader(io);
        Node[][] startAndGoal = b.getScenarioCoordinates(benchmarkFile);

        double totalTimeD = 0;
        double totalTimeA = 0;
        double totalTimeJ = 0;

        PerformanceTest test = new PerformanceTest(5);
        for (int i = 0; i < startAndGoal.length; i++) {
          test.testDijkstra(benchmarkMapFile, startAndGoal[i][0], startAndGoal[i][1]);
          totalTimeD += test.getAverage();

          test.testAStar(benchmarkMapFile, startAndGoal[i][0], startAndGoal[i][1]);
          totalTimeA += test.getAverage();

          test.testJPS(benchmarkMapFile, startAndGoal[i][0], startAndGoal[i][1]);
          totalTimeJ += test.getAverage();

          System.out.println("Scenario " + i);
        }

        avgD.setText("Dijkstra: " + totalTimeD + " ms");
        avgA.setText("A*: " + totalTimeA + " ms");
        avgJ.setText("JPS: " + totalTimeJ + " ms");

      } catch (Exception exception) {
      }
    });


    VBox allResults = new VBox();
    allResults.setSpacing(20);
    allResults.setPadding(new Insets(20));

    VBox resultsD = new VBox();
    resultsD.getChildren().addAll(labelD, distD, nodesD, timeD);
    resultsD.setSpacing(10);

    VBox resultsA = new VBox();
    resultsA.getChildren().addAll(labelA, distA, nodesA, timeA);
    resultsA.setSpacing(10);

    VBox resultsJ = new VBox();
    resultsJ.getChildren().addAll(labelJ, distJ, nodesJ, timeJ);
    resultsJ.setSpacing(10);

    allResults.getChildren().addAll(resultsD, resultsA, resultsJ);

    VBox benchmarkResults = new VBox();
    benchmarkResults.getChildren().addAll(avgD, avgA, avgJ);
    benchmarkResults.setSpacing(10);

    VBox controls = new VBox();
    controls.setSpacing(40);
    controls.getChildren().addAll(drawChoice, configurations, otherOptions, mapSaving, runTest, benchmarkChoice, benchmarkResults);

    VBox layout = new VBox();
    layout.setSpacing(20);
    layout.setPadding(new Insets(20));

    HBox hB = new HBox(20);
    hB.getChildren().addAll(controls, pane, allResults);
    hB.setSpacing(30);
    hB.setPadding(new Insets(20));
    primaryStage.setHeight(1000);
    primaryStage.setWidth(1600);

    errorMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
    errorMessage.setFill(Color.RED);

    layout.getChildren().addAll(hB, errorMessage);

    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
