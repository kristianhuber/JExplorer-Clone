package textures;

public class ModelTexture {

	private int textureID;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	public ModelTexture(int id) {

		this.textureID = id;
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getTextureID() {

		return textureID;
	}
}
