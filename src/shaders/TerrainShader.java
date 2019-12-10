package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import entities.Camera;
import entities.Light;

public class TerrainShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private static final String FRAGMENT_FILE = "src/shaders/FragmentShaderTerrain.txt";
	private static final String VERTEX_FILE = "src/shaders/VertexShaderTerrain.txt";

	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightColor[];
	private int locationLightPosition[];
	private int locationSkyColor;
	private int locationbackgroundTexture;
	private int locationrTexture;
	private int locationgTexture;
	private int locationbTexture;
	private int locationBlendMap;

	public TerrainShader() {

		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void bindAttributes() {

		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	protected void getAllUniformLocations() {

		locationTransformationMatrix = super
				.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationSkyColor = super.getUniformLocation("skyColor");
		locationbackgroundTexture = super
				.getUniformLocation("backgroundTexture");
		locationrTexture = super.getUniformLocation("rTexture");
		locationgTexture = super.getUniformLocation("gTexture");
		locationbTexture = super.getUniformLocation("bTexture");
		locationBlendMap = super.getUniformLocation("blendMap");

		locationLightColor = new int[MAX_LIGHTS];
		locationLightPosition = new int[MAX_LIGHTS];
		
		for (int i = 0; i < MAX_LIGHTS; i++) {

			locationLightColor[i] = super.getUniformLocation("lightColor[" + i
					+ "]");
			locationLightPosition[i] = super
					.getUniformLocation("lightPosition[" + i + "]");
		}
	}

	public void connectTextureUnits() {
		super.loadInt(locationbackgroundTexture, 0);
		super.loadInt(locationrTexture, 1);
		super.loadInt(locationgTexture, 2);
		super.loadInt(locationbTexture, 3);
		super.loadInt(locationBlendMap, 4);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {

		super.loadMatrix(locationTransformationMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {

		super.loadMatrix(locationProjectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}

	public void loadLights(List<Light> lights) {

		for (int i = 0; i < MAX_LIGHTS; i++) {
			if(i<lights.size()){
				
				super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
				super.loadVector(locationLightColor[i], lights.get(i).getColor());
			}else{
				
				super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(locationLightColor[i], new Vector3f(0, 0, 0));
			}
			
		}
	}

	public void loadSkyColor(float r, float g, float b) {

		super.loadVector(locationSkyColor, new Vector3f(r, g, b));
	}
}
