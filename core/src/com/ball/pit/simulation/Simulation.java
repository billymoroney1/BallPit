package com.ball.pit.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.Disposable;

public class Simulation implements Disposable {
    public Ball ball;
    public Stage stage;
    public transient SimulationListener listener;

    /*
    *
    * Set game boundary constants?
    *
     */

    public Model ballModel;
    public Model stageModel;

    public Simulation () { populate(); }

    private void populate () {
        // how load g3dj?
        ObjLoader objLoader = new ObjLoader();
        ballModel = objLoader.loadModel(Gdx.files.internal("data/pitball.g3dj"));
        stageModel = objLoader.loadModel(Gdx.files.internal("data/halfpipe.g3dj"));

        ball = new Ball(ballModel);
        stage = new Stage(stageModel, 0f, 0f, 0f);
    }

    public void update (float delta) {
        ball.update(delta);
        stage.update(delta);
        // check condition for next level
    }

    @Override
    public void dispose () {
        ballModel.dispose();
        stageModel.dispose();
    }
}
