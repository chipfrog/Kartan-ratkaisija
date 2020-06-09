package shortest_path_visualizer.algorithms;


import java.util.ArrayList;
import shortest_path_visualizer.IO.IO;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.Node;

public class AStar {
  private final IO io;
  private final char[][] karttamatriisi;
  private final Node[][] solmumatriisi;
  private Keko openList;
  private int iNaapurilista;
  private Node[][] verkko;
  private boolean[][] addedToOpenList;
  private Node startingNode;
  private Node goalNode;
  private int etaisyysMaaliin;
  private ArrayList<Node> visitedOrder;
  private Node takaisin;
  private boolean goalFound;

  public AStar(IO io, char[][] karttamatriisi) {
    this.io = io;
    this.karttamatriisi = karttamatriisi;
    this.solmumatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.addedToOpenList = new boolean[karttamatriisi.length][karttamatriisi[0].length];
    this.iNaapurilista = 0;
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.visitedOrder = new ArrayList<>();
    this.goalFound = false;
  }

  /**
   * Suorittaa A*-algoritmin.
   */
  public void runAStar() {
    initVerkko();
    this.openList = new Keko();
    startingNode.setG_Matka(0);
    startingNode.setEtaisyys(manhattanDist(startingNode, goalNode));
    openList.addNode(startingNode);

    while (!openList.isEmpty()) {
      Node current = openList.pollNode();
      addedToOpenList[current.getY()][current.getX()] = false;
      if (current.isGoal()) {
        System.out.println("Maali löytyi!");
        goalFound = true;
        etaisyysMaaliin = current.getG_Matka();
        break;
      }
      for (Node naapuri : verkko[current.getTunnus()]) {
        if (naapuri != null) {
          int uusiGMatka = current.getG_Matka() + 1;
          if (uusiGMatka < naapuri.getG_Matka()) {
            naapuri.setParent(current);
            naapuri.setG_Matka(uusiGMatka);
            naapuri.setEtaisyys(uusiGMatka + manhattanDist(naapuri, goalNode));

            if (!addedToOpenList[naapuri.getY()][naapuri.getX()]) {
              openList.addNode(naapuri);
              addedToOpenList[naapuri.getY()][naapuri.getX()] = true;
              visitedOrder.add(naapuri);
            }
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

  public int getEtaisyysMaaliin() {
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
    // Lisätään kullekin solmumatriisin solmulle lista naapurisolmuista
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        verkko[solmumatriisi[i][j].getTunnus()] = haeNaapurisolmut(j, i);
      }
    }
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
      naapurit[indeksi] = solmumatriisi[currentY + 1][currentX];
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
      naapurit[indeksi] = solmumatriisi[currentY - 1][currentX];
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
      naapurit[indeksi] = solmumatriisi[currentY][currentX + 1];
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
      naapurit[indeksi] = solmumatriisi[currentY][currentX - 1];
      iNaapurilista ++;
    }
  }

}
