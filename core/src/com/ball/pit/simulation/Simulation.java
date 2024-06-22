package com.ball.pit.simulation;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.UBJsonReader;

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

        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        ballModel = modelLoader.loadModel(Gdx.files.getFileHandle("data/pitball.g3db", Files.FileType.Internal));
        stageModel = modelLoader.loadModel(Gdx.files.getFileHandle("data/halfpipe.g3db", Files.FileType.Internal));

        ball = new Ball(ballModel);
        Vector3 ballPosition = new Vector3();
        ball.transform.getTranslation(ballPosition);
        stage = new Stage(stageModel, ballPosition.x, ballPosition.y - 100f, ballPosition.z);
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

    public void rotateCameraLeft(float delta, float increment){

    }
}
