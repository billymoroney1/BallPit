package com.ball.pit.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import static java.lang.Math.abs;

public class Stage extends GameObject {
    /*
    *
    * any values pertaining to stage size/boundaries?
    *
     */

    private Matrix4 stageTransform = new Matrix4();
    private Vector3 movePosition = new Vector3(0f, 0f, 0f);
    private Quaternion rotation = new Quaternion();
    private float currentDegreesX = 0f;
    private float currentDegreesY = 0f;

    public Stage (Model model, String node, btCollisionShape shape, Vector3 localInertia, float mass) {
        super(model, node, shape, localInertia, mass);
    }

//    public Stage (Model model, float x, float y, float z) {
//        super(model, x, y, z);
//    }

    public void update(float delta){
        // what would i do in here? change stage?
        applyRotation();
//        Collision.setStageObjectTransform(this.transform);
    }

    public void rotateX(float amount){
        stageTransform.set(this.transform);

        currentDegreesX += amount;

        if (currentDegreesX > 20f)
            currentDegreesX = 20f;

        if (currentDegreesX < -20f)
            currentDegreesX = -20f;

        if (abs(currentDegreesX) < 20f){
            stageTransform.rotate(Vector3.X, amount);
        }

        this.transform.set(stageTransform);
        this.transform.translate(movePosition);
    }

    public void rotateY(float amount){
        stageTransform.set(this.transform);
        currentDegreesY += amount;

        if (currentDegreesY > 20f)
            currentDegreesY = 20f;

        if (currentDegreesY < -20f)
            currentDegreesY = -20f;

        if (abs(currentDegreesY) < 20f){
            stageTransform.rotate(Vector3.Y, amount);
        }
        this.transform.set(stageTransform);
        this.transform.translate(movePosition);
    }

    private void applyRotation(){
        this.transform.translate(movePosition);
        this.transform.getRotation(rotation);
    }

}
