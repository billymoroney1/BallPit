package com.ball.pit.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class Ball extends ModelInstance {
    /*
    *
    * Set any important values related to ball size/velocity, lives etc.
    *
     */

    // call a different super if i need to have more control over how model is instantiated

    public Ball (Model model) {
        super(model);
    }

    public Ball (Model model, float x, float y, float z) {
        super(model, x, y, z);
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
