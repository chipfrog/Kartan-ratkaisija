package shortest_path_visualizer.utils;

/**
 * Luokkaan on koottu omia toteutuksia matemaattisista metodeista, kuten itseisarvon hakeminen.
 */
public class MathFunctions {
  private double num;

  public MathFunctions() {

  }

  /** Antaa parametrina annetulle luvulle itseisarvon.
   * @param num Luku, jonka itseisarvo halutaan.
   * @return Luvun itseisarvo
   */
  public double getAbs(double num) {
    this.num = num;
    if (num < 0) {
      return num * (-1);
    }
    return num;
  }

  /** Kertoo kumpi parametrina annetuista luvuista on pienempi ja palauttaa sen.
   * @param num1 Ensimmäinen vertailtava luku
   * @param num2 Toinen vertailtava luku
   * @return Luvuista pienempi
   */
  public double getMin(double num1, double num2) {
    if (num1 <= num2) {
      return num1;
    }
    return num2;
  }
}
