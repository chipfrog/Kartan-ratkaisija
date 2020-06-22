import static org.junit.Assert.assertTrue;

import org.junit.Test;
import shortest_path_visualizer.utils.MathFunctions;

public class MathFunctionsTest {
  MathFunctions mf = new MathFunctions();

  @Test
  public void absoluteValuePositiveWhenGivenNumberNegative() {
    assertTrue(mf.getAbs(-2) == 2);
  }

  @Test
  public void absoluteValuePositiveWhenGivenValuePositive() {
    assertTrue(mf.getAbs(10) == 10);
  }

  @Test
  public void getMinReturnSmallerNumber() {
    assertTrue(mf.getMin(2, 5) == 2);
  }

  @Test
  public void getMinReturnNegativeValueWhenOtherValueIsPositive() {
    assertTrue(mf.getMin(-5, 6) == -5);
  }

  @Test
  public void getMinReturnSmallerNegativeValie() {
    assertTrue(mf.getMin(-2, -10) == -10);
  }
}
