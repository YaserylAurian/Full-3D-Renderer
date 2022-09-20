public class Transform
{
	private Vector4f   pos;
	private Quaternion rot;
	private Vector4f   scale;

	public Transform()
	{
		this(new Vector4f(0,0,0,0));
	}

	public Transform(Vector4f pos)
	{
		this(pos, new Quaternion(0,0,0,1), new Vector4f(1,1,1,1));
	}

	public Transform(Vector4f pos, Quaternion rot, Vector4f scale)
	{
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
	}

	public Transform SetPos(Vector4f pos)
	{
		return new Transform(pos, rot, scale);
	}

	public Transform Rotate(Quaternion rotation)
	{
		return new Transform(pos, rotation.Mul(rot).Normalized(), scale);
	}

	public Transform LookAt(Vector4f point, Vector4f up)
	{
		return Rotate(GetLookAtRotation(point, up));
	}

	public Quaternion GetLookAtRotation(Vector4f point, Vector4f up)
	{
		return new Quaternion(new Matrix4f().InitRotation(point.Sub(pos).Normalized(), up));
	}

	public Matrix4f GetTransformation()
	{
		Matrix4f translationMatrix = new Matrix4f().InitTranslation(pos.GetX(), pos.GetY(), pos.GetZ());
		Matrix4f rotationMatrix = rot.ToRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().InitScale(scale.GetX(), scale.GetY(), scale.GetZ());

		return translationMatrix.Mul(rotationMatrix.Mul(scaleMatrix));
	}

	public Vector4f GetTransformedPos()
	{
		return pos;
	}

	public Quaternion GetTransformedRot()
	{
		return rot;
	}

	public Vector4f GetPos()
	{
		return pos;
	}

	public Quaternion GetRot()
	{
		return rot;
	}

	public Vector4f GetScale()
	{
		return scale;
	}
}
