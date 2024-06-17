package com.ball.pit.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Ball extends ModelInstance {
    /*
    *
    * Set any important values related to ball size/velocity, lives etc.
    *
     */

    // call a different super if i need to have more control over how model is instantiated

    public Ball (Model model) { super(model); }

    public void update (float delta) {
        // the ball is being moved in simulation, but this can handle a significant change to the fields listed above
    }
}
