package com.ball.pit.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable {
    public final btRigidBody body;
    public boolean moving;

    public GameObject (Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(model, node);
        body = new btRigidBody(constructionInfo);
    }

    @Override
    public void dispose () {
        body.dispose();
    }

    // Maybe should enhance this class to assign the proper classes like ball and stage so that the GameObjects can have more custom logic

    static class Constructor implements Disposable {
        public final Model model;
        public final String node;
        public final btCollisionShape shape;
        public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
        private static Vector3 localInertia = new Vector3();

        public Constructor (Model model, String node, btCollisionShape shape, float mass){
            this.model = model;
            this.node = node;
            this.shape = shape;
            if (mass > 0f)
                shape.calculateLocalInertia(mass, localInertia);
            else
                localInertia.set(0, 0, 0);
            this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        }

        public GameObject construct() {
            return new GameObject(model, node, constructionInfo);
        }

        @Override
        public void dispose () {
            shape.dispose();
            constructionInfo.dispose();
        }
    }
}