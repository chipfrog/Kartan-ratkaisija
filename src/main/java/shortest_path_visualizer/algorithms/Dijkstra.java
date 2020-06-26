package shortest_path_visualizer.algorithms;

import shortest_path_visualizer.IO.IO;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.NeighbourFinder;
import shortest_path_visualizer.dataStructures.Node;

/**
 * Luokka, jossa toteutetaan Dijkstran algoritmi.
 */

public class Dijkstra {
  /**
   * Algoritmin käyttämä kartta ascii-merkkeinä matriisimuodossa.
   */
  private char[][] karttamatriisi;
  /**
   * Sama kartta, mutta koostuu Node-olioista. Tarvitaan NeighbourFinderia varten. Kun solmumatriisin avulla
   * haetaan naapurisolmuja, päästään käsiksi juuri haluttuun Node-olioon yksilöllisen tunnuksen avulla.
   */
  private Node[][] solmuMatriisi;
  /**
   * Kunkin solmun etäisyys lähtöpisteestä. Indeksinä toimii solmun uniikki tunnus.
   */
  private double[] etaisyys;
  private Node startingNode;
  private Node goalNode;
  private double etaisyysMaaliin;
  /**
   * Minimikeko, josta nostetaan aina Node, jonka etäisyys lähtösolmusta pienin.
   */
  private Keko keko;
  private NeighbourFinder finder;
  private DynamicArray visitedNodes;

  public Dijkstra () {
  }

  /** Alustaa Dijkstran tarvitsemat rakenteet, kuten keon ja muuttujat. Käytetään myös kartan vaihtamiseen.
   * @param kartta algoritmille selvitettäväksi annettava kartta
   */
  public void setMap(char[][] kartta) {
    this.karttamatriisi = kartta;
    this.solmuMatriisi = new Node[karttamatriisi.length][karttamatriisi[0].length];
    this.etaisyys = new double[karttamatriisi.length * karttamatriisi[0].length];
    this.etaisyysMaaliin = Integer.MAX_VALUE;
    this.goalNode = null;
    this.keko = new Keko();
    this.finder = new NeighbourFinder(karttamatriisi, solmuMatriisi);
    this.visitedNodes = new DynamicArray();
    initSolmumatriisi();
    initEtaisyydet();
  }

  /**
   * Suorittaa Dijkstran algoritmin. Aloitussolmusta lähtien alkaa tutkia solmujen naapurisolmuja. Naapurisolmut lisätään
   * aina kekoon, jos etäisyys naapurisolmuun nykyisen solmun (current) kautta on lyhyempi kuin aiempi etäisyys. Keosta nostetaan
   * aina uusi solmu, jonka etäisyys (g-matka) lähtösolmusta pienin ja tämän naapurisolmut käydään läpi. Jos solmussa on jo vierailtu,
   * sitä ei käsitellä uudestaan. Metodi jatkuu kunnes keko on tyhjä tai maali on löytynyt.
   */
  public void runDijkstra() {
    keko.addNode(startingNode);

    while(!keko.isEmpty()) {
      Node node = keko.pollNode();
      if (node.onVierailtu()) {
        continue;
      }
      node.vieraile();
      visitedNodes.add(node);
      if (node.isGoal()) {
        this.goalNode = node;
        this.etaisyysMaaliin = node.getEtaisyys();
        break;
      }
      for (Node naapuri : haeNaapurisolmut(node.getX(), node.getY())) {
        if (naapuri != null) {
          double nykyinenEtaisyys = etaisyys[naapuri.getTunnus()];
          double uusiEtaisyys = etaisyys[node.getTunnus()] + neighbourDist(node, naapuri);

          if (uusiEtaisyys < nykyinenEtaisyys) {
            etaisyys[naapuri.getTunnus()] = uusiEtaisyys;
            solmuMatriisi[naapuri.getY()][naapuri.getX()].setEtaisyys(uusiEtaisyys);
            naapuri.setEtaisyys(uusiEtaisyys);
            keko.addNode(naapuri);
          }
        }
      }
    }
  }

  /** Kertoo etäisyyden kahden vierekkäisen solmun välillä. Jos solmut samalla x- tai samalla y-akselilla,
   * etäisyys 1, muuten sqrt(2).
   * @param n1 Ensimmäinen solmu
   * @param n2 Toinen solmu
   * @return Etäisyys solmujen n1 ja n2 välillä
   */
  public double neighbourDist(Node n1, Node n2) {
    if (n1.getX() == n2.getX() || n1.getY() == n2.getY()) {
      return 1;
    }
    return Math.sqrt(2);
  }

  public DynamicArray getVisitedOrder() {
    return this.visitedNodes;
  }

  public double getEtaisyysMaaliin() {
    return this.etaisyysMaaliin;
  }


  /**
   * Piirtää karttamatriisiin lyhimmän reitin maalisolmusta aloitussolmuun, kun solmujen etäisyydet on ensin selvitetty Dijkstran algoritmilla.
   * Lyhimmän reitin varrella karttamatriisissa korvataan siis pisteet '.' 'X'-kirjaimella. Ui:ssa X:ien perusteella piirretään visuaalinen reitti.
   * @return Maalisolmu, jos reitti alotussolmuun löytyy. Null, jos ei löydy.
   */
  public Node haeReitti() {
    Node currentNode = goalNode;
    while (!currentNode.isStart()) {
      Node naapuri = pieninNaapuri(currentNode);
      currentNode = naapuri;
      if (currentNode.getEtaisyys() == 1) {
        karttamatriisi[currentNode.getY()][currentNode.getX()] = 'X';
        break;
      }
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
    double minDist = Integer.MAX_VALUE;
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

  /**
   * Käy karttamatriisin läpi ja luo jokaiselle koordinaatiston pisteelle oman uniikin Node-olion solmumatriisiin.
   * Tallentaa samalla muistiin start- ja goal-nodet.
   */

  public void initSolmumatriisi() {
    int solmutunnus = 0;
    for (int i = 0; i < karttamatriisi.length; i++) {
      for (int j = 0; j < karttamatriisi[0].length; j++) {
        Node node = new Node(solmutunnus, j, i);
        if (karttamatriisi[i][j] == '@') {
          node = null;
        }
        else {
          node.setEtaisyys(Integer.MAX_VALUE);

          if (karttamatriisi[i][j] == 'G') {
            node.setAsGoalNode();
          } else if (karttamatriisi[i][j] == 'S') {
            node.setAsStartNode();
            node.setEtaisyys(0);
            this.startingNode = node;
          }
        }
        solmuMatriisi[i][j] = node;
        solmutunnus++;

      }
    }
  }

  public Node[][] getSolmuMatriisi() {
    return this.solmuMatriisi;
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
