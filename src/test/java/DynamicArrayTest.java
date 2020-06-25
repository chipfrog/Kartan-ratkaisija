import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import shortest_path_visualizer.dataStructures.DynamicArray;
import shortest_path_visualizer.dataStructures.Node;

public class DynamicArrayTest {
  DynamicArray dynamicArray;

  @Before
  public void init() {
    this.dynamicArray = new DynamicArray();
  }

  @Test
  public void sizeIsZeroWhenNothingHasBeenAdded() {
    assertTrue(dynamicArray.size() == 0);
  }

  @Test
  public void sizeIncreasesWhenNodesAreAdded() {
    Node n1 = new Node(1,2,3);
    Node n2 = new Node(1, 2, 3);
    Node n3 = new Node(1, 2, 3);
    dynamicArray.add(n1);
    assertTrue(dynamicArray.size() == 1);
    dynamicArray.add(n2);
    assertTrue(dynamicArray.size() == 2);
    dynamicArray.add(n3);
    assertTrue(dynamicArray.size() == 3);
  }

  @Test
  public void returnsRightItem() {
    Node n1 = new Node(1,2,3);
    Node n2 = new Node(1, 2, 3);
    Node n3 = new Node(1, 2, 3);
    dynamicArray.add(n1);
    dynamicArray.add(n2);
    dynamicArray.add(n3);
    assertTrue(dynamicArray.get(1) == n2);
  }

  @Test
  public void sizeIncreasesWhenInitialSizeGetsTooSmall() {
    assertTrue(dynamicArray.getAvailableSize() == 10);
    for (int i = 0; i < 15; i ++) {
      Node node = new Node(1, 2, 3);
      dynamicArray.add(node);
    }
    assertTrue(dynamicArray.getAvailableSize() == 20);
  }
}
