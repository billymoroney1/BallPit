package com.ball.pit.simulation;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.UBJsonReader;

public class Simulation implements Disposable {
    public Ball ball;
    public Stage stage;
    public ModelInstance ground;
    public ModelInstance testBall;
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

//        stage = new Stage(stageModel, ballPosition.x, ballPosition.y - 10f, ballPosition.z);

        Vector3 stagePosition = new Vector3();
        stage = new Stage(stageModel);
        stage.transform.getTranslation(stagePosition);
//        ball = new Ball(ballModel, stagePosition.x + -40f, stagePosition.y + 25f, stagePosition.z);
        ball = new Ball(ballModel, 0, 9f, 0);
//        Vector3 ballPosition = new Vector3();
//        ball.transform.getTranslation(ballPosition);


        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED))).box(5f, 1f, 5f);
        mb.node().id = "ball";
        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
        Model model = mb.end();
        ground = new ModelInstance(model, "ground");
        testBall = new ModelInstance(model, "ball");
        testBall.transform.setToTranslation(0, 9f, 0);

//        Collision.init(ball, stage);

        Collision.init(ball, ground);
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
