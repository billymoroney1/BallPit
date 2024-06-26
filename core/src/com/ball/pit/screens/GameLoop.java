package com.ball.pit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.ball.pit.BallPit;
import com.ball.pit.Renderer;
import com.ball.pit.simulation.Simulation;
import com.ball.pit.simulation.SimulationListener;

public class GameLoop extends BallPitScreen implements SimulationListener {

    private final Simulation simulation;
    private final Renderer renderer;

    // can put a controller listener here to send commands to the simulation listener?

    private ControllerListener listener = new ControllerAdapter();

    public GameLoop (BallPit ballPit) {
        super(ballPit);
        simulation = new Simulation();
        simulation.listener = this;
        renderer = new Renderer();

        /*
        *
        * Sound effect file load?
         */

        if (ballPit.getController() != null) {
            ballPit.getController().addListener(listener);
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

    }
}
