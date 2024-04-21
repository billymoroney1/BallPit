package com.ball.pit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class BallPit extends ApplicationAdapter {
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public boolean loading;
	public Environment environment;

	// NEXT TIME pick pu from fbx-conv section of this:https://xoppa.github.io/blog/loading-models-using-libgdx/

	
	@Override
	public void create () {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(7f, 7f, 7f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

//		ModelBuilder modelBuilder = new ModelBuilder();
//		model = modelBuilder.createBox(5f, 5f, 5f,
//				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//		instance = new ModelInstance(model);

//		ModelLoader loader = new ObjLoader();
//		model = loader.loadModel(Gdx.files.internal("assets/ship.obj"));
//		instance = new ModelInstance(model);

		assets = new AssetManager();
		assets.load("assets/ship.obj", Model.class);
		loading = true;
	}

	private void doneLoading() {
		Model ship = assets.get("assets/ship.obj", Model.class);
		for (float x = -5f; x <= 5f; x += 2f) {
			for (float z = -5f; z <= 5f; z += 2f) {
				ModelInstance shipInstance = new ModelInstance(ship);
				shipInstance.transform.setToTranslation(x, 0, z);
				instances.add(shipInstance);
			}
		}
		loading = false;
	}

	@Override
	public void render () {
		if (loading && assets.update())
			doneLoading();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
}
