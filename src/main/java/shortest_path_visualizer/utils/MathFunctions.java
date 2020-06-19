package shortest_path_visualizer.utils;

public class MathFunctions {
  private double num;

  public MathFunctions() {

  }

  public double getAbs(double num) {
    this.num = num;
    if (num < 0) {
      return num * (-1);
    }
    return num;
  }

  public double getMin(double num1, double num2) {
    if (num1 <= num2) {
      return num1;
    }
    return num2;
  }


}
