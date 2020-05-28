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
  private ArrayList<Node>[] verkko;
  private Node startingNode;
  private Node goalNode;
  private ArrayList<Node> visitedOrder;
  private int etaisyysMaaliin;

  public Dijkstra(IO io, char[][] karttamatriisi) {
    this.io = io;
    this.karttamatriisi = karttamatriisi;
    this.solmuMatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.etaisyys = new int[karttamatriisi.length * karttamatriisi[0].length];
    this.keko = new PriorityQueue<>();
    this.visitedOrder = new ArrayList<>();
    this.etaisyysMaaliin = Integer.MAX_VALUE;
  }

  public void runDijkstra() {
    initEtaisyydet(startingNode);
    keko.add(startingNode);

    while (!keko.isEmpty()) {
      Node node = keko.poll();
      if (node.onVierailtu()) {
        continue;
      }
      node.vieraile();
      visitedOrder.add(node);
      if (node.getGoal()) {
        this.goalNode = node;
        this.etaisyysMaaliin = node.getEtaisyys();
        System.out.println("Etäisyys maaliin: " + node.getEtaisyys());
        break;
      }
      if (!node.getStart()) {
        karttamatriisi[node.getY()][node.getX()] = 'O';
      }

      for (Node naapuri : verkko[node.getTunnus()]) {
        int nykyinenEtaisyys = etaisyys[naapuri.getTunnus()];
        int uusiEtaisyys = etaisyys[node.getTunnus()] + 1;

        if (uusiEtaisyys < nykyinenEtaisyys) {
          etaisyys[naapuri.getTunnus()] = uusiEtaisyys;
          solmuMatriisi[naapuri.getY()][naapuri.getX()].setEtaisyys(uusiEtaisyys);
          naapuri.setEtaisyys(uusiEtaisyys);
          keko.add(naapuri);
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

  public void haeReitti() {
    Node currentNode = goalNode;
    while (currentNode.getEtaisyys() != 1) {
      Node naapuri = pieninNaapuri(currentNode);
      currentNode = naapuri;
      if (!naapuri.getStart()) {
        karttamatriisi[currentNode.getY()][currentNode.getX()] = 'X';
      }
    }
  }

  public Node pieninNaapuri(Node node) {
    int minDist = Integer.MAX_VALUE;
    Node smallestDistNode = null;
    ArrayList<Node> naapurit = verkko[node.getTunnus()];

    for (int i = 0; i < naapurit.size(); i ++) {
      if (naapurit.get(i).getStart()) {
        smallestDistNode = naapurit.get(i);
      }
      else if (naapurit.get(i).getEtaisyys() < minDist) {
        minDist = naapurit.get(i).getEtaisyys();
        smallestDistNode = naapurit.get(i);
      }

    }
    return smallestDistNode;
  }

  public char[][] getSolvedMap() {
    haeReitti();
    return this.karttamatriisi;
  }


  public void printMap() {
    for (int i = 0; i < karttamatriisi.length; i ++) {
      for (int j = 0; j < karttamatriisi[0].length; j ++) {
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
    this.verkko = new ArrayList[solmut + 1];

    int solmunumero = 0;
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        Node node = new Node(solmunumero, j, i);
        node.setEtaisyys(Integer.MAX_VALUE);
        if (karttamatriisi[i][j] == 'G') {
          node.setAsGoalNode();
        }
        else if (karttamatriisi[i][j] == 'S') {
          node.setAsStartNode();
          this.startingNode = node;
        }
        solmuMatriisi[i][j] = node;
        solmunumero++;
      }
    }
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        verkko[solmuMatriisi[i][j].getTunnus()] = haeNaapurisolmut(j, i);
      }
    }
  }

  public void initEtaisyydet(Node startingNode) {
    for(int i = 0; i < etaisyys.length; i ++) {
      etaisyys[i] = Integer.MAX_VALUE;
    }
    etaisyys[startingNode.getTunnus()] = 0;
  }

  /**
   * Metodi hakee matriisikartan solmulle kaikki naapurisolmut, eli solmut jotka eivät ole esteitä.
   * Tarkasteltavan solmun x- ja y-koordinaatit annettu parametreina.
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @return lista naapurisolmuista
   */

  public ArrayList<Node> haeNaapurisolmut(int currentX, int currentY) {
    ArrayList<Node> naapurit = new ArrayList<>();

    if (karttamatriisi[currentY][currentX] != 'T') {
      // Vasen yläkulma
      if (currentX == 0 && currentY == 0) {
        checkSouth(currentX, currentY, naapurit);
        checkEast(currentX, currentY, naapurit);
      }
      // Oikea yläkulma
      else if (currentX == karttamatriisi[0].length - 1 && currentY == 0) {
        checkSouth(currentX, currentY, naapurit);
        checkWest(currentX, currentY, naapurit);
      }
      // Vasen alakulma
      else if (currentX == 0 && currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit);
        checkEast(currentX, currentY, naapurit);
      }
      // Oikea alakulma
      else if (currentX == karttamatriisi[0].length - 1 &&
          currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit);
        checkWest(currentX, currentY, naapurit);
      }
      // Vasen reuna
      else if (currentX == 0) {
        checkNorth(currentX, currentY, naapurit);
        checkSouth(currentX, currentY, naapurit);
        checkEast(currentX, currentY, naapurit);
      }
      // Oikea reuna
      else if (currentX == karttamatriisi[0].length - 1) {
        checkNorth(currentX, currentY, naapurit);
        checkSouth(currentX, currentY, naapurit);
        checkWest(currentX, currentY, naapurit);
      }
      // Alareuna
      else if (currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit);
        checkWest(currentX, currentY, naapurit);
        checkEast(currentX, currentY, naapurit);
      }
      // Yläreuna
      else if (currentY == 0) {
        checkSouth(currentX, currentY, naapurit);
        checkWest(currentX, currentY, naapurit);
        checkEast(currentX, currentY, naapurit);
      }
      // "Keskellä"
      else {
        checkNorth(currentX, currentY, naapurit);
        checkSouth(currentX, currentY, naapurit);
        checkWest(currentX, currentY, naapurit);
        checkEast(currentX, currentY, naapurit);
      }
    }
    return naapurit;
  }

  /**
   * Tarkistaa onko naapurisolmuja etelässä.
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkSouth(int currentX, int currentY, ArrayList<Node> naapurit) {
    if (karttamatriisi[currentY + 1][currentX] != 'T') {
      naapurit.add(solmuMatriisi[currentY + 1][currentX]);
    }
  }
  /**
   * Tarkistaa onko naapurisolmuja pohjoisessa.
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkNorth(int currentX, int currentY, ArrayList<Node> naapurit) {
    if (karttamatriisi[currentY - 1][currentX] != 'T') {
      naapurit.add(solmuMatriisi[currentY - 1][currentX]);
    }
  }
  /**
   * Tarkistaa onko naapurisolmuja idässä.
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkEast(int currentX, int currentY, ArrayList<Node> naapurit) {
    if (karttamatriisi[currentY][currentX + 1] != 'T') {
      naapurit.add(solmuMatriisi[currentY][currentX + 1]);
    }
  }
  /**
   * Tarkistaa onko naapurisolmuja lännessä.
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkWest(int currentX, int currentY, ArrayList<Node> naapurit) {
    if (karttamatriisi[currentY][currentX - 1] != 'T') {
      naapurit.add(solmuMatriisi[currentY][currentX - 1]);
    }
  }
}
