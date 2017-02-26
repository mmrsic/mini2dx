package de.mmrsic.mini2dx.taxi.passengers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mmrsic.mini2dx.taxi.map.TaxiMap;

/**
 * Passenger managers are responsible to create new passengers and place them on a city map. Also, they keep track of
 * them in order to be able to remove them later on.
 *
 * @author Miro
 */
public class PassengerManager {

  private final TaxiMap mMap;
  private final PassengerFactory mFactory;
  private final List<Passenger> mList = new ArrayList<>();

  public PassengerManager(TaxiMap map, PassengerFactory factory) {
    mMap = map;
    mFactory = factory;
  }

  /**
   * Get all passengers added to this manager and not yet removed.
   * 
   * @return an unmodifiable list - never <code>null</code> but may be empty
   */
  public List<Passenger> passengerList() {
    return Collections.unmodifiableList(mList);
  }

  /**
   * A a single {@link Passenger} to this manager. The passenger will be added to the list of passengers and also
   * returned.
   * 
   * @return the created Passenger instance - never <code>null</code>
   */
  public Passenger addPassenger() {
    final Passenger newPassenger = mFactory.create(mMap);
    mList.add(newPassenger);
    return newPassenger;
  }

  /**
   * Remove a given passenger instance from the list of passengers of this manager.
   * 
   * @param passenger
   *          the passenger to remove - must not be <code>null</code>
   */
  public void removePassenger(Passenger passenger) {
    mList.remove(passenger);
  }

}
