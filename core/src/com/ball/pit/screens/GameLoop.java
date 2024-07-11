package com.ball.pit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.ball.pit.BallPit;
import com.ball.pit.Renderer;
import com.ball.pit.controllers.XBox360Pad;
import com.ball.pit.simulation.Simulation;
import com.ball.pit.simulation.SimulationListener;
import com.ball.pit.simulation.StageController;

public class GameLoop extends BallPitScreen implements SimulationListener, ControllerListener {

    private final Simulation simulation;
    private final Renderer renderer;
    Controller controller;

    // can put a controller listener here to send commands to the simulation listener?

    private ControllerListener listener = new ControllerAdapter();

    public GameLoop (BallPit ballPit) {
        super(ballPit);
        simulation = new Simulation();
        simulation.listener = this;
        renderer = new Renderer();
        // this is necessary for controller input to register in this class
        Controllers.addListener(this);

        /*
        *
        * Sound effect file load?
         */

        if (ballPit.getController() != null) {
            ballPit.getController().addListener(listener);
            controller = ballPit.getController();
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        if (ballPit.getController() != null) {
            ballPit.getController().removeListener(listener);
        }
        simulation.dispose();
    }

    @Override
    public boolean isDone () {
        /*
        *
        * Some game over condition?
         */
        return false;
    }

    @Override
    public void draw (float delta) { renderer.render(simulation, delta); }

    @Override
    public void update (float delta){
        simulation.update(delta);

        processInput(delta);
//        stageInput(delta);

    }

    private void processInput(float delta) {

        /*
         *
         * control input handling here?
         * pass controller input and delta values to the simluation class
         *
         */

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            renderer.rotateCameraLeft(delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            renderer.rotateCameraRight(delta);
        }

        if (controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f || controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f){
            simulation.stage.rotateX(controller.getAxis(XBox360Pad.AXIS_LEFT_X));
        }

        if (controller.getAxis(XBox360Pad.AXIS_LEFT_Y) > 0.2f || controller.getAxis(XBox360Pad.AXIS_LEFT_Y) < -0.2f){
            simulation.stage.rotateY(controller.getAxis(XBox360Pad.AXIS_LEFT_Y));
        }
    }

    public void connected(Controller controller){
        System.out.println("Connected! :: " + controller);
    }

    public void disconnected(Controller controller){
        System.out.println("Disconnected! :: " + controller);
    };

    public boolean buttonDown(Controller controller, int var2){return false;}
    public boolean buttonUp(Controller controller, int var2){return false;}

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }
}
