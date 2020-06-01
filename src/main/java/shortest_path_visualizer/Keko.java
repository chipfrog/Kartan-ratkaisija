package shortest_path_visualizer;

public class Keko {
  private Node[] keko;
  private Node pienin;
  private int sijainti;
  private int juuri = 1;

  public Keko() {
    this.keko = new Node[1000];
    this.pienin = new Node(0, -1, -1);
    pienin.setEtaisyys(Integer.MIN_VALUE);
    keko[0] = pienin;
    this.sijainti = 1;
  }

  public boolean isEmpty() {
    return sijainti == 1;
  }

  public Node pollNode() {
    Node nodeToReturn = keko[juuri];
    keko[juuri] = keko[sijainti - 1];
    findNewRootNode(juuri);
    keko[sijainti - 1] = null;
    sijainti --;
    return nodeToReturn;
  }

  private void findNewRootNode(int currentPos) {
    if (!leaf(currentPos)) {
      if (keko[currentPos].getEtaisyys() > keko[leftChild(currentPos)].getEtaisyys()
          || keko[currentPos].getEtaisyys() > keko[rightChild(currentPos)].getEtaisyys()) {

        if (keko[leftChild(currentPos)].getEtaisyys() < keko[rightChild(currentPos)].getEtaisyys()) {
          swapPlaces(currentPos, leftChild(currentPos));
          findNewRootNode(leftChild(currentPos));
        }
        else {
          swapPlaces(currentPos, rightChild(currentPos));
          findNewRootNode(rightChild(currentPos));
        }
      }
    }
  }

  private boolean leaf(int pos) {
    return (pos >= sijainti / 2 && pos <= sijainti);
  }

  private int leftChild(int pos) {
    return 2 * pos;
  }

  private int rightChild(int pos) {
    return 2 * pos + 1;
  }

  private void swapPlaces(int pos1, int pos2) {
    Node temp = keko[pos1];
    keko[pos1] = keko[pos2];
    keko[pos2] = temp;
  }

  public void addNode(Node node) {
    keko[sijainti] = node;
    int currentPos = sijainti;
    if (keko[currentPos / 2] != null) {
      while(keko[currentPos / 2].getEtaisyys() > node.getEtaisyys()) {
        swapPlaces(currentPos / 2, currentPos);
        currentPos = currentPos / 2;
      }
    }
    sijainti ++;
  }
}
