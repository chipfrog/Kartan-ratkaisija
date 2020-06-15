package shortest_path_visualizer.algorithms;

import java.util.ArrayList;
import shortest_path_visualizer.IO.IO;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.utils.Node;

public class AStar {
  private final IO io;
  private char[][] karttamatriisi;
  private Node[][] solmumatriisi;
  private Keko openList;
  private Node[][] verkko;
  private boolean[][] addedToOpenList;
  private Node startingNode;
  private Node goalNode;
  private double etaisyysMaaliin;
  private ArrayList<Node> visitedOrder;
  private Node takaisin;
  private boolean goalFound;
  private NeighbourFinder finder;

  public AStar(IO io) {
    this.io = io;
  }

  public void setMap(char[][] kartta) {
    this.karttamatriisi = kartta;
    this.solmumatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.addedToOpenList = new boolean[karttamatriisi.length][karttamatriisi[0].length];
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.visitedOrder = new ArrayList<>();
    this.goalFound = false;
    this.finder = new NeighbourFinder(karttamatriisi, solmumatriisi);
    initVerkko();
  }

  /**
   * Suorittaa A*-algoritmin.
   */
  public void runAStar() {
    //initVerkko();
    this.openList = new Keko();
    startingNode.setG_Matka(0);
    startingNode.setEtaisyys(diagonalDist(startingNode, goalNode));
    openList.addNode(startingNode);

    while (!openList.isEmpty()) {
      Node current = openList.pollNode();
      addedToOpenList[current.getY()][current.getX()] = false;
      if (current.isGoal()) {
        goalFound = true;
        etaisyysMaaliin = current.getG_Matka();
        break;
      }
      current.vieraile();
      for (Node naapuri : haeNaapurisolmut(current.getX(), current.getY())) {
        if (naapuri != null) {
          double uusiGMatka = current.getG_Matka() + neighbourDist(current, naapuri);
          if (!naapuri.onVierailtu() || uusiGMatka < naapuri.getG_Matka()) {
            naapuri.setParent(current);
            naapuri.setG_Matka(uusiGMatka);
            double h = diagonalDist(naapuri, goalNode); //* 1.001;
            naapuri.setEtaisyys(uusiGMatka + h);

            openList.addNode(naapuri);
            addedToOpenList[naapuri.getY()][naapuri.getX()] = true;

            if (!naapuri.onVierailtu()) {
              visitedOrder.add(naapuri);
              naapuri.vieraile();
            }

            /*if (!addedToOpenList[naapuri.getY()][naapuri.getX()]) {
              openList.addNode(naapuri);
              addedToOpenList[naapuri.getY()][naapuri.getX()] = true;
              visitedOrder.add(naapuri);
              naapuri.vieraile();
            } else if (naapuri.onVierailtu()) {
              openList.addNode(naapuri);
            }*/
          }
        }
      }
    }
  }

  public boolean goalWasFound() {
    return goalFound;
  }

  public void printMap() {
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        io.printChar(karttamatriisi[i][j]);
      }
      io.printString("");
    }
  }

  /** Päivittää karttamatriisiin reitin maalisolmusta lähtösolmuun. Katsoo maalisolmusta alkaen, mikä solmu on edeltävän solmun vanhempi jne.
   * @return (Toisinaan) lyhimmällä reitillä päivitetty karttamatriisi
   */
  public char[][] getReitti() {
    takaisin = goalNode.getParent();
    while (!takaisin.isStart()) {
      karttamatriisi[takaisin.getY()][takaisin.getX()] = 'X';
      takaisin = takaisin.getParent();
    }
    return karttamatriisi;
  }

  /** Palauttaa listan vierailluista solmuista vierailujärjestyksessä.
   * @return listga vierailluista solmuista
   */
  public ArrayList<Node> getVisitedOrder() {
    return visitedOrder;
  }

  public double getEtaisyysMaaliin() {
    return etaisyysMaaliin;
  }

  /** Laskee Manhattan-etäisyyden parametrina annettujen solmujen välille.
   * @param n1 Solmu 1
   * @param n2 Solmu 2
   * @return Manhattan-etäisyys solmujen 1 ja 2 välillä
   */
  public int manhattanDist(Node n1, Node n2) {
    return Math.abs(n1.getX() - n2.getX()) + Math.abs(n1.getY() - n2.getY());
  }

  public double diagonalDist(Node n1, Node n2) {
    double dx = Math.abs(n1.getX() - n2.getX());
    double dy = Math.abs(n1.getY() - n2.getY());
    return (dx + dy) + (Math.sqrt(2) - 2) * Math.min(dx, dy);
  }

  public double neighbourDist(Node n1, Node n2) {
    if (n1.getX() == n2.getX() || n1.getY() == n2.getY()) {
      return 1;
    }
    return Math.sqrt(2);
  }

  /**
   * Alustaa verkon. Luo kuhunkin matriisin solmuun uuden solmun ja tallentaa sille listan naapurisolmuista.
   */
  public void initVerkko() {
    int solmut = karttamatriisi.length * karttamatriisi[0].length;
    this.verkko = new Node[solmut][];

    // Täytetään solmumatriisi solmuolioilla. Merkitään mikä on aloitussolmu ja mikä maalisolmu.
    int solmutunnus = 0;
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        Node node = new Node(solmutunnus, j, i);
        node.setG_Matka(Integer.MAX_VALUE);
        node.setEtaisyys(Integer.MAX_VALUE);

        if (karttamatriisi[i][j] == 'G') {
          node.setAsGoalNode();
          this.goalNode = node;
        } else if (karttamatriisi[i][j] == 'S') {
          node.setAsStartNode();
          //node.setG_Matka(0);
          this.startingNode = node;
        }
        solmumatriisi[i][j] = node;
        solmutunnus++;
      }
    }
    /*// Lisätään kullekin solmumatriisin solmulle lista naapurisolmuista
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        verkko[solmumatriisi[i][j].getTunnus()] = haeNaapurisolmut(j, i);
      }
    }*/
  }

  private Node[] haeNaapurisolmut(int x, int y) {
    return finder.haeNaapurisolmut(x, y);
  }
}