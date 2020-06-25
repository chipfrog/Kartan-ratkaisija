package shortest_path_visualizer.dataStructures;

/**
 * Kokoaan dynaamisesti kasvattava taulukko.
 */
public class DynamicArray {
  private Node[] array;
  private int index;
  private int size;

  public DynamicArray() {
    this.array = new Node[10];
    this.index = -1;
    this.size = 10;
  }

  /** Lisää uuden Noden taulukkoon ja tuplaa taulukon koon, jos lisäyksen jälkeen taulukko on täysi.
   * @param node Taulukkoon lisättävä Node
   */
  public void add(Node node) {
    index ++;
    if (index == size) {
      increaseSize();
    }
    array[index] = node;
  }

  public void addToIndex(int i, Node node) {
    if (index == size) {
      increaseSize();
    }
  }

  /** Palauttaa taulukon nykyisen koon. Koko = viimeksi lisätyn Noden indeksi + 1
   * @return taulukon koko
   */
  public int size() {
    if (index == -1) {
      return 0;
    }
    return index + 1;
  }

  /** Hakee taulukosta Noden parametrina annetulla indeksillä.
   * @param i Haettavan Noden indeksi
   * @return taulukossa indeksillä i oleva Node tai null
   */
  public Node get(int i) {
    if (i <= index) {
      return array[i];
    }
    return null;
  }

  /**
   * Tuplaa taulukon koon.
   */
  public void increaseSize() {
    Node[] temp = null;
    temp = new Node[size * 2];
    for (int i = 0; i < size; i ++) {
      temp[i] = array[i];
    }
    array = temp;
    size *= 2;
  }

  public int getAvailableSize() {
    return this.size;
  }

}
