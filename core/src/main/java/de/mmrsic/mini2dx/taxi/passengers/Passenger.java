package de.mmrsic.mini2dx.taxi.passengers;

import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.graphics.Texture;

import de.mmrsic.mini2dx.taxi.map.Position;
import de.mmrsic.mini2dx.taxi.vehicles.Vehicle;

/**
 * Passengers strive along the city and are willing to pay to a taxi drive to a certain target on the city map. The fee
 * for transport decreases over time, even if they are on board. So, the taxi driver is responsible for delivering them
 * in time.
 */
public class Passenger extends Sprite {

  private final Position mStart;
  private final Position mTarget;

  private final long mCreationTime;
  private final int mStartFee;

  private Vehicle mVehicle;

  public Passenger(Texture texture, Position start, Position target, int startFee) {
    super(texture);

    mCreationTime = System.currentTimeMillis();
    mStart = start;
    mTarget = target;
    mStartFee = startFee;
  }

  /**
   * Get the starting position of this passenger.
   * 
   * @return the same instance for each call - never <code>null</code>
   */
  public Position getStart() {
    return mStart;
  }

  /**
   * Get the target position of this passenger.
   * 
   * @return the same instance for each call - never <code>null</code>
   */
  public Position getTarget() {
    return mTarget;
  }

  /**
   * Get the fee this passenger is willing to pay when he is placed on the map (at the beginning of his journey). This
   * fee will decrease over time (whether waiting or riding) and may drop to zero before being delivered.
   * 
   * @return a constant value for each call - always > 0
   */
  public int getStartFee() {
    return mStartFee;
  }

  /**
   * The amount of time since this passenger was created, that is places on the map.
   *
   * @return value >=0, milliseconds since 1970 January, 1st
   */
  public long getCreationTime() {
    return mCreationTime;
  }

  /**
   * Get the fee this passenger would pay at this time, if he got to his target.
   *
   * @return a value >= 0 (cents)
   */
  public int getCurrentFee() {
    final int ageInSeconds = (int) age() / 1000;
    return Math.max(0, getStartFee() - ageInSeconds);
  }

  /**
   * Get the age of this passenger in milliseconds.
   *
   * @return milliseconds since creation time
   */
  public long age() {
    return System.currentTimeMillis() - getCreationTime();
  }

  // ACTIONS //

  /**
   * Get on a given vehicle.
   *
   * @param vehicle
   *          the vehicle this passenger should get on - must not be null
   */
  public void getOn(Vehicle vehicle) {
    if (mVehicle != null) {
      throw new IllegalStateException("Already riding " + mVehicle + " when trying to get on " + vehicle);
    }
    mVehicle = vehicle;
  }

  /**
   * Get this passenger off the vehicle he got on the last time.
   *
   * @return the money paid for the ride, a value >= 0
   */
  public int getOff() {
    if (mVehicle == null) {
      throw new IllegalStateException("Tried to get off while not riding");
    }
    mVehicle = null;
    return getCurrentFee();
  }

}
