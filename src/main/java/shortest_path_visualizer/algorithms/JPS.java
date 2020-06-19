package shortest_path_visualizer.algorithms;

import java.sql.SQLOutput;
import java.util.ArrayList;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.MathFunctions;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.utils.Node;

public class JPS {
  private char[][] kartta;
  private Node[][] solmumatriisi;
  private Node goalNode;
  private Node startingNode;
  private Keko jumpPoints;
  private MathFunctions math;
  private boolean goalFound;
  private DynamicArray visitedNodes;

  public JPS(char[][] kartta) {
    this.kartta = kartta;
    this.solmumatriisi = new Node[kartta.length][kartta[0].length];
    this.jumpPoints = new Keko();
    this.math = new MathFunctions();
    this.goalFound = false;
    this.visitedNodes = new DynamicArray();
  }

  public void runJPS() {
    initSolmumatriisi();
    int x = startingNode.getX();
    int y = startingNode.getY();

    // Luodaan aloitussolmut. Kaikki lähtevät samasta pisteestä, mutta eri suuntiin.
    for (int i = -1; i < 2; i ++) {
      for (int j = -1; j < 2; j++ ) {
        if (i == 0 && j == 0) {
        } else {
          Node n = new Node(x, y, j, i, 0);
          n.setEtaisyys(diagonalDist(n, goalNode));
          n.setAsStartNode();
          jumpPoints.addNode(n);
        }
      }
    }
    while (!jumpPoints.isEmpty()) {
      Node current = jumpPoints.pollNode();
      if (!current.isStart()) {
        visitedNodes.add(current);
      }

      if (goalFound) {
        System.out.println("Maali löytyi!");
        break;
      }
      if (current.getDirH() != 0 && current.getDirV() != 0) {
        diagonalScan(current);
      } else if (current.getDirH() != 0 && current.getDirV() == 0) {
        horizontalScan(current);
      } else if (current.getDirV() != 0 && current.getDirH() == 0) {
        verticalScan(current);
      }
    }
  }
  public DynamicArray getVisitedNodes() {
    return this.visitedNodes;
  }

  public Node getGoalNode() {
    return this.goalNode;
  }

  public void initSolmumatriisi() {
    int solmutunnus = 0;
    for (int i = 0; i < kartta.length; i++) {
      for (int j = 0; j < kartta[0].length; j++) {
        Node node = new Node(solmutunnus, j, i);
        node.setG_Matka(Integer.MAX_VALUE);
        node.setEtaisyys(Integer.MAX_VALUE);

        if (kartta[i][j] == 'G') {
          node.setAsGoalNode();
          this.goalNode = node;
        } else if (kartta[i][j] == 'S') {
          node.setAsStartNode();
          node.setG_Matka(0);
          node.setEtaisyys(0);
          this.startingNode = node;
        }
        solmumatriisi[i][j] = node;
        solmutunnus++;
      }
    }
  }

  public void horizontalScan(Node parent) {
    int x = parent.getX();
    int y = parent.getY();
    int dirH = parent.getDirH();
    double distance = parent.getG_Matka();
    int xNext = x;

    while (true) {
      if (dirH == 0) {
        break;
      }
      xNext += dirH;
      distance += 1;

      if (xNext < 0 || xNext > kartta[0].length - 1) {
        break;
      }
      if (kartta[y][xNext] == '@') {
        break;
      }
      if (kartta[y][xNext] == 'G') {
        goalFound = true;
        System.out.println("Maalietäisyys: " + distance);
        goalNode.setG_Matka(distance);
        break;
      }

      if (xNext + dirH >= 0 && xNext + dirH < kartta[0].length
          && y + 1 < kartta.length && y - 1 >= 0) {
        if (kartta[y][xNext + dirH] != '@' && kartta[y - 1][xNext + dirH] == '@') {
          Node node = new Node(xNext, y, dirH, -1, distance);
          node.setEtaisyys(distance + diagonalDist(node, goalNode));
          jumpPoints.addNode(node);
        }

        if (kartta[y][xNext +dirH] != '@' && kartta[y + 1][xNext + dirH] == '@') {
          Node node = new Node(xNext, y, dirH, 1, distance);
          node.setEtaisyys(distance + diagonalDist(node, goalNode));
          jumpPoints.addNode(node);
        }
      }
    }
  }

  public void verticalScan(Node parent) {
    int x = parent.getX();
    int y = parent.getY();
    int dirV = parent.getDirV();
    double distance = parent.getG_Matka();
    int yNext = y;

    while (true) {
      if (dirV == 0) {
        break;
      }
      yNext += dirV;
      distance += 1;
      if (yNext < 0 || yNext > kartta[0].length - 1) {
        break;
      }
      if (kartta[yNext][x] == '@') {
        break;
      }
      if (kartta[yNext][x] == 'G') {
        goalFound = true;
        System.out.println("Maalietäisyys: " + distance);
        goalNode.setG_Matka(distance);
        break;
      }
      if (yNext + dirV >= 0 && yNext + dirV < kartta.length
          && x + 1 < kartta[0].length && x - 1 >= 0) {

        if (kartta[yNext + dirV][x] != '@' && kartta[yNext + dirV][x - 1] == '@') {
          Node node = new Node(x, yNext, -1, dirV, distance);
          node.setEtaisyys(distance + diagonalDist(node, goalNode));
          jumpPoints.addNode(node);
        }
        if (kartta[yNext + dirV][x] != '@' && kartta[yNext + dirV][x + 1] == '@') {
          Node node = new Node(x, yNext, 1, dirV, distance);
          node.setEtaisyys(distance + diagonalDist(node, goalNode));
          jumpPoints.addNode(node);
        }
      }
    }
  }

  public void diagonalScan(Node parent) {
    int x = parent.getX();
    int y = parent.getY();
    int dirH = parent.getDirH();
    int dirV = parent.getDirV();
    double distance = parent.getG_Matka();
    int yNext = y;
    int xNext = x;

    while (true) {
      xNext += dirH;
      yNext += dirV;
      distance += Math.sqrt(2);

      if (xNext > kartta[0].length - 1|| xNext < 0 || yNext > kartta.length - 1 || yNext < 0) {
        break;
      }
      if (kartta[yNext][xNext] == '@') {
        break;
      }
      if(kartta[yNext][xNext] == 'G') {
        goalFound = true;
        goalNode.setG_Matka(distance);
        System.out.println("Maalietäisyys: " + distance);
        break;
      }
      Node newParent = new Node(xNext, yNext, dirH, dirV, distance);
      horizontalScan(newParent);
      verticalScan(newParent);

      if (kartta[yNext][x] == '@' && kartta[yNext + dirV][x] != '@') {
        Node node = new Node(xNext, yNext, -(dirH), dirV, distance);
        node.setEtaisyys(distance + diagonalDist(node, goalNode));
        jumpPoints.addNode(node);
      }
      if (kartta[y][xNext] == '@' && kartta[y][xNext + dirH] != '@') {
        Node node = new Node(xNext, yNext, dirH, -(dirV), distance);
        node.setEtaisyys(distance + diagonalDist(node, goalNode));
        jumpPoints.addNode(node);
      }
    }
  }

  public double diagonalDist(Node n1, Node n2) {
    double dx = math.getAbs(n1.getX() - n2.getX());
    double dy = math.getAbs(n1.getY() - n2.getY());
    return (dx + dy) + (Math.sqrt(2) - 2) * math.getMin(dx, dy);
  }

}











