package shortest_path_visualizer.algorithms;

import shortest_path_visualizer.IO.IO;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.MathFunctions;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.utils.Node;

public class AStar {
  private final IO io;
  private char[][] karttamatriisi;
  private Node[][] solmumatriisi;
  private Keko openList;
  private Node[][] verkko;
  private double[] etaisyys;
  private boolean[][] addedToOpenList;
  private Node startingNode;
  private Node goalNode;
  private double etaisyysMaaliin;
  private Node takaisin;
  private boolean goalFound;
  private NeighbourFinder finder;
  private DynamicArray visitedNodes;
  private MathFunctions math;

  public AStar(IO io) {
    this.io = io;
  }

  public void setMap(char[][] kartta) {
    this.karttamatriisi = kartta;
    this.solmumatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.addedToOpenList = new boolean[karttamatriisi.length][karttamatriisi[0].length];
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.goalFound = false;
    this.finder = new NeighbourFinder(karttamatriisi, solmumatriisi);
    this.etaisyys = new double[karttamatriisi.length * karttamatriisi[0].length];
    this.visitedNodes = new DynamicArray();
    this.math = new MathFunctions();
    initVerkko();
    initEtaisyydet();
  }

  /**
   * Suorittaa A*-algoritmin.
   */
  public void runAStar() {
    this.openList = new Keko();
    startingNode.setG_Matka(0);
    startingNode.setEtaisyys(diagonalDist(startingNode, goalNode));
    openList.addNode(startingNode);

    while (!openList.isEmpty()) {
      Node current = openList.pollNode();

      if (current.isGoal()) {
        goalFound = true;
        etaisyysMaaliin = current.getG_Matka();
        break;
      }
      //current.vieraile();
      for (Node naapuri : haeNaapurisolmut(current.getX(), current.getY())) {
        if (naapuri != null) {
          double uusiGMatka = current.getG_Matka() + neighbourDist(current, naapuri);

          if (etaisyys[naapuri.getTunnus()] == Integer.MAX_VALUE || uusiGMatka < naapuri.getG_Matka()) {
            etaisyys[naapuri.getTunnus()] = uusiGMatka;
            naapuri.setParent(current);
            naapuri.setG_Matka(uusiGMatka);
            double h = diagonalDist(naapuri, goalNode); //* 1.001;
            naapuri.setEtaisyys(uusiGMatka + h);
            openList.addNode(naapuri);

            if (!naapuri.onVierailtu()) {
              visitedNodes.add(naapuri);
              naapuri.vieraile();
            }
          }
        }
      }
    }
  }

  public boolean goalWasFound() {
    return goalFound;
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
  public DynamicArray getVisitedOrder() {
    return visitedNodes;
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
    double dx = math.getAbs(n1.getX() - n2.getX());
    double dy = math.getAbs(n1.getY() - n2.getY());
    return (dx + dy) + (Math.sqrt(2) - 2) * math.getMin(dx, dy);
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
  }

  public void initEtaisyydet() {
    for (int i = 0; i < etaisyys.length; i++) {
      etaisyys[i] = Integer.MAX_VALUE;
    }
    etaisyys[startingNode.getTunnus()] = 0;
  }

  private Node[] haeNaapurisolmut(int x, int y) {
    return finder.haeNaapurisolmut(x, y);
  }
}
