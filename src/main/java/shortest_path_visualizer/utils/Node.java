package shortest_path_visualizer.utils;

public class Node {
  private int tunnus;
  private int etaisyys;
  private boolean vierailtu;
  private boolean start;
  private boolean goal;
  private int xCoor;
  private int yCoor;
  private Node parent;
  private int g_Matka;

  public Node(int tunnus, int xCoor, int yCoor) {
    this.tunnus = tunnus;
    this.vierailtu = false;
    this.start = false;
    this.goal = false;
    this.xCoor = xCoor;
    this.yCoor = yCoor;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public Node getParent() {
    return parent;
  }

  public void setG_Matka(int g) {
    this.g_Matka = g;
  }

  public int getG_Matka() {
    return this.g_Matka;
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

  public boolean isStart() {
    return this.start;
  }

  public boolean isGoal() {
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
}
