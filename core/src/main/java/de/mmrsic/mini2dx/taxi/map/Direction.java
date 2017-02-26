package de.mmrsic.mini2dx.taxi.map;

/**
 * A direction on the taxi city map: One of four possible values.
 *
 * @author Miro
 */
public enum Direction {
  UP, DOWN, LEFT, RIGHT;

  public Direction turnedRight() {
    switch (this) {
      case UP:
        return RIGHT;
      case RIGHT:
        return DOWN;
      case DOWN:
        return LEFT;
      case LEFT:
        return UP;
      default:
        throw new IllegalArgumentException("Undefined how to turn right for " + this);
    }
  }

  public Direction turnedLeft() {
    switch (this) {
      case UP:
        return LEFT;
      case RIGHT:
        return UP;
      case DOWN:
        return RIGHT;
      case LEFT:
        return DOWN;
      default:
        throw new IllegalArgumentException("Undefined how to turn left for " + this);
    }
  }

  public int deltaX() {
    switch (this) {
      case LEFT:
        return -1;
      case RIGHT:
        return 1;
      default:
        return 0;
    }
  }

  public int deltaY() {
    switch (this) {
      case UP:
        return -1;
      case DOWN:
        return 1;
      default:
        return 0;
    }
  }

}
