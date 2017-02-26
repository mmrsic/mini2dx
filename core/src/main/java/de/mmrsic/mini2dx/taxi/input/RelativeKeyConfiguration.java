/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmrsic.mini2dx.taxi.input;

import com.badlogic.gdx.Input;

/**
 * Key configuration implementation for relative turn commands: LEFT arrow turns left, RIGTH arrow turns right, UP arrow
 * goes straight on.
 *
 * @author Miro
 */
public class RelativeKeyConfiguration implements KeyConfiguration {

   @Override
   public int turnLeft() {
      return Input.Keys.LEFT;
   }

   @Override
   public int turnRight() {
      return Input.Keys.RIGHT;
   }

   @Override
   public int dontTurn() {
      return Input.Keys.UP;
   }

}
