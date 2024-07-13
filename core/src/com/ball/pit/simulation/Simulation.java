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
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.ArrayList;

public class Simulation implements Disposable {
    public Ball ball;
    public Stage stage;
    public ModelInstance ground;
    public ModelInstance testBall;
    public transient SimulationListener listener;
    public ArrayMap<String, GameObject.Constructor> constructors;

    // what is this definition?

    final static short GROUND_FLAG = 1 << 8;
    final static short OBJECT_FLAG = 1 << 9;
    final static short ALL_FLAG = -1;

    /*
    *
    * Set game boundary constants?
    *
     */

    public Model ballModel;
    public Model stageModel;

    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    MyContactListener contactListener;
    btBroadphaseInterface broadphase;
    btCollisionWorld collisionWorld;

    ArrayList<GameObject> instances;

    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
            instances.get(userValue0).moving = false;
            instances.get(userValue1).moving = false;
            return true;
        }
    }

    public Simulation () { populate(); }

    private void populate () {
        // Load models
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        ballModel = modelLoader.loadModel(Gdx.files.getFileHandle("data/pitball.g3db", Files.FileType.Internal));
        stageModel = modelLoader.loadModel(Gdx.files.getFileHandle("data/halfpipe.g3db", Files.FileType.Internal));

        // Construct generic ground model
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED))).box(5f, 1f, 5f);
//        mb.node().id = "ball";
//        mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
        Model model = mb.end();

        //Generic game object constructor pattern
        constructors = new ArrayMap<String, GameObject.Constructor>(String.class, GameObject.Constructor.class);
        constructors.put("ground", new GameObject.Constructor(model, "ground", new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f)), 0f));
        constructors.put("ball", new GameObject.Constructor(ballModel, "ball", new btSphereShape(3f), 1f));

        // Initialize model instances
        ground = new ModelInstance(model, "ground");
//        stage = new Stage(model);
        ball = new Ball(ballModel, 0, 9f, 0);
//        testBall = new ModelInstance(model, "ball");
//        testBall.transform.setToTranslation(0, 9f, 0);

        // Collision Configuration
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        contactListener = new MyContactListener();

        // Populate instances with ground
//        instances = new ArrayList<GameObject>();
//        GameObject object = constructors.get("ground").construct();
//        instances.add(object);

        // if i pass everything through game object, then i can get bt rigid body and construction info
//        collisionWorld.addCollisionObject(object.body, GROUND_FLAG, ALL_FLAG);

//        Collision.init(ball, stage);

//        Collision.init(ball, ground);
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

        for (GameObject obj : instances)
            obj.dispose();
        instances.clear();

        for (GameObject.Constructor ctor : constructors.values())
            ctor.dispose();
        constructors.clear();

        collisionWorld.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();

        contactListener.dispose();
    }
}
