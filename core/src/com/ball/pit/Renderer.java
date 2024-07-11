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
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.ball.pit.simulation.Ball;
import com.ball.pit.simulation.Collision;
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
    private FirstPersonCameraController cameraController;
    private Vector3 rotationAxis = new Vector3();
    private float rotationDirection;

    private String status = "";

    // what is the value of initializing objects that are reused? speed?
    final Vector3 tmpV = new Vector3();

    ModelBatch modelBatch;

    Environment lights;

    // Camera
    private float distanceFromPlayer = 20f;
    private float camPitch = 20f;
    private float angleAroundPlayer = 0f;
    private float angleBehindPlayer = 0f;

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
//            camera.near = 1f;
//            camera.far = 200;
            camera.position.set(0, 0, 4f);

            cameraController = new FirstPersonCameraController(camera);
            Gdx.input.setInputProcessor(cameraController);
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

        modelBatch.begin(camera);
        modelBatch.render(simulation.ball);
        modelBatch.render(simulation.stage);
//        modelBatch.render(simulation.ground);
//        modelBatch.render(simulation.testBall);
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

        float horDistance = calculateHorizontalDistance(distanceFromPlayer);
        float vertDistance = calculateVerticalDistance(distanceFromPlayer);

        ball.transform.getTranslation(tmpV);

        calculatePitch();
        calculateAngleAroundBall();
        calculateCameraPosition(tmpV, horDistance, vertDistance);

        camera.up.set(Vector3.Y);

        camera.lookAt(tmpV);
        camera.update();
    }

    private void calculateCameraPosition(Vector3 currentPosition, float horDistance, float vertDistance) {
        float offsetX = (float) (horDistance * Math.sin(Math.toRadians(angleAroundPlayer)));
        float offsetZ = (float) (horDistance * Math.cos(Math.toRadians(angleAroundPlayer)));

        camera.position.x = currentPosition.x - offsetX;
        camera.position.z = currentPosition.z - offsetZ;
        camera.position.y = currentPosition.y + vertDistance;
    }

    private void calculateAngleAroundBall() {
        // some logic here if i want free look cam
        angleAroundPlayer = angleBehindPlayer;
    }

    private void calculatePitch() {
        float pitchChange = -Gdx.input.getDeltaY() * 0.3f;
        camPitch -= pitchChange;

        // min and max pitch
    }

    public void rotateCameraLeft(float delta){
        angleBehindPlayer -= 40f * delta;
    }

    public void rotateCameraRight(float delta) {
        angleBehindPlayer += 40f * delta;
    }

    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }

    private float calculateHorizontalDistance(float distanceFromPlayer){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
    }

    private float calculateVerticalDistance(float distanceFromPlayer){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
    }
}
