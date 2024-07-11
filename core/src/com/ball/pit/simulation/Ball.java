package com.ball.pit.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class Ball extends ModelInstance {

    public final btRigidBody body;
    public boolean moving;
    public final float mass = 1f;
    public final btCollisionShape ballShape = new btSphereShape(1f);
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    private static Vector3 localInertia = new Vector3();
    /*
    *
    * Set any important values related to ball size/velocity, lives etc.
    *
     */

    // call a different super if i need to have more control over how model is instantiated

    public Ball (Model model) {
        super(model);
        ballShape.calculateLocalInertia(mass, localInertia);
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, ballShape, localInertia);
        body = new btRigidBody(constructionInfo);
    }

    public Ball (Model model, float x, float y, float z) {
        super(model, x, y, z);
        ballShape.calculateLocalInertia(mass, localInertia);
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, ballShape, localInertia);
        body = new btRigidBody(constructionInfo);
    }

    public void update (float delta) {
        // the ball is being moved in simulation, but this can handle a significant change to the fields listed above
        if (!Collision.collision) {
            this.transform.translate(0f, -delta, 0f);
            Collision.setBallObjectTransform(this.transform);
            Collision.checkCollision();
        }
    }
}
