package renderEngine;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Light;
import entities.Player;
import guis.GuiTexture;

public class Main {
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth(), HEIGHT = (int) Toolkit
			.getDefaultToolkit().getScreenSize().getHeight();
	public static final String TITLE = "Thin Matrix";

	private static long lastFrameTime;
	private static float delta;

	public Main() {
		try {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.setTitle(TITLE);
			Display.sync(60);

			Display.create();

		} catch (LWJGLException e) {

			e.printStackTrace();
		}

		Loader loader = new Loader();

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("sandTexture"));
		TerrainTexture rTexture = new TerrainTexture(
				loader.loadTexture("sandTexture"));
		TerrainTexture gTexture = new TerrainTexture(
				loader.loadTexture("pathTexture"));
		TerrainTexture bTexture = new TerrainTexture(
				loader.loadTexture("snowTexture"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(
				loader.loadTexture("blendMap"));

		RawModel model = OBJLoader.loadObjModel("person", loader);
		ModelTexture texture = new ModelTexture(
				loader.loadTexture("playerTexture"));
		TexturedModel texModel = new TexturedModel(model, texture);
		// Entity entity = new Entity(texModel, new Vector3f(10, 0, -10), 0, 0,
		// 0,
		// 1);
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 10000, -7000), new Vector3f(1, 1, 1)));
		lights.add(new Light(new Vector3f(-200, 10, -200), new Vector3f(10, 0, 0)));
		lights.add(new Light(new Vector3f(200, 10, 200), new Vector3f(0, 0, 10)));
		
		// entity.getModel().getTexture().setHasTransparency(true);
		// entity.getModel().getTexture().setUseFakeLighting(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap,
				"heightmap");

		MasterRenderer renderer = new MasterRenderer();

		Player player = new Player(texModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		Camera camera = new Camera(player);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25F, 0.25F));
		guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(this, terrain);

			renderer.processEntity(player);
			// renderer.processEntity(entity);
			renderer.processTerrain(terrain);

			renderer.render(lights, camera);

			guiRenderer.render(guis);
			
			Display.update();
			
			long currentFrameTime = getCurrentTime();
			delta = (currentFrameTime - lastFrameTime) / 1000F;
			lastFrameTime = currentFrameTime;
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		Display.destroy();
	}

	public static void main(String[] args) {

		new Main();
	}

	public float getDelta() {

		return delta;
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
