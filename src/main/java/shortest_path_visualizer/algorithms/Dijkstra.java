package shortest_path_visualizer.algorithms;

import java.util.ArrayList;
import shortest_path_visualizer.IO.IO;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.utils.Node;

/**
 * Luokka, jossa toteutetaan Dijkstran algoritmi.
 */

public class Dijkstra {
  private final IO io;
  private final char[][] karttamatriisi;
  private final Node[][] solmuMatriisi;
  private int[] etaisyys;
  private Node [][] verkko;
  private Node startingNode;
  private Node goalNode;
  private ArrayList<Node> visitedOrder;
  private int etaisyysMaaliin;
  private Keko heap;
  private NeighbourFinder finder;

  public Dijkstra (IO io, char[][] karttamatriisi) {
    this.io = io;
    this.karttamatriisi = karttamatriisi;
    this.solmuMatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.etaisyys = new int[karttamatriisi.length * karttamatriisi[0].length];
    this.visitedOrder = new ArrayList<>();
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.goalNode = null;
    this.heap = new Keko();
    this.finder = new NeighbourFinder(karttamatriisi, solmuMatriisi);
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
      if (node.isGoal()) {
        this.goalNode = node;
        this.etaisyysMaaliin = node.getEtaisyys();
        break;
      }
      if (!node.isStart()) {
        karttamatriisi[node.getY()][node.getX()] = 'O';
      }

      for (Node naapuri : haeNaapurisolmut(node.getX(), node.getY())) {
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
      if (!naapuri.isStart()) {
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
    Node[] naapurit = haeNaapurisolmut(node.getX(), node.getY());

    for (int i = 0; i < naapurit.length; i ++) {
      if (naapurit[i] != null) {
        if (naapurit[i].isStart()) {
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
    /*// Lisätään kullekin solmumatriisin solmulle lista naapurisolmuista
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        verkko[solmuMatriisi[i][j].getTunnus()] = haeNaapurisolmut(j, i);
      }
    }*/
  }

  public Node[] haeNaapurisolmut(int x, int y) {
    return finder.haeNaapurisolmut(x, y);
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
}
