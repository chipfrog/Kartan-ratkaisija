package shortest_path_visualizer.utils;

public class Node {
  private int tunnus;
  private double etaisyys;
  private boolean vierailtu;
  private boolean start;
  private boolean goal;
  private int x;
  private int y;
  private Node parent;
  private double g_Matka;
  private int dirH;
  private int dirV;

  public Node(int tunnus, int xCoor, int yCoor) {
    this.tunnus = tunnus;
    this.vierailtu = false;
    this.start = false;
    this.goal = false;
    this.x = xCoor;
    this.y = yCoor;
  }

  public Node(int x, int y, int dirH, int dirV, double distance) {

  }

  public void setDirH(int dirH) {
    this.dirH = dirH;
  }

  public void setDirV(int dirV) {
    this.dirV = dirV;
  }

  public int getDirV() {
    return this.dirV;
  }

  public int getDirH() {
    return this.dirH;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public Node getParent() {
    return parent;
  }

  public void setG_Matka(double g) {
    this.g_Matka = g;
  }

  public double getG_Matka() {
    return this.g_Matka;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
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

  public void setEtaisyys(double uusiEtaisyys) {
    this.etaisyys = uusiEtaisyys;
  }

  public double getEtaisyys() {
    return this.etaisyys;
  }
}
