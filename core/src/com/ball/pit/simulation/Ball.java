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

public class Ball extends GameObject {

    public Ball (Model model, String node, btCollisionShape shape, Vector3 localInertia, float mass) {
        super(model, node, shape, localInertia, mass);
    }

    public void update (float delta) {
        // ball should be controlled by dynamics world in simulation now
    }
}
