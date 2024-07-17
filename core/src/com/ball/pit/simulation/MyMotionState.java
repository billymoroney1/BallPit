package com.ball.pit.simulation;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MyMotionState extends btMotionState {
    Matrix4 transform;
    Vector3 tempLoc = new Vector3();
    Ball ball;

    public MyMotionState(Ball ball){
        this.ball= ball;
    }

    @Override
    public void getWorldTransform (Matrix4 worldTrans) {
        worldTrans.set(transform);
    }
    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        transform.getTranslation(tempLoc);
        if(tempLoc.y > -10f)
            transform.set(worldTrans);
        else {
            this.ball.respawn();
        }
    }
}
