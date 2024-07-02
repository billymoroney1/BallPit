package com.ball.pit;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.ball.pit.screens.BallPitScreen;
import com.ball.pit.screens.GameLoop;
import com.ball.pit.screens.GameOver;
import com.ball.pit.screens.MainMenu;
import com.ball.pit.simulation.Collision;

public class BallPit extends Game {
	protected PerspectiveCamera cam;
	protected CameraInputController camController;
//	protected ModelBatch modelBatch;
//	protected AssetManager assets;
	protected Array<ModelInstance> instances = new Array<ModelInstance>();
	protected Environment environment;
	protected boolean loading;
	protected Controller controller;
	protected Node ball;
	protected ModelInstance pitBallInstance;

	private Music music;
	private FPSLogger fps;

	private ControllerAdapter controllerListener = new ControllerAdapter(){
		@Override
		public void connected(Controller c) {
			if (controller == null) {
				controller = c;
			}
		}
		@Override
		public void disconnected(Controller c) {
			if (controller == c) {
				controller = null;
			}
		}
	};

	public Controller getController(){ return controller;}

	@Override
	public void create () {
		//COMMENTS ARE FROM BLOG TUTORIAL PATTERN
//		modelBatch = new ModelBatch();
//		environment = new Environment();
//		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
//		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//
//		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		cam.position.set(1f, 1f, 1f);
//		cam.lookAt(0,0,0);
//		cam.near = 1f;
//		cam.far = 600f;
//		cam.update();
//
//		for (Controller controller : Controllers.getControllers()) {
//			Gdx.app.log("TAG", controller.getName());
//		}
//
//		if(Controllers.getControllers().size > 0){
//			controller = Controllers.getControllers().get(0);
//			System.out.println("axis count: " + controller.getAxisCount());
//		}
//
//		camController = new CameraInputController(cam);
//		Gdx.input.setInputProcessor(camController);
//
//		assets = new AssetManager();
//		assets.load("halfpipe.g3db", Model.class);
//		assets.load("pitball.g3db", Model.class);
//		loading = true;

		Array<Controller> controllers = Controllers.getControllers();
		if (controllers.size > 0) {
			controller = controllers.first();
		}

		Controllers.addListener(controllerListener);

		setScreen(new MainMenu(this));
		//set music here

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyUp (int keycode) {
				if (keycode == Input.Keys.ENTER && Gdx.app.getType() == Application.ApplicationType.WebGL) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				}
				return true;
			}
		});
		fps = new FPSLogger();

		Bullet.init();
	}

//	private void doneLoading() {
//		Model halfPipe = assets.get("halfpipe.g3db", Model.class);
//		ModelInstance halfPipeInstance = new ModelInstance(halfPipe);
//		instances.add(halfPipeInstance);
//
//		Model ballModel = assets.get("pitball.g3db", Model.class);
//		pitBallInstance = new ModelInstance(ballModel);
//		instances.add(pitBallInstance);
//
//		ball = pitBallInstance.getNode("Sphere");
//		// discovered that the translation of the ball and teh halfpipe are 0, so i suspect the halfpipe is off center
//		// need to make a scene with the 2 models
//	}

	private int visibleCount;
	@Override
	public void render () {

		// COMMENTS ARE OLD WAY
//		if (loading && assets.update())
//			doneLoading();
//		camController.update();
//
//		controller = Controllers.getCurrent();
//
//		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//
//		if (controller != null){
//			float leftStickX = controller.getAxis(0);
//			float leftStickY = controller.getAxis(1);
//			float rightStickX = controller.getAxis(2);
//			float rightStickY = controller.getAxis(3);
//		}
//
//		if (ball != null && pitBallInstance != null){
//			ball.translation.set(0f, ball.translation.y - 0.01f, 0f);
//			pitBallInstance.calculateTransforms();
//
//			System.out.println(">>>> ball.translation :: " + ball.translation.toString());
//		}
//
//		modelBatch.begin(cam);
//		modelBatch.render(instances, environment);
//		modelBatch.end();

		BallPitScreen currentScreen = getScreen();

		currentScreen.render(Gdx.graphics.getDeltaTime());

		if (currentScreen.isDone()) {
			currentScreen.dispose();

			if (currentScreen instanceof MainMenu) {
				setScreen(new GameLoop(this));
			} else {
				if (currentScreen instanceof GameLoop) {
					setScreen(new GameOver(this));
				} else if (currentScreen instanceof GameOver) {
					setScreen(new MainMenu(this));
				}
			}
		}
	}

	@Override
	public void dispose () {
//		modelBatch.dispose();
		instances.clear();
//		assets.dispose();
		Collision.disposeObjects();
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

	@Override
	public BallPitScreen getScreen () { return (BallPitScreen)super.getScreen(); }
}
