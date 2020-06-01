package shortest_path_visualizer;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Luokka, jossa toteutetaan Dijkstran algoritmi.
 */

public class Dijkstra {
  private final IO io;
  private final char[][] karttamatriisi;
  private final Node[][] solmuMatriisi;

  private PriorityQueue<Node> keko;
  private int[] etaisyys;
  private Node [][] verkko;
  private Node startingNode;
  private Node goalNode;
  private ArrayList<Node> visitedOrder;
  private int etaisyysMaaliin;
  private int iNaapurilista;
  private Keko heap;

  public Dijkstra(IO io, char[][] karttamatriisi) {
    this.io = io;
    this.karttamatriisi = karttamatriisi;
    this.solmuMatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.etaisyys = new int[karttamatriisi.length * karttamatriisi[0].length];
    this.keko = new PriorityQueue<>();
    this.visitedOrder = new ArrayList<>();
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.goalNode = null;
    this.iNaapurilista = 0;
    this.heap = new Keko();
  }

  /**
   * Suorittaa Dijkstran algoritmin.
   */
  public void runDijkstra() {
    initVerkko();
    initEtaisyydet();
    heap.addNode(startingNode);

    while(!heap.isEmpty()) {
      Node node = heap.pollNode();
      if (node.onVierailtu()) {
        continue;
      }
      node.vieraile();
      visitedOrder.add(node);
      if (node.getGoal()) {
        this.goalNode = node;
        this.etaisyysMaaliin = node.getEtaisyys();
        break;
      }
      if (!node.getStart()) {
        karttamatriisi[node.getY()][node.getX()] = 'O';
      }

      for (Node naapuri : verkko[node.getTunnus()]) {
        if (naapuri != null) {
          int nykyinenEtaisyys = etaisyys[naapuri.getTunnus()];
          int uusiEtaisyys = etaisyys[node.getTunnus()] + 1;

          if (uusiEtaisyys < nykyinenEtaisyys) {
            etaisyys[naapuri.getTunnus()] = uusiEtaisyys;
            solmuMatriisi[naapuri.getY()][naapuri.getX()].setEtaisyys(uusiEtaisyys);
            naapuri.setEtaisyys(uusiEtaisyys);
            heap.addNode(naapuri);
          }
        }
      }
    }
  }

  public ArrayList<Node> getVisitedOrder() {
    return this.visitedOrder;
  }

  public int getEtaisyysMaaliin() {
    return this.etaisyysMaaliin;
  }


  /**
   * Piirtää karttamatriisiin lyhimmän reitin maalisolmusta aloitussolmuun, kun solmujen etäisyydet on ensin selvitetty Dijkstran algoritmilla.
   *
   * @return Maalisolmu, jos reitti alotussolmuun löytyy. Null, jos ei löydy.
   */
  public Node haeReitti() {
    Node currentNode = goalNode;
    while (currentNode.getEtaisyys() != 1) {
      Node naapuri = pieninNaapuri(currentNode);
      currentNode = naapuri;
      if (!naapuri.getStart()) {
        karttamatriisi[currentNode.getY()][currentNode.getX()] = 'X';
      }
    }
    return currentNode;
  }

  /**
   * Hakee parametrina annetulle solmulle naapurisolmun, jonka etäisyys aloitussolmuun on pienin.
   *
   * @param node Solmu, jolle etäisyydeltään pienin naapurisolmu haetaan.
   * @return Solmu, jonka etäisyys aloitussolmuun pienin.
   */
  public Node pieninNaapuri(Node node) {
    int minDist = Integer.MAX_VALUE;
    Node smallestDistNode = null;
    Node[] naapurit = verkko[node.getTunnus()];

    for (int i = 0; i < naapurit.length; i ++) {
      if (naapurit[i] != null) {
        if (naapurit[i].getStart()) {
          smallestDistNode = naapurit[i];
        } else if (naapurit[i].getEtaisyys() < minDist) {
          minDist = naapurit[i].getEtaisyys();
          smallestDistNode = naapurit[i];
        }
      }
    }

    return smallestDistNode;
  }

