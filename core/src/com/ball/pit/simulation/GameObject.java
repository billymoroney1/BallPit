package com.ball.pit.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable {
    public final btRigidBody body;
    public final btCollisionShape shape;
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public final MyMotionState motionState;

    public GameObject (Model model, String node, btCollisionShape shape, Vector3 localInertia, float mass) {
        super(model, node);

        motionState = new MyMotionState();
        motionState.transform = transform;

        this.shape = shape;
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);
    }

    static class MyMotionState extends btMotionState {
        Matrix4 transform;
        @Override
        public void getWorldTransform (Matrix4 worldTrans) {
            worldTrans.set(transform);
        }
        @Override
        public void setWorldTransform(Matrix4 worldTrans) {
            transform.set(worldTrans);
        }
    }

    @Override
    public void dispose () {
        body.dispose();
    }
}