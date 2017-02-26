package de.mmrsic.mini2dx.taxi;

import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TiledMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.mmrsic.mini2dx.taxi.input.KeyConfiguration;
import de.mmrsic.mini2dx.taxi.input.RelativeKeyConfiguration;
import de.mmrsic.mini2dx.taxi.map.DirectedPosition;
import de.mmrsic.mini2dx.taxi.map.Position;
import de.mmrsic.mini2dx.taxi.map.TaxiMap;
import de.mmrsic.mini2dx.taxi.passengers.Passenger;
import de.mmrsic.mini2dx.taxi.passengers.PassengerFactory;
import de.mmrsic.mini2dx.taxi.passengers.PassengerManager;
import de.mmrsic.mini2dx.taxi.vehicles.Vehicle;

public class TaxiGame extends BasicGame {

  private static final int START_FEE = 100;

  public static final String GAME_IDENTIFIER = "de.mmrsic.mini2dx.taxi";
  public static final Random RANDOM = new Random();

  private static final Logger LOGGER = Logger.getLogger(TaxiGame.class.getName());

  private KeyConfiguration mKeyConfig;

  private TaxiMap mMap;
  private int mTileWidth;
  private int mTileHeight;

  private Vehicle mTaxi;

  @Override
  public void initialise() {

    LOGGER.fine("Directory: " + new File(".").getAbsolutePath());

    mKeyConfig = new RelativeKeyConfiguration();

    mMap = new TaxiMap("levels/002.tmx");
    try {
      mMap.load();
    } catch (Exception ex) {
      return;
    }
    final TiledMap tiledMap = mMap.tiled();
    mTileWidth = tiledMap.getTileWidth();
    mTileHeight = tiledMap.getTileHeight();

    final DirectedPosition taxiEntry = mMap.getRandomEntryPoint();
    LOGGER.log(Level.FINE, "Entry point chosen: {0}", taxiEntry);
    final Texture taxiTexture = new Texture(Gdx.files.internal("sprites/TaxiCar/default.png"));
    mTaxi = new Vehicle(taxiTexture, mMap, taxiEntry);

    // TODO Use Creator instance to generate Passengers
    initialisePassengerManagement();
  }

  @Override
  public void update(float delta) {
    mTaxi.update(this, delta);
  }

  @Override
  public void interpolate(float alpha) {
    mTaxi.interpolate(this, alpha);
  }

  @Override
  public void render(Graphics g) {
    g.scale(2f, 2f);
    mMap.tiled().draw(g, 0, 0, mMap.tiled().getLayerIndex("Ground"));
    mTaxi.draw(g);
  }

  @Override
  public boolean keyDown(int keycode) {
    LOGGER.log(Level.INFO, "keycode={0}", keycode);
    if (mKeyConfig.turnLeft() == keycode) {
      // Create key configuration for turnLeft, turnRight, and dontTurn
      // actions
      mTaxi.signalLeft();
    } else if (mKeyConfig.turnRight() == keycode) {
      mTaxi.signalRight();
    } else if (mKeyConfig.dontTurn() == keycode) {
      // Remove intention to turn left or right and go straight ahead
    }
    return super.keyDown(keycode);
  }

  // HELPERS //

  private void initialisePassengerManagement() {
    final PassengerManager passengerMgr = new PassengerManager(mMap, passengerFactory());
    final Creator passengerCreator = new Creator(() -> passengerMgr.addPassenger());
    passengerCreator.setDelay(36_000);
    passengerCreator.start();
  }

  private PassengerFactory passengerFactory() {
    return new PassengerFactory() {
      @Override
      public Passenger create(TaxiMap map) {
        final Position start = null;
        final Position target = null;
        return new Passenger(new Texture(Gdx.files.internal("sprites/Passenger.png")), start, target, START_FEE);
      }
    };
  }

  private Object passengerTiles(TaxiMap mMap2) {
    // TODO Auto-generated method stub
    return null;
  }

}
