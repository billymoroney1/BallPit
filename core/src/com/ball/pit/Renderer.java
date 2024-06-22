package com.ball.pit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.ball.pit.simulation.Ball;
import com.ball.pit.simulation.Simulation;

public class Renderer {

    private SpriteBatch spriteBatch;
    private BitmapFont font;

    /* matrices for rendering and transforming 3d objects */
    private final Matrix4 viewMatrix = new Matrix4();
    private final Matrix4 transform = new Matrix4();
    private final Matrix4 normal = new Matrix4();
    private final Matrix3 normal3 = new Matrix3();

    private PerspectiveCamera camera;
    private Vector3 rotationAxis = new Vector3();
    private float rotationDirection;

    private String status = "";

    // what is the value of initializing objects that are reused? speed?
    final Vector3 tmpV = new Vector3();

    ModelBatch modelBatch;

    Environment lights;

    public Renderer () {
        try {
            // init batches here, load textures/lighting/camera
            lights = new Environment();
            lights.add(new DirectionalLight().set(Color.WHITE, new Vector3(-1, -0.5f, 0).nor()));

            spriteBatch = new SpriteBatch();
            modelBatch = new ModelBatch();

            font = new BitmapFont();

            // can print a status message on screen

            camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void render (Simulation simulation, float delta) {
        // gl init
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //background render

        gl.glEnable(GL20.GL_DEPTH_TEST);
        gl.glEnable(GL20.GL_CULL_FACE);
        setProjectionAndCamera(simulation.ball);

        // set projection and camera

        modelBatch.begin(camera);
        modelBatch.render(simulation.ball);
        modelBatch.render(simulation.stage);
        modelBatch.end();

        // Need to understand openGL better to understand this wrapper

        gl.glDisable(GL20.GL_CULL_FACE);
        gl.glDisable(GL20.GL_DEPTH_TEST);

        spriteBatch.begin();
        spriteBatch.enableBlending();
        font.draw(spriteBatch, status, 0, 320);
        spriteBatch.end();
    }

    public void setProjectionAndCamera(Ball ball){
        ball.transform.getTranslation(tmpV);
        camera.position.set(tmpV.x, 6, 2);
        camera.direction.set(tmpV.x, 0, -4).sub(camera.position).nor();
        // trying to set camera rotation somehow so i can move with mouse/keyboard
        rotationAxis.set(1f, 0f, 0f);
        rotationAxis.crs(camera.direction);
        camera.rotateAround(tmpV, rotationAxis, rotationDirection);
        camera.update();
    }

    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }

}
