/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmrsic.mini2dx.taxi.input;

/**
 * Configuration of the keys for user input.
 *
 * @author Miro
 */
public interface KeyConfiguration {

   /**
    * Key code representing the aim to turn left relative to the current direction.
    *
    * @return value from {@link com.badlogic.gdx.Input.Keys}
    */
   public int turnLeft();

   /**
    * Key code representing the aim to turn right relative to the current direction.
    *
    * @return value from {@link com.badlogic.gdx.Input.Keys}
    */
   public int turnRight();

   /**
    * Key code representing the aim to not to turn left or right, but to go straight on.
    *
    * @return value from {@link com.badlogic.gdx.Input.Keys}
    */
   public int dontTurn();

}
