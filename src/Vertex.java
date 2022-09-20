
public class Vertex
{
	private Vector4f pos;
	private Vector4f texCoords;
	private Vector4f normal;

	public float GetX() { return pos.GetX(); }
	public float GetY() { return pos.GetY(); }

	public Vector4f GetPosition() { return pos; }
	public Vector4f GetTexCoords() { return texCoords; }
	public Vector4f GetNormal() { return normal; }

	public Vertex(Vector4f pos, Vector4f texCoords, Vector4f normal)
	{
		this.pos = pos;
		this.texCoords = texCoords;
		this.normal = normal;
	}

	public Vertex Transform(Matrix4f transform, Matrix4f normalTransform)
	{
		return new Vertex(transform.Transform(pos), texCoords, 
				normalTransform.Transform(normal).Normalized());
	}

	public Vertex PerspectiveDivide()
	{
		return new Vertex(new Vector4f(pos.GetX()/pos.GetW(), pos.GetY()/pos.GetW(), 
						pos.GetZ()/pos.GetW(), pos.GetW()),	
				texCoords, normal);
	}

	public float TriangleAreaTimesTwo(Vertex b, Vertex c)
	{
		float x1 = b.GetX() - pos.GetX();
		float y1 = b.GetY() - pos.GetY();

		float x2 = c.GetX() - pos.GetX();
		float y2 = c.GetY() - pos.GetY();

		return (x1 * y2 - x2 * y1);
	}

	public Vertex Lerp(Vertex other, float lerpAmt)
	{
		return new Vertex(
				pos.Lerp(other.GetPosition(), lerpAmt),
				texCoords.Lerp(other.GetTexCoords(), lerpAmt),
				normal.Lerp(other.GetNormal(), lerpAmt)
				);
	}

	public boolean IsInsideViewFrustum()
	{
		return 
			Math.abs(pos.GetX()) <= Math.abs(pos.GetW()) &&
			Math.abs(pos.GetY()) <= Math.abs(pos.GetW()) &&
			Math.abs(pos.GetZ()) <= Math.abs(pos.GetW());
	}

	public float Get(int index)
	{
		switch(index)
		{
			case 0:
				return pos.GetX();
			case 1:
				return pos.GetY();
			case 2:
				return pos.GetZ();
			case 3:
				return pos.GetW();
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	public static float Lerp(float firstFloat, float secondFloat, float by)
	{
		return firstFloat * (1 - by) + secondFloat * by;
	}

	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
}
