package shortest_path_visualizer.algorithms;

import shortest_path_visualizer.IO.IO;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.MathFunctions;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.dataStructures.Node;

public class AStar {
  /**
   * Algoritmin käyttämä kartta ascii-merkkeinä matriisimuodossa.
   */
  private char[][] karttamatriisi;
  /**
   * Sama kartta, mutta koostuu Node-olioista. Tarvitaan NeighbourFinderia varten. Kun solmumatriisin avulla
   * haetaan naapurisolmuja, päästään käsiksi Node-olioihin tallennettuihin tietoihin, kuten eri etäisyyksiin (f ja g), sekä
   * tietoon onko solmussa vierailtu jne.
   */
  private Node[][] solmumatriisi;
  /**
   * Minimikeko, josta nostetaan aina Node, joka järjestää solmut f-etäisyyden perusteella
   */
  private Keko openList;
  /**
   * Pitää kirjaa käsitellyistä solmuista.
   */
  private boolean[] inClosedList;
  private Node startingNode;
  private Node goalNode;
  /**
   * Etäisyys maaliin g-arvona, eli todellinen etäisyys lähtö- ja maalisolmun välillä
   */
  private double etaisyysMaaliin;
  /**
   * Muuttuja, jonka avulla selvitetään reitti maalisolmusta takaisin lähtösolmuun.
   */
  private Node takaisin;
  private boolean goalFound;
  private NeighbourFinder finder;
  private DynamicArray visitedNodes;
  private MathFunctions math;

  public AStar() {
  }

  /** Alustaa A*:n tarvitsemat rakenteet, kuten keon ja muuttujat. Käytetään myös kartan vaihtamiseen.
   * @param kartta algoritmille selvitettäväksi annettava kartta
   */
  public void setMap(char[][] kartta) {
    this.karttamatriisi = kartta;
    this.solmumatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.inClosedList = new boolean[karttamatriisi.length * karttamatriisi[0].length];
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.goalFound = false;
    this.finder = new NeighbourFinder(karttamatriisi, solmumatriisi);
    this.visitedNodes = new DynamicArray();
    this.math = new MathFunctions();
    this.openList = new Keko();
    initVerkko();
  }

  /**
   * Suorittaa A*-algoritmin. Sijoittaa openListiin aluksi aloitussolmun ja alkaa sitten hakea naapurisolmuja.
   * Naapurit lisätään minimikekoon, josta poimitaan aina solmu, jonka f-etäisyys pienin (f = g-etäisyys + h-etäisyys).
   * Käsitellyt solmut lisätään inClosedListiin ja niihin ei enää kosketa, ellei voida päivittää etäisyyttä paremmaksi.
   * Algoritmin suoritus jatkuu niin kauan, kun openList ei ole tyhjä tai kunnes maalisolmu löytyy.
   */
  public void runAStar() {
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
      for (Node naapuri : haeNaapurisolmut(current.getX(), current.getY())) {
        if (naapuri != null) {
          double uusiGMatka = current.getG_Matka() + neighbourDist(current, naapuri);

          if (!inClosedList[naapuri.getTunnus()] || uusiGMatka < naapuri.getG_Matka()) {
            inClosedList[naapuri.getTunnus()] = true;
            naapuri.setParent(current);
            naapuri.setG_Matka(uusiGMatka);
            double h = diagonalDist(naapuri, goalNode) * 1.001;
            naapuri.setEtaisyys(uusiGMatka + h);
            openList.addNode(naapuri);

            // Tallentaa muistiin vieraillut solmut.
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
   * @return Lyhimmällä löydetyllä reitillä päivitetty karttamatriisi.
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
   * @return lista vierailluista solmuista
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

  /** Heuristiikka, joka arvioi etäsyyden kahden solmun välillä. Käytetään tilanteissa, joissa kartta
   * sallii vinoittaisen liikkumisen ruutujen välillä.
   * @param n1 Ensimmäinen Node
   * @param n2 Toinen Node
   * @return Arvioitu etäisyys Noden n1 ja Noden n2 välillä
   */
  public double diagonalDist(Node n1, Node n2) {
    double dx = math.getAbs(n1.getX() - n2.getX());
    double dy = math.getAbs(n1.getY() - n2.getY());
    return (dx + dy) + (Math.sqrt(2) - 2) * math.getMin(dx, dy);
  }

  public double euclideanDist(Node n1, Node n2) {
    double dx = math.getAbs(n1.getX() - n2.getX());
    double dy = math.getAbs(n1.getY() - n2.getY());
    return Math.sqrt(dx * dx + dy * dy);
  }

  /** Kertoo etäisyyden kahden vierekkäisen solmun välillä. Jos solmut samalla x- tai samalla y-akselilla,
   * etäisyys 1, muuten sqrt(2).
   * @param n1 Ensimmäinen solmu
   * @param n2 Toinen solmu
   * @return Etäisyys solmujen n1 ja n2 välillä.
   */
  public double neighbourDist(Node n1, Node n2) {
    if (n1.getX() == n2.getX() || n1.getY() == n2.getY()) {
      return 1;
    }
    return Math.sqrt(2);
  }

  /**
   * Alustaa solmumatriisin luoden kustakin char[][] -kartan pisteestä Node-olion. Jos kyseessä todellinen solmu (eikä este '@'), sen
   * g-etäisyydeksi ja heuristiseksi etäisyydeksi asetetaan ääretön. Samalla tallennetaan ylös aloitussolmu ja maalisolmu. Tietoa maalisolmusta
   * tarvitaan, jotta A* osaa suunnata reitinhakua sen suuntaan.
   */
  public void initVerkko() {
    int solmutunnus = 0;
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        Node node = new Node(solmutunnus, j, i);
        if (karttamatriisi[i][j] == '@') {
          node = null;
        }
        else {
          node.setG_Matka(Integer.MAX_VALUE);
          node.setEtaisyys(Integer.MAX_VALUE);

          if (karttamatriisi[i][j] == 'G') {
            node.setAsGoalNode();
            this.goalNode = node;
          } else if (karttamatriisi[i][j] == 'S') {
            node.setAsStartNode();
            this.startingNode = node;
          }
        }
        solmumatriisi[i][j] = node;
        solmutunnus++;
      }
    }
  }

  /** Hakee pisteessä (x,y) sijaitsevalle solmulle naapurisolmut.
   * @param x Solmun x-koordinaatti
   * @param y Solmun y-koordinaatti
   * @return Pisteessä (x,y) sijaitsevan solmun naapurisolmut
   */
  private Node[] haeNaapurisolmut(int x, int y) {
    return finder.haeNaapurisolmut(x, y);
  }
}
