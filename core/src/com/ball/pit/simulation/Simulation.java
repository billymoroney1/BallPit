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
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
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
    btConstraintSolver constraintSolver;
    btDynamicsWorld dynamicsWorld;

    ArrayList<GameObject> instances = new ArrayList<GameObject>();

    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded (int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1, int index1, boolean match1) {
            if (match0){
                System.out.println("match0");
                ((ColorAttribute)instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            }
            if (match1){
                System.out.println("match1");
                ((ColorAttribute)instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            }
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

        // Initialize model instances
        ground = new ModelInstance(model, "ground");

        // need to try and execute the constructor info but in a way that can be native to the sub classes?
        ball = new Ball(ballModel, "Sphere", new btSphereShape(0.65f), new Vector3(), 1f);
        ball.transform.trn(0f, 20f, 0f);
//        ball.body.setWorldTransform(ball.transform);
        ball.body.setUserValue(1);
        ball.body.proceedToTransform(ball.transform);
        ball.body.setCollisionFlags(ball.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        stage = new Stage(model, "ground", new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f)), new Vector3(), 0f);
        stage.body.setCollisionFlags(stage.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        instances.add(stage);
        instances.add(ball);

        // Collision Configuration
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        contactListener = new MyContactListener();

        dynamicsWorld.addRigidBody(ball.body);
        ball.body.setContactCallbackFlag(OBJECT_FLAG);
        ball.body.setContactCallbackFilter(GROUND_FLAG);
        dynamicsWorld.addRigidBody(stage.body);
        stage.body.setContactCallbackFlag(GROUND_FLAG);
        stage.body.setContactCallbackFilter(0);
        stage.body.setActivationState(Collision.DISABLE_DEACTIVATION);



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
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
        ball.update(delta);
//        ball.body.getWorldTransform(ball.transform);
        stage.update(delta);
//        stage.body.getWorldTransform(stage.transform);
    }

    @Override
    public void dispose () {
        ballModel.dispose();
        stageModel.dispose();

        for (GameObject obj : instances)
            obj.dispose();
        instances.clear();

        collisionWorld.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
        dynamicsWorld.dispose();
        constraintSolver.dispose();

        contactListener.dispose();
    }
}
