/*
 * (c) 2016 by mirmalu
 */
package de.mmrsic.mini2dx.taxi.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TiledMap;

import com.badlogic.gdx.Gdx;

import de.mmrsic.mini2dx.taxi.TaxiGame;

/**
 * A taxi map represents a single level of the playground of a taxi game.
 *
 * @author Miro
 */
public class TaxiMap {

  private static final Logger LOGGER = Logger.getLogger(TaxiGame.class.getName());

  private final String mFileName;
  private TiledMap mTiledMap;
  private final Set<DirectedPosition> mEntryPoints;
  private final EnumMap<Direction, String> mDirToProp;
  private final EnumMap<Property, Set<Tile>> mPropToTiles;

  private final List<Position> mPassengerPositions = new ArrayList<>();

  public TaxiMap(final String i_fileName) {
    mFileName = i_fileName;

    mEntryPoints = new HashSet<DirectedPosition>();

    mDirToProp = new EnumMap<Direction, String>(Direction.class);
    mDirToProp.put(Direction.UP, Property.Up.name());
    mDirToProp.put(Direction.LEFT, Property.Left.name());
    mDirToProp.put(Direction.DOWN, Property.Down.name());
    mDirToProp.put(Direction.RIGHT, Property.Right.name());

    mPropToTiles = new EnumMap<Property, Set<Tile>>(Property.class);
    for (Property property : Property.values()) {
      mPropToTiles.put(property, new HashSet<Tile>());
    }
  }

  public TiledMap tiled() {
    return mTiledMap;
  }

  public void load() throws Exception {
    try {
      mTiledMap = new TiledMap(Gdx.files.internal(mFileName));
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Couldn't load map " + new File(mFileName).getAbsolutePath(), ex);
      throw ex;
    }
    collectEntryPoints();
    collectTileProperties();
  }

  public Set<DirectedPosition> getEntryPoints() {
    return mEntryPoints;
  }

  public DirectedPosition getRandomEntryPoint() {
    final int numEntryPoints = mEntryPoints.size();
    if (numEntryPoints < 1) {
      throw new IllegalStateException("No entry points found");
    }

    final int randomPos = TaxiGame.RANDOM.nextInt(numEntryPoints);
    return new ArrayList<DirectedPosition>(mEntryPoints).get(randomPos);
  }

  // MOVING //

  /**
   * Get the tile at the ground layer for given x and y positions.
   * 
   * @param x
   *          the x position to query
   * @param y
   *          the y position to query
   * @return the same instance per call - never <code>null</code>
   */
  public Tile getTile(int x, int y) {
    return mTiledMap.getTile(x, y, groundLayerIdx());
  }

  public int getTileWidth() {
    return tiled().getTileWidth();
  }

  public int getTileHeight() {
    return tiled().getTileHeight();
  }

  public Set<Tile> getTiles(Property property) {
    return Collections.unmodifiableSet(mPropToTiles.get(property));
  }

  /**
   * Check whether this taxi city map allows for placement at a given directed position.
   *
   * @param pos
   *          the position in question - must not be null
   * @return true if placement is allowed, false otherwise
   */
  public boolean allowsPlacement(DirectedPosition pos) {
    final String prop = mDirToProp.get(pos.getDirection());
    return getTile(pos.getX(), pos.getY()).containsProperty(prop);
  }

  /**
   * Check whether this taxi city map allows only for right turn at a given directed position.
   *
   * @param pos
   * @return true if only right turn is allowed, false otherwise
   */
  public boolean allowsOnlyRightTurn(DirectedPosition pos) {
    return allowsPlacement(pos.turnedRight()) && !allowsPlacement(pos.moved(1))
        && !allowsPlacement(pos.moved(1).turnedLeft().moved(1));
  }

  /**
   * Collect all the entry points the this taxi map provides.
   */
  private void collectEntryPoints() {
    // Collect entry points from left and right
    for (int r = 0; r < mTiledMap.getHeight(); r++) {
      for (int c : new int[] { 0, mTiledMap.getWidth() - 1 }) {
        final Tile candidate = getTile(c, r);
        if (candidate != null && candidate.containsProperty(c == 0 ? Property.Right.name() : Property.Left.name())) {
          mEntryPoints.add(new DirectedPosition(c, r, c == 0 ? Direction.RIGHT : Direction.LEFT));
        }
      }
    }
    // Collect map entry points from top and button
    for (int c = 0; c < mTiledMap.getWidth(); c++) {
      for (int r : new int[] { 0, mTiledMap.getHeight() - 1 }) {
        final Tile candidate = getTile(c, r);
        if (candidate != null && candidate.containsProperty(r == 0 ? Property.Down.name() : Property.Up.name())) {
          mEntryPoints.add(new DirectedPosition(c, r, r == 0 ? Direction.DOWN : Direction.UP));
        }
      }
    }
  }

  private void collectPassengerPositions() {
    for (int r = 0; r < mTiledMap.getHeight(); r++) {
      for (int c = 0; c < mTiledMap.getWidth(); c++) {
        final Tile tile = getTile(r, c);
        if (tile != null && tile.containsProperty(Property.Street.name())) {
          mPassengerPositions.add(new Position(c, r));
        }
      }
    }
  }

  private int groundLayerIdx() {
    return mTiledMap.getLayerIndex("Ground");
  }

  private void collectTileProperties() {
    for (int row = 0; row < mTiledMap.getHeight(); row++) {
      for (int col = 0; col < mTiledMap.getWidth(); col++) {
        final Tile tile = getTile(row, col);
        for (Property property : Property.values()) {
          if (tile.containsProperty(property.name())) {
            mPropToTiles.get(property).add(tile);
          }
        }
      }
    }
    LOGGER.finest("Tile properties for loaded map: " + mPropToTiles);
  }

}
