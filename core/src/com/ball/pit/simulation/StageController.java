package com.ball.pit.simulation;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

public class StageController implements ControllerListener {

    // do i need this?

    public void connected(Controller controller){
    }

    public void disconnected(Controller controller){};

    public boolean buttonDown(Controller controller, int var2){return false;}
    public boolean buttonUp(Controller controller, int var2){return false;}

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        System.out.println(">>>> controller :: " + controller);
        System.out.println(">>>> axisCode :: " + axisCode);
        System.out.println(">>>> value :: " + value);
        return false;
    }
}
