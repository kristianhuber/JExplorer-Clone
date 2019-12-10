package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import entities.Camera;
import entities.Light;

public class StaticShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private static final String FRAGMENT_FILE = "src/shaders/FragmentShader.txt";
	private static final String VERTEX_FILE = "src/shaders/VertexShader.txt";

	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightColor[];
	private int locationLightPosition[];
	private int locationUseFakeLighting;
	private int locationSkyColor;

	public StaticShader() {

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
		locationUseFakeLighting = super.getUniformLocation("useFakeLighting");
		locationSkyColor = super.getUniformLocation("skyColor");

		locationLightPosition = new int[MAX_LIGHTS];
		locationLightColor = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {

			locationLightPosition[i] = super
					.getUniformLocation("lightPosition[" + i + "]");
			locationLightColor[i] = super.getUniformLocation("lightColor[" + i
					+ "]");
		}
	}

	public void loadFakeLightingVariable(boolean useFake) {
		super.loadBoolean(locationUseFakeLighting, useFake);
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

			if (i < lights.size()) {
				super.loadVector(locationLightPosition[i], lights.get(i)
						.getPosition());
				super.loadVector(locationLightPosition[i], lights.get(i)
						.getColor());
			} else {

				super.loadVector(locationLightPosition[i],
						new Vector3f(0, 0, 0));
				super.loadVector(locationLightPosition[i],
						new Vector3f(0, 0, 0));
			}
		}
	}

	public void loadSkyColor(float r, float g, float b) {

		super.loadVector(locationSkyColor, new Vector3f(r, g, b));
	}
}
