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
import com.badlogic.gdx.utils.Disposable;

public class Ball extends ModelInstance implements Disposable {

    public final btRigidBody body;
    public final long cPtr;
    public final btCollisionShape shape;
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public MyMotionState motionState;

    public Ball (Model model, String node, btCollisionShape shape, Vector3 localInertia, float mass) {
        super(model, node);

        motionState = new MyMotionState(this);
        motionState.transform = transform;

        this.shape = shape;
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);
        this.cPtr = body.getCPointer();
    }

    public void update (float delta) {
        // ball should be controlled by dynamics world in simulation now
    }

    public void respawn(){
        this.motionState.dispose();
        motionState = new MyMotionState(this);
        motionState.transform = transform;
        body.setLinearVelocity(new Vector3(0f, 0f, 0f));
        body.clearForces();
        System.out.println(body.getAngularVelocity());
        body.setAngularVelocity(new Vector3(0f, 0f, 0f));
        this.transform.setToTranslation(0f, 20f, 0f);
        body.setMotionState(motionState);
    }

    @Override
    public void dispose () {
        body.dispose();
        motionState.dispose();
    }
}
