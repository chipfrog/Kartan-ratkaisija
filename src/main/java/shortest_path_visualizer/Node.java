package shortest_path_visualizer;

public class Node implements Comparable<Node> {
  private int tunnus;
  private int etaisyys;
  private boolean vierailtu;
  private boolean start;
  private boolean goal;
  private int xCoor;
  private int yCoor;

  public Node(int tunnus, int xCoor, int yCoor) {
    this.tunnus = tunnus;
    this.vierailtu = false;
    this.start = false;
    this.goal = false;
    this.xCoor = xCoor;
    this.yCoor = yCoor;

  }

  public int getX() {
    return this.xCoor;
  }

  public int getY() {
    return this.yCoor;
  }

  public void setAsStartNode() {
    this.start = true;
  }

  public void setAsGoalNode() {
    this.goal = true;
  }

  public boolean getStart() {
    return this.start;
  }

  public boolean getGoal() {
    return this.goal;
  }

  public void vieraile() {
    this.vierailtu = true;
  }

  public boolean onVierailtu() {
    return vierailtu;
  }

  public int getTunnus() {
    return this.tunnus;
  }

  public void setEtaisyys(int uusiEtaisyys) {
    this.etaisyys = uusiEtaisyys;
  }

  public int getEtaisyys() {
    return this.etaisyys;
  }

  @Override
  public int compareTo(Node n) {
    return this.etaisyys - n.etaisyys;
  }
}