  /**
   * Palauttaa ratkaistun kartan, jossa näkyvät vieraillut solmut ja eräs lyhin reitti aloitussolmusta maalisolmuun. Dijkstran algoritmi tulee ajaan ensin.
   *
   * @return Karttamatriisi, johon merkattu käydyt solmut ja lyhin reitti.
   */
  public char[][] getSolvedMap() {
    haeReitti();
    return this.karttamatriisi;
  }


  public void printMap() {
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        io.printChar(karttamatriisi[i][j]);
      }
      io.printString("");
    }
  }

  /**
   * Muodostaa solmuista vieruslistan, eli hakee matriisin jokaiselle solmulle naapurisolmut.
   */

  public void initVerkko() {
    int solmut = karttamatriisi.length * karttamatriisi[0].length;
    this.verkko = new Node[solmut][];

    // Täytetään solmumatriisi solmuolioilla. Merkitään mikä on aloitussolmu ja mikä maalisolmu.
    int solmutunnus = 0;
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        Node node = new Node(solmutunnus, j, i);
        node.setEtaisyys(Integer.MAX_VALUE);
        if (karttamatriisi[i][j] == 'G') {
          node.setAsGoalNode();
        } else if (karttamatriisi[i][j] == 'S') {
          node.setAsStartNode();
          this.startingNode = node;
        }
        solmuMatriisi[i][j] = node;
        solmutunnus++;
      }
    }
    // Lisätään kullekin solmumatriisin solmulle lista naapurisolmuista
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        verkko[solmuMatriisi[i][j].getTunnus()] = haeNaapurisolmut(j, i);
      }
    }
  }

  public Node getGoalNode() {
    return this.goalNode;
  }

  /**
   * Alustaa solmujen etäisyystaulukon. Kunkin solmun etäisyydeksi tulee aluksi "ääretön".
   */
  public void initEtaisyydet() {
    for (int i = 0; i < etaisyys.length; i++) {
      etaisyys[i] = Integer.MAX_VALUE;
    }
    etaisyys[startingNode.getTunnus()] = 0;
  }

  /**
   * Metodi hakee matriisikartan solmulle kaikki naapurisolmut, eli solmut jotka eivät ole esteitä.
   * Tarkasteltavan solmun x- ja y-koordinaatit annettu parametreina.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @return lista naapurisolmuista
   */

  public Node[] haeNaapurisolmut(int currentX, int currentY) {
    Node[] naapurit = new Node[4];
    iNaapurilista = 0;

    if (karttamatriisi[currentY][currentX] != 'T') {
      // Vasen yläkulma
      if (currentX == 0 && currentY == 0) {
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
      // Oikea yläkulma
      else if (currentX == karttamatriisi[0].length - 1 && currentY == 0) {
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Vasen alakulma
      else if (currentX == 0 && currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
      // Oikea alakulma
      else if (currentX == karttamatriisi[0].length - 1 &&
          currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Vasen reuna
      else if (currentX == 0) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
      // Oikea reuna
      else if (currentX == karttamatriisi[0].length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Alareuna
      else if (currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
      // Yläreuna
      else if (currentY == 0) {
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
      // "Keskellä"
      else {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
    }
    return naapurit;
  }

  /**
   * Tarkistaa onko naapurisolmuja etelässä.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkSouth(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY + 1][currentX] != 'T') {
      naapurit[indeksi] = solmuMatriisi[currentY + 1][currentX];
      iNaapurilista ++;
    }
  }

  /**
   * Tarkistaa onko naapurisolmuja pohjoisessa.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkNorth(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY - 1][currentX] != 'T') {
      naapurit[indeksi] = solmuMatriisi[currentY - 1][currentX];
      iNaapurilista ++;
    }
  }

  /**
   * Tarkistaa onko naapurisolmuja idässä.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkEast(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY][currentX + 1] != 'T') {
      naapurit[indeksi] = solmuMatriisi[currentY][currentX + 1];
      iNaapurilista ++;
    }
  }

  /**
   * Tarkistaa onko naapurisolmuja lännessä.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkWest(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY][currentX - 1] != 'T') {
      naapurit[indeksi] = solmuMatriisi[currentY][currentX - 1];
      iNaapurilista ++;
    }
  }
}
