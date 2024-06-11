package com.ball.pit;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.utils.Array;

public class BallPit extends InputAdapter implements ApplicationListener {
	protected PerspectiveCamera cam;
	protected CameraInputController camController;
	protected ModelBatch modelBatch;
	protected AssetManager assets;
	protected Array<ModelInstance> instances = new Array<ModelInstance>();
	protected Environment environment;
	protected boolean loading;
	protected Controller controller;

	@Override
	public void create () {
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(1f, 1f, 1f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 600f;
		cam.update();

		for (Controller controller : Controllers.getControllers()) {
			Gdx.app.log("TAG", controller.getName());
		}

		controller = Controllers.getControllers().get(0);
		System.out.println("axis count: " + controller.getAxisCount());

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		assets = new AssetManager();
		assets.load("halfpipe.g3db", Model.class);
		loading = true;

	}

	private void doneLoading() {
		Model halfPipe = assets.get("halfpipe.g3db", Model.class);
		ModelInstance halfPipeInstance = new ModelInstance(halfPipe);
		instances.add(halfPipeInstance);
	}

	private int visibleCount;
	@Override
	public void render () {
		if (loading && assets.update())
			doneLoading();
		camController.update();

		controller = Controllers.getCurrent();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		float leftStickX = controller.getAxis(0);
		float leftStickY = controller.getAxis(1);
		float rightStickX = controller.getAxis(2);
		float rightStickY = controller.getAxis(3);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
