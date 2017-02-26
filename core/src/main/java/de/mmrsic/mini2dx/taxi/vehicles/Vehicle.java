/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmrsic.mini2dx.taxi.vehicles;

import com.badlogic.gdx.graphics.Texture;
import de.mmrsic.mini2dx.taxi.map.DirectedPosition;
import de.mmrsic.mini2dx.taxi.map.Direction;
import de.mmrsic.mini2dx.taxi.map.TaxiMap;
import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

/**
 * A Vehicle is a Sprite that may go over "Street" tiles. It is either controlled by the user or by the game engine.
 *
 * @author Miro
 */
public class Vehicle extends Sprite {

  private TaxiMap mMap;
  private DirectedPosition mMapPos;
  private float mSpeed;
  private Direction mTurnSignals = Direction.UP;
  private double mOverallDistance;

  private final CollisionBox mColBox = new CollisionBox(0, 0, 16, 16);

  public Vehicle(Texture textureUp, TaxiMap map, DirectedPosition pos) {
    super(textureUp);
    mMap = map;
    mMapPos = pos;
    if (pos.getDirection() != Direction.UP) {
      final int angle = 180 - 90 * pos.getDirection().deltaX();
      rotate(angle);
      System.out.println("rotate(" + angle + ")");
    }
    setTilePosition(pos.getX(), pos.getY());

    mSpeed = 50;
    mOverallDistance = 0;
  }

  public void setMap(TaxiMap map) {
    mMap = map;
  }

  public void placeAt(DirectedPosition newPos) {
    setTilePosition(newPos.getX(), newPos.getY());
    while (newPos.getDirection() != mMapPos.getDirection()) {
      turnRight();
    }
  }

  public void update(GameContainer container, float delta) {
    mColBox.preUpdate();

    move(delta);
    mColBox.update(container, delta);
  }

  public void interpolate(GameContainer container, float alpha) {
    mColBox.interpolate(container, alpha);
  }

  public void draw(Graphics g) {
    // mColBox.draw(g); // in order to draw collision box 
    g.drawSprite(this, mColBox.getRenderX(), mColBox.getRenderY());
  }

  public void signalRight() {
    System.out.println("Vehicle.signalRight()");
    mTurnSignals = Direction.RIGHT;
  }

  public void signalLeft() {
    System.out.println("Vehicle.signalLeft()");
    mTurnSignals = Direction.LEFT;
  }

  public void signalOff() {
    System.out.println("Vehicle.signalOff()");
    mTurnSignals = Direction.UP;
  }

  // HELPERS //
  /**
   * Move this vehicle straight along the street for a given fraction of seconds.
   */
  private void move(float deltaTime) {
    // Save old position
    final float oldXPos = getX();
    final float oldYPos = getY();
    final Direction oldDir = mMapPos.getDirection();
    // Get new position
    final float deltaDistance = mSpeed * deltaTime;
    final float newXPos = oldXPos + deltaDistance * oldDir.deltaX();
    final float newYPos = oldYPos + deltaDistance * oldDir.deltaY();
    DirectedPosition toPos = mMapPos.moved(1);
    if (oldDir.deltaX() < 0 && newXPos <= toPos.getX() * mMap.getTileWidth()
            || oldDir.deltaX() > 0 && newXPos >= toPos.getX() * mMap.getTileWidth()
            || oldDir.deltaY() < 0 && newYPos <= toPos.getY() * mMap.getTileHeight()
            || oldDir.deltaY() > 0 && newYPos >= toPos.getY() * mMap.getTileHeight()) {
//      if (!mMap.isTilePosition(toPos.getX(), toPos.getY())) {
//        exitMap();
//      }
      setTilePosition(toPos.getX(), toPos.getY());
      if (!mMap.allowsPlacement(toPos)) {
        turnLeft(); // To turn right is no option here
        signalOff();
      } else if (mMap.allowsOnlyRightTurn(toPos)) {
        turnRight();
        signalOff();
      } else if (wantsRight() && mMap.allowsPlacement(toPos.turnedRight())) {
        turnRight();
        signalOff();
      } else if (wantsLeft() && mMap.allowsPlacement(toPos.turnedLeft().moved(1))) {
        turnLeft();
        signalOff();
      }
      final float timeConsumedSoFar = (Math.abs(newXPos - getX()) + Math.abs(newYPos - getY())) / mSpeed;
      final float timeToProcess = deltaTime - timeConsumedSoFar;
      if (timeToProcess > 0) {
        move(timeToProcess);
      }
    } else {
      mColBox.setPosition(newXPos, newYPos);
      setPosition(newXPos, newYPos);
    }

    mOverallDistance += deltaTime;
  }

  protected boolean wantsRight() {
    return mTurnSignals == Direction.RIGHT;
  }

  protected boolean wantsLeft() {
    return mTurnSignals == Direction.LEFT;
  }

  private void turnRight() {
    rotate90(false); // Caution: Javadoc is not correct for Mini2DX as opposed to LibGDX
    setDirection(mMapPos.getDirection().turnedRight());
  }

  private void turnLeft() {
    rotate90(true);
    setDirection(mMapPos.getDirection().turnedLeft());
  }

  private void setTilePosition(int x, int y) {
    mColBox.setPosition(x * mMap.getTileWidth(), y * mMap.getTileHeight());
    setPosition(mColBox.getX(), mColBox.getY());
    mMapPos = new DirectedPosition(x, y, mMapPos.getDirection());
    System.out.println("Vehicle.setTilePosition(" + x + "," + y + "): " + mMapPos);
  }

  private void setDirection(Direction newDir) {
    mMapPos = new DirectedPosition(mMapPos.getPosition(), newDir);
    System.out.println("Vehicle.setDirection(" + newDir + "): " + mMapPos);
  }

}
