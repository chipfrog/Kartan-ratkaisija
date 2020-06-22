import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import shortest_path_visualizer.dataStructures.Keko;
import shortest_path_visualizer.utils.Node;

public class KekoTest {
  Keko keko;

  @Before
  public void initKeko() {
    this.keko = new Keko();
  }

  @Test
  public void kekoIsEmptyWhenNoNodesHaveBeenAdded() {
    assertTrue(keko.isEmpty());
  }

  @Test
  public void kekoIsNotEmptyWhenNodeHasBeenAdded() {
    Node n1 = new Node(1, 2,3);
    keko.addNode(n1);
    assertTrue(!keko.isEmpty());
  }

  @Test
  public void kekoGetsEmptyWhenLastNodeisPolled() {
    Node n1 = new Node(1,2,3);
    keko.addNode(n1);
    keko.pollNode();
    assertTrue(keko.isEmpty());
  }

  @Test
  public void kekoPutsNodeWithLowestCostToRoot() {
    Node n1 = new Node(1,2,3);
    n1.setEtaisyys(10);
    Node n2 = new Node(2,2,2);
    n2.setEtaisyys(2);
    Node n3 = new Node(3, 3,3);
    n3.setEtaisyys(15);
    keko.addNode(n1);
    keko.addNode(n2);
    keko.addNode(n3);
    Node n = keko.pollNode();
    assertTrue(n.getEtaisyys() == 2);
  }

  @Test
  public void kekoChangesRootWhenNodeIsPolled() {
    Node n1 = new Node(1, 2, 2);
    n1.setEtaisyys(1);
    Node n2 = new Node(2, 2, 2);
    n2.setEtaisyys(10);
    Node n3 = new Node(3, 3,3);
    n3.setEtaisyys(5);
    keko.addNode(n1);
    keko.addNode(n2);
    keko.addNode(n3);
    keko.pollNode();
    assertTrue(keko.pollNode().getTunnus() == 3);
  }
}
