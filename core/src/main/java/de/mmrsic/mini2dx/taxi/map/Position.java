package de.mmrsic.mini2dx.taxi.map;

/**
 * Position on a taxi map: x/y values on the so named axises.
 *
 * @author Miro
 */
public class Position {

  private final int x;
  private final int y;

  /**
   * Create a new position for given x and y values with (0,0) being the upper left corner of the map.
   *
   * @param x value >=0, position on the x axis
   * @param y value >=0, position on the y axis
   */
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(getX()).append("/").append(getY()).toString();
  }

  /**
   * Get the horizontal coordinate of this position.
   *
   * @return the value specified at creation time of this instance
   */
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

}
