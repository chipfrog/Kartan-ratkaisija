package shortest_path_visualizer.algorithms;

import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.Node;

public class JPS {
  private char[][] kartta;
  private Node[][] solmumatriisi;
  private Node goalNode;
  private Node startingNode;
  private Keko keko;

  public JPS(char[][] kartta, Node[][] solmumatriisi) {
    this.kartta = kartta;
    this.solmumatriisi = solmumatriisi;
    this.keko = new Keko();

  }

  public void runJPS() {
    initSolmumatriisi();




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
          this.startingNode = node;
        }
        solmumatriisi[i][j] = node;
        solmutunnus++;
      }
    }
  }

  public ArrayList<Node> horizontalScan(int x, int y, int dirH, double distance) {
    ArrayList<Node> jumpPoints = new ArrayList<>();

    while (true) {
      int x1 = x + dirH;
      if (x1 > kartta[0].length || x1 < 0) {
        return jumpPoints;
      }
      if(kartta[y][x1] == '@') {
        return jumpPoints;
      }
      if (kartta [y][x1] == 'G') {
        Node node = solmumatriisi[y][x1];
        node.setG_Matka(distance + 1);
        jumpPoints.add(node);
        return jumpPoints;
      }

      distance = distance + 1;
      int x2 = x1 + dirH;

      if (kartta[y - 1][x1] == '@' && kartta[y - 1][x2] != '@') {
        Node node = solmumatriisi[y][x1];
        node.setDirH(dirH);
        node.setDirV(-1);
        node.setG_Matka(distance);
        jumpPoints.add(node);
      }

      if (kartta[y + 1][x1] == '@' && kartta[y + 1][x2] != '@') {
        Node node = solmumatriisi[y][x1];
        node.setDirH(dirH);
        node.setDirV(1);
        node.setG_Matka(distance);
        jumpPoints.add(node);
      }

      if (jumpPoints.size() > 0) {
        Node node = solmumatriisi[y][x1];
        node.setDirH(dirH);
        node.setDirV(0);
        node.setG_Matka(distance);
        jumpPoints.add(node);
      }
      return jumpPoints;

    }
  }

  public ArrayList<Node> verticalScan(int x, int y, int dirV, double distance) {
    ArrayList<Node> jumpPoints = new ArrayList<>();

    while (true) {
      int y1 = y + dirV;
      if (y1 > kartta[0].length || y1 < 0) {
        return jumpPoints;
      }
      if(kartta[y1][x] == '@') {
        return jumpPoints;
      }
      if (kartta [y1][x] == 'G') {
        Node node = solmumatriisi[y1][x];
        node.setG_Matka(distance + 1);
        jumpPoints.add(node);
        return jumpPoints;
      }

      distance = distance + 1;
      int y2 = y1 + dirV;

      if (kartta[y1][x - 1] == '@' && kartta[y2][x - 1] != '@') {
        Node node = solmumatriisi[y1][x];
        node.setDirV(dirV);
        node.setDirH(-1);
        node.setG_Matka(distance);
        jumpPoints.add(node);
      }

      if (kartta[y1][x + 1] == '@' && kartta[y2][x + 1] != '@') {
        Node node = solmumatriisi[y1][x];
        node.setDirV(dirV);
        node.setDirH(1);
        node.setG_Matka(distance);
        jumpPoints.add(node);
      }

      if (jumpPoints.size() > 0) {
        Node node = solmumatriisi[y1][x];
        node.setDirV(dirV);
        node.setDirH(0);
        node.setG_Matka(distance);
        jumpPoints.add(node);
      }
      return jumpPoints;
    }
  }

  public ArrayList<Node> diagonalScan(int x, int y, int dirH, int dirV, double distance) {
    ArrayList<Node> jumpPoints = new ArrayList<>();
    ArrayList<Node> horizontalNodes = new ArrayList<>();
    ArrayList<Node> verticalNodes = new ArrayList<>();
    boolean horScanDone = false;
    boolean verScanDone = false;

    while (true) {
      int x1 = x + dirH;
      int y1 = y + dirV;

      if (x1 > kartta[0].length || x1 < 0 || y1 > kartta.length || y1 < 0) {
        return jumpPoints;
      }

      if(kartta[y1][x1] == '@') {
        return jumpPoints;
      }

      if(kartta[y1][x1] == 'G') {
        Node node = solmumatriisi[y1][x1];
        node.setG_Matka(distance + Math.sqrt(2));
        jumpPoints.add(node);
        return jumpPoints;
      }

      distance = distance + Math.sqrt(2);
      int x2 = x1 + dirH;
      int y2 = y1 + dirV;

      if (kartta[y1][x] == '@' && kartta[y2][x] != '@') {
        Node node = solmumatriisi[y1][x1];
        node.setG_Matka(distance);
        node.setDirH(-1);
        node.setDirV(1);
        jumpPoints.add(node);
      }

      if (kartta[y][x1] == '@' && kartta[y][x2] != '@') {
        Node node = solmumatriisi[y1][x1];
        node.setG_Matka(distance);
        node.setDirH(1);
        node.setDirV(-1);
        jumpPoints.add(node);
        horScanDone = false;
        verScanDone = false;
      }

      if (jumpPoints.size() == 0) {
        horizontalNodes = horizontalScan(x1, y1, dirH, distance);
        horScanDone = true;
      }

      if (horizontalNodes.size() > 0) {
        Node parent = horizontalNodes.get(horizontalNodes.size() - 1);

        for (Node node : horizontalNodes) {
          node.setParent(parent);
        }
        jumpPoints.add(parent);

        if (jumpPoints.size() == 0) {
          verticalNodes = verticalScan(x1, y1, dirV, distance);
          verScanDone = true;

          if (verticalNodes.size() > 0) {
            parent = verticalNodes.get(verticalNodes.size() - 1);
            for (Node node : verticalNodes) {
              node.setParent(parent);
            }
            jumpPoints.add(parent);

            if(jumpPoints.size() > 0) {
              if (!horScanDone) {
                Node node = solmumatriisi[y1][x1];
                node.setDirH(dirH);
                node.setDirV(0);
                node.setG_Matka(distance);
                jumpPoints.add(node);
              }

              if (!verScanDone) {
                Node node = solmumatriisi[y1][x1];
                node.setDirH(0);
                node.setDirV(dirV);
                node.setG_Matka(distance);
                jumpPoints.add(node);

                node.setDirH(dirH);
                jumpPoints.add(node);

                return jumpPoints;
              }
            }
          }
        }

      }

    }
  }

}











