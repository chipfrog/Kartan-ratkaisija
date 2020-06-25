package shortest_path_visualizer.dataStructures;

/**
 * Luokka solmioliolle. Olio sisältää tiedon mm. sijainnista (x- ja y-koordinaatit), siitä onko solmussa vierailtu ja algoritmista riippuen erilaisia etäisyyksiä. Esim
 * etäisyys lähtösolmusta ja f-arvo.
 */
public class Node {
  /**
   * Solmuolion uniikki tunnus.
   */
  private int tunnus;
  /**
   * Dijkstran algoritmissa etäisyys maalisolmusta. A*:n ja JPS:n tapauksessa f-matka, eli g-matka (etäisyys lähtösolmusta) + h-matka (heuristinen etäisyys maalisolmuun).
   */
  private double etaisyys;
  private boolean vierailtu;
  private boolean start;
  private boolean goal;
  private int x;
  private int y;
  private Node parent;
  /**
   * Etäisyys lähtösolmusta kyseiseen solmuun
   */
  private double g_Matka;
  /**
   * Solmusta käsin tutkittava suunta x-akselilla JPS-algoritmissa. 0, 1, tai -1
   */
  private int dirH;
  /**
   * Solmusta käsin tutkittava suunta y-akselilla JPS-algoritmissa. 0, 1, tai -1
   */
  private int dirV;

  /** Solmuolion ensimmäinen konstruktori
   * @param tunnus solmun uniikki tunnus
   * @param xCoor solmun x-koordinaatti
   * @param yCoor solmun y-koordinaatti
   */
  public Node(int tunnus, int xCoor, int yCoor) {
    this.tunnus = tunnus;
    this.vierailtu = false;
    this.start = false;
    this.goal = false;
    this.x = xCoor;
    this.y = yCoor;
  }

  /** Solmuolion toinen konstruktori, käytetään JPS-algoritmin kanssa
   * @param x solmun x-koordinaatti
   * @param y solmun y-koordinaatti
   * @param dirH solmusta käsin tutkittava suunta x-akselilla, arvona 0, 1 tai -1
   * @param dirV solmusta käsin tutkittava suunta y-akselilla, arvona 0, 1, tai -1
   * @param distance etäisyys lähtösolmusta kyseiseen solmuun
   */
  public Node(int x, int y, int dirH, int dirV, double distance) {
    this.x = x;
    this.y = y;
    this.dirH = dirH;
    this.dirV = dirV;
    this.g_Matka = distance;
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
