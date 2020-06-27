package shortest_path_visualizer.dataStructures;

public class Keko {
  private Node[] keko;
  private Node pienin;
  private int sijainti;
  private int juuri = 1;

  /**
   * Minimikeon toteutus. Pitää yllä hakupuuta, jonka juurena solmu,
   * jonka etäisyys Dijkstran tapauksessa maalisolmuun pienin. A*:n ja JPS:n tapauksessa
   * juuressa solmu jonka f-arvo pienin.
   */
  public Keko() {
    this.keko = new Node[1000000];
    this.pienin = new Node(0, -1, -1);
    pienin.setEtaisyys(Integer.MIN_VALUE);
    keko[0] = pienin;
    this.sijainti = 1;
  }

  /**
   * Kertoo onko keko tyhjä. Jos tyhjä, sijainnin arvo 1. Tämä siksi, että indeksissä 0
   * on pienin mahdollinen arvo, ei siis minkään todellisen solmun etäisyys.
   *
   * @return Onko puu tyhjä vai ei
   */
  public boolean isEmpty() {
    return sijainti == 1;
  }

  /**
   * Hakee ja poistaa puun juuressa olevan solmun.
   *
   * @return Solmu, jonka etäisyys maalisolmuun pienin.
   */
  public Node pollNode() {
    Node nodeToReturn = keko[juuri];
    keko[juuri] = keko[sijainti - 1];
    findNewRootNode(juuri);
    keko[sijainti - 1] = null;
    sijainti--;
    return nodeToReturn;
  }

  /**
   * Päivittää kekoa ja etsii sille rekursiivisesti uuden juurisolmun.
   *
   * @param currentPos Nykyinen sijainti puussa.
   */
  private void findNewRootNode(int currentPos) {
    if (!leaf(currentPos)) {
      if (keko[currentPos].getEtaisyys() > keko[leftChild(currentPos)].getEtaisyys()
          || keko[currentPos].getEtaisyys() > keko[rightChild(currentPos)].getEtaisyys()) {

        if (keko[leftChild(currentPos)].getEtaisyys()
            < keko[rightChild(currentPos)].getEtaisyys()) {
          swapPlaces(currentPos, leftChild(currentPos));
          findNewRootNode(leftChild(currentPos));
        } else {
          swapPlaces(currentPos, rightChild(currentPos));
          findNewRootNode(rightChild(currentPos));
        }
      }
    }
  }

  /**
   * Kertoo onko tutkittava solmu puun lehti vai ei.
   *
   * @param pos Sijainti puussa
   * @return Lehti vai ei
   */
  private boolean leaf(int pos) {
    return (pos >= sijainti / 2 && pos <= sijainti);
  }

  private int leftChild(int pos) {
    return 2 * pos;
  }

  private int rightChild(int pos) {
    return 2 * pos + 1;
  }

  /**
   * Vaihtaa parametreina annettavien solmujen paikkaa puussa.
   *
   * @param pos1 Paikkaa vaihtava solmu 1
   * @param pos2 Paikkaa vaihtava solmu 2
   */
  private void swapPlaces(int pos1, int pos2) {
    Node temp = keko[pos1];
    keko[pos1] = keko[pos2];
    keko[pos2] = temp;
  }

  /**
   * Lisää puuhun uuden solmun ja sijoittaa sen oikealle paikalle.
   *
   * @param node Puuhun lisättävä solmu
   */
  public void addNode(Node node) {
    keko[sijainti] = node;
    int currentPos = sijainti;
    if (keko[currentPos / 2] != null) {
      while (keko[currentPos / 2].getEtaisyys() > node.getEtaisyys()) {
        swapPlaces(currentPos / 2, currentPos);
        currentPos = currentPos / 2;
      }
    }
    sijainti++;
  }
}
