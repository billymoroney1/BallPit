package com.ball.pit.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Stage extends ModelInstance {
    /*
    *
    * any values pertaining to stage size/boundaries?
    *
     */

    public Stage (Model model) { super(model); }

    public Stage (Model model, float x, float y, float z) { super(model, x, y, z); }

    public void update(float delta){
        // what would i do in here? change stage?
    }

}
