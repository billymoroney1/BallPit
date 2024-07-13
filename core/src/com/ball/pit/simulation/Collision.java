package com.ball.pit.simulation;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

public class Collision {
    public static boolean collision;
    static btCollisionShape ballShape;
    static btCollisionObject ballObject;
    static btCollisionShape stageShape;
    static btCollisionObject stageObject;
    static btCollisionConfiguration collisionConfig;
    static btDispatcher dispatcher;
    static btDynamicsWorld dynamicsWorld;
    static btConstraintSolver constraintSolver;
    static btBroadphaseInterface broadphase;
    static btCollisionWorld collisionWorld;
    static Simulation.MyContactListener contactListener;

    // should these fields be static with this init method, or should i choose another pattern

    public static void init(ModelInstance ball, ModelInstance stage){
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
//        contactListener = new MyContactListener();

//        ballShape = Bullet.obtainStaticNodeShape(ball.nodes);
//        stageShape = Bullet.obtainStaticNodeShape(stage.nodes);

        ballShape = new btSphereShape(0.5f);
        stageShape = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));

        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);

        stageObject = new btCollisionObject();
        stageObject.setCollisionShape(stageShape);
        stageObject.setWorldTransform(stage.transform);
    }

    public static void disposeObjects(){
        ballShape.dispose();
        ballObject.dispose();
        stageShape.dispose();
        stageObject.dispose();
        collisionConfig.dispose();
        dispatcher.dispose();
    }

    public static void checkCollision(){
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(stageObject);

        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
        ci.dispose();
        co1.dispose();
        co0.dispose();

        collision = r;
    }

    public static void setBallObjectTransform(Matrix4 transform){
        ballObject.setWorldTransform(transform);
    }

    public static void setStageObjectTransform(Matrix4 transform) {
        stageObject.setWorldTransform(transform);
    }

    // generic method for many collision objects, depends on an Array of ModelInstances

//    class MyContactListener extends ContactListener {
//        @Override
//        public boolean onContactAdded (btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
//                                       btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
//            instances.get(colObj0Wrap.getCollisionObject().getuserValue()).moving = false;
//            instances.get(colObj1Wrap.getCollisionObject().getUserValue()).moving = false;
//        }
//    }

}
