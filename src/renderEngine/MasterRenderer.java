package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	public static float RED = 0.15F;
	public static float GREEN = 0.55F;
	public static float BLUE = 1.0F;
	
	private Matrix4f projectionMatrix;
	
	public MasterRenderer(){
		enableCulling();
		
		createProjectionMatrix();
		
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public static void enableCulling(){
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();

		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);

		renderer.render(entities);

		shader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		
		terrainRenderer.render(terrains);
		
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain){
		
		terrains.add(terrain);
	}
	
	public void prepare() {

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	private void createProjectionMatrix() {

		float aspectRatio = Main.WIDTH / Main.HEIGHT;
		float y = (float) (1f / Math.tan(Math.toRadians(FOV / 2f)))
				* aspectRatio;
		float x = y / aspectRatio;
		float f = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x;
		projectionMatrix.m11 = y;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / f);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / f);
		projectionMatrix.m33 = 0;
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);

		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public void cleanUp() {

		terrainShader.cleanUp();
		shader.cleanUp();
	}
}
