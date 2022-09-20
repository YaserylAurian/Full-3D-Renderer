import java.util.ArrayList;

public class Edge
{
	public ArrayList<Float>      interVals;
	public ArrayList<Float>      stepVals;

	private float                x;
	private float                xStep;
	private int                  yStart;
	private int                  yEnd;

	public Edge(Gradients gradients, Vertex minYVert, Vertex maxYVert, int minYVertIndex)
	{
		interVals = new ArrayList<>();
		stepVals = new ArrayList<>();
		setUp(gradients, minYVert, maxYVert, minYVertIndex);
	}

	private void setUp(Gradients gradients, Vertex minYVert, Vertex maxYVert, int minYVertIndex)
	{
		yStart = (int)Math.ceil(minYVert.GetY());
		yEnd = (int)Math.ceil(maxYVert.GetY());

		float yDist = maxYVert.GetY() - minYVert.GetY();
		float xDist = maxYVert.GetX() - minYVert.GetX();

		float yPrestep = yStart - minYVert.GetY();
		xStep = xDist/yDist;
		x = minYVert.GetX() + yPrestep * xStep;
		float xPrestep = x - minYVert.GetX();

		int counter = 0;
		ArrayList<Float> list = gradients.getStepVals();
		for (float[] arr: gradients.getInterVals())
		{
			float val = arr[minYVertIndex] +
						list.get(counter) * xPrestep +
						list.get(counter+1) * yPrestep;
			float step = list.get(counter+1) + list.get(counter) * xStep;

			interVals.add(val);
			stepVals.add(step);

			counter += 2;
		}
	}
	public void Step()
	{
		x += xStep;
		interVals.set(0, interVals.get(0) + stepVals.get(0));
		interVals.set(1, interVals.get(1) + stepVals.get(1));
		interVals.set(2, interVals.get(2) + stepVals.get(2));
		interVals.set(3, interVals.get(3) + stepVals.get(3));
		interVals.set(4, interVals.get(4) + stepVals.get(4));
	}

	//region Getters
	public float getX()          { return x; }
	public int   getYStart()     { return yStart; }
	public int   getYEnd()       { return yEnd; }


	public float getTexCoordX()  { return interVals.get(0); }
	public float getTexCoordY()  { return interVals.get(1); }
	public float getOneOverZ()   { return interVals.get(2); }
	public float getDepth()      { return interVals.get(3); }
	public float getLightAmt()   { return interVals.get(4); }
	//endregion
}
