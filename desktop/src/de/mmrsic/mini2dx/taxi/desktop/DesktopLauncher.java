package de.mmrsic.mini2dx.taxi.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import de.mmrsic.mini2dx.taxi.TaxiGame;

public class DesktopLauncher {

  public static void main(String[] arg) {
    final DesktopMini2DxConfig config = new DesktopMini2DxConfig(TaxiGame.GAME_IDENTIFIER);
    config.vSyncEnabled = true;
    config.title = "Mini Taxi";
    config.height = 640;
    new DesktopMini2DxGame(new TaxiGame(), config);
  }

}
