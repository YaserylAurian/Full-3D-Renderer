import java.util.ArrayList;

public class Gradients
{
	private final ArrayList<float[]> interVals;
	private final ArrayList<Float> stepVals;
	
	public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert)
	{
		float oneOverdX = 1.0f /
			(((midYVert.GetX() - maxYVert.GetX()) *
			(minYVert.GetY() - maxYVert.GetY())) -
			((minYVert.GetX() - maxYVert.GetX()) *
			(midYVert.GetY() - maxYVert.GetY())));

		interVals = new ArrayList<>();
		stepVals = new ArrayList<>();
		setUpInterVals(minYVert, midYVert,maxYVert);
		setUpStepVals(minYVert, midYVert,maxYVert,oneOverdX);
	}

	private void setUpInterVals(Vertex minYVert,Vertex midYVert,Vertex maxYVert)
	{
		float[] texCoordX = new float[3];
		float[] texCoordY = new float[3];
		float[] oneOverZ  = new float[3];
		float[] depth     = new float[3];
		float[] lightAmt  = new float[3];

		// Note that the W component is the perspective Z value;
		// The Z component is the occlusion Z value

		oneOverZ[0] = 1.0f/minYVert.GetPosition().GetW();
		oneOverZ[1] = 1.0f/midYVert.GetPosition().GetW();
		oneOverZ[2] = 1.0f/maxYVert.GetPosition().GetW();

		texCoordX[0] = minYVert.GetTexCoords().GetX() * oneOverZ[0];
		texCoordX[1] = midYVert.GetTexCoords().GetX() * oneOverZ[1];
		texCoordX[2] = maxYVert.GetTexCoords().GetX() * oneOverZ[2];
		
		texCoordY[0] = minYVert.GetTexCoords().GetY() * oneOverZ[0];
		texCoordY[1] = midYVert.GetTexCoords().GetY() * oneOverZ[1];
		texCoordY[2] = maxYVert.GetTexCoords().GetY() * oneOverZ[2];

		depth[0] = minYVert.GetPosition().GetZ();
		depth[1] = midYVert.GetPosition().GetZ();
		depth[2] = maxYVert.GetPosition().GetZ();

		Vector4f lightDir = new Vector4f(0,0,1);
		lightAmt[0] = Saturate(minYVert.GetNormal().Dot(lightDir)) * 0.9f + 0.1f;
		lightAmt[1] = Saturate(midYVert.GetNormal().Dot(lightDir)) * 0.9f + 0.1f;
		lightAmt[2] = Saturate(maxYVert.GetNormal().Dot(lightDir)) * 0.9f + 0.1f;

		interVals.add(texCoordX);
		interVals.add(texCoordY);
		interVals.add(oneOverZ);
		interVals.add(depth);
		interVals.add(lightAmt);
	}

	private void setUpStepVals(Vertex minYVert,Vertex midYVert,Vertex maxYVert, float oneOverdx)
	{
		float oneOverdy = -oneOverdx;

		for (float[] arr: interVals)
		{
			float stepX = CalcXStep(arr, minYVert, midYVert, maxYVert, oneOverdx);
			float stepY = CalcYStep(arr, minYVert, midYVert, maxYVert, oneOverdy);

			stepVals.add(stepX);
			stepVals.add(stepY);
		}
	}

	private float CalcXStep(float[] values, Vertex minYVert, Vertex midYVert,
							Vertex maxYVert, float oneOverdX)
	{
		return
				(((values[1] - values[2]) *
				(minYVert.GetY() - maxYVert.GetY())) -
				((values[0] - values[2]) *
				(midYVert.GetY() - maxYVert.GetY()))) * oneOverdX;
	}

	private float CalcYStep(float[] values, Vertex minYVert, Vertex midYVert,
							Vertex maxYVert, float oneOverdY)
	{
		return
				(((values[1] - values[2]) *
				(minYVert.GetX() - maxYVert.GetX())) -
				((values[0] - values[2]) *
				(midYVert.GetX() - maxYVert.GetX()))) * oneOverdY;
	}

	private float Saturate(float val)
	{
		if(val > 1.0f)
		{
			return 1.0f;
		}
		return Math.max(val, 0.0f);
	}

	//region Getters
	public ArrayList<float[]> getInterVals()
	{
		return interVals;
	}
	public ArrayList<Float> getStepVals() {
		return stepVals;
	}

	public float getTexCoordX(int loc) { return interVals.get(0)[loc]; }
	public float getTexCoordY(int loc) { return interVals.get(1)[loc]; }
	public float getOneOverZ(int loc)  { return interVals.get(2)[loc]; }
	public float getDepth(int loc)     { return interVals.get(3)[loc]; }
	public float getLightAmt(int loc)  { return interVals.get(4)[loc]; }

	public float getTexCoordXXStep() { return stepVals.get(0); }
	public float getTexCoordXYStep() { return stepVals.get(1); }
	public float getTexCoordYXStep() { return stepVals.get(2); }
	public float getTexCoordYYStep() { return stepVals.get(3); }
	public float getOneOverZXStep()  { return stepVals.get(4); }
	public float getOneOverZYStep()  { return stepVals.get(5); }
	public float getDepthXStep()     { return stepVals.get(6); }
	public float getDepthYStep()     { return stepVals.get(7); }
	public float getLightAmtXStep()  { return stepVals.get(8); }
	public float getLightAmtYStep()  { return stepVals.get(9); }
	//endregion
}
