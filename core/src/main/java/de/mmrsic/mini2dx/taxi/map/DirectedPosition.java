package de.mmrsic.mini2dx.taxi.map;

/**
 * Directed position on a taxi city map: x/y values on the so named axises with a direction set to one of the four
 * possible values.
 *
 * @author Miro
 */
public class DirectedPosition {

  private final Position pos;
  private final Direction dir;

  public DirectedPosition(Position pos, Direction dir) {
    this.pos = pos;
    this.dir = dir;
  }

  public DirectedPosition(int x, int y, Direction dir) {
    this(new Position(x, y), dir);
  }

  @Override
  public String toString() {
    return new StringBuilder().append(getPosition()).append(' ').append(getDirection().name()).toString();
  }

  public Position getPosition() {
    return pos;
  }

  public int getX() {
    return getPosition().getX();
  }

  public int getY() {
    return getPosition().getY();
  }

  public Direction getDirection() {
    return dir;
  }

  public DirectedPosition moved(int num) {
    final int newX = getX() + num * dir.deltaX();
    final int newY = getY() + num * dir.deltaY();
    return new DirectedPosition(new Position(newX, newY), dir);
  }

  public DirectedPosition turnedLeft() {
    return new DirectedPosition(getPosition(), getDirection().turnedLeft());
  }

  public DirectedPosition turnedRight() {
    return new DirectedPosition(getPosition(), getDirection().turnedRight());
  }
}
