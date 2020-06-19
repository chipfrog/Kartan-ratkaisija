package shortest_path_visualizer.dataStructures;

import shortest_path_visualizer.utils.Node;

public class DynamicArray {
  private Node[] array;
  private int index;
  private int size;

  public DynamicArray() {
    this.array = new Node[10];
    this.index = 0;
    this.size = 10;
  }

  public void add(Node node) {
    if (index == size) {
      increaseSize();
    }
    array[index] = node;
    index ++;
  }

  public void addToIndex(int i, Node node) {
    if (index == size) {
      increaseSize();
    }
  }

  public int size() {
    return this.index;
  }

  public Node get(int i) {
    if (i <= index) {
      return array[i];
    }
    return null;
  }

  public void increaseSize() {
    Node[] temp = null;
    temp = new Node[size * 2];
    for (int i = 0; i < size; i ++) {
      temp[i] = array[i];
    }
    array = temp;
    size *= 2;
  }

}
