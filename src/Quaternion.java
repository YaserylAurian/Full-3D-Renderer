public class Quaternion
{
	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(Vector4f axis, float angle)
	{
		float sinHalfAngle = (float)Math.sin(angle / 2);
		float cosHalfAngle = (float)Math.cos(angle / 2);

		this.x = axis.GetX() * sinHalfAngle;
		this.y = axis.GetY() * sinHalfAngle;
		this.z = axis.GetZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}

	public float Length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion Normalized()
	{
		float length = Length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	public Quaternion Conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion Mul(float r)
	{
		return new Quaternion(x * r, y * r, z * r, w * r);
	}

	public Quaternion Mul(Quaternion r)
	{
		float w_ = w * r.GetW() - x * r.GetX() - y * r.GetY() - z * r.GetZ();
		float x_ = x * r.GetW() + w * r.GetX() + y * r.GetZ() - z * r.GetY();
		float y_ = y * r.GetW() + w * r.GetY() + z * r.GetX() - x * r.GetZ();
		float z_ = z * r.GetW() + w * r.GetZ() + x * r.GetY() - y * r.GetX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion Mul(Vector4f r)
	{
		float w_ = -x * r.GetX() - y * r.GetY() - z * r.GetZ();
		float x_ =  w * r.GetX() + y * r.GetZ() - z * r.GetY();
		float y_ =  w * r.GetY() + z * r.GetX() - x * r.GetZ();
		float z_ =  w * r.GetZ() + x * r.GetY() - y * r.GetX();
		
		return new Quaternion(x_, y_, z_, w_);
	}

	public Quaternion Sub(Quaternion r)
	{
		return new Quaternion(x - r.GetX(), y - r.GetY(), z - r.GetZ(), w - r.GetW());
	}

	public Quaternion Add(Quaternion r)
	{
		return new Quaternion(x + r.GetX(), y + r.GetY(), z + r.GetZ(), w + r.GetW());
	}

	public Matrix4f ToRotationMatrix()
	{
		Vector4f forward =  new Vector4f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
		Vector4f up = new Vector4f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
		Vector4f right = new Vector4f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

		return new Matrix4f().InitRotation(forward, up, right);
	}

	public float Dot(Quaternion r)
	{
		return x * r.GetX() + y * r.GetY() + z * r.GetZ() + w * r.GetW();
	}

	public Quaternion NLerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		Quaternion correctedDest = dest;

		if(shortest && this.Dot(dest) < 0)
			correctedDest = new Quaternion(-dest.GetX(), -dest.GetY(), -dest.GetZ(), -dest.GetW());

		return correctedDest.Sub(this).Mul(lerpFactor).Add(this).Normalized();
	}

	public Quaternion SLerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		final float EPSILON = 1e3f;

		float cos = this.Dot(dest);
		Quaternion correctedDest = dest;

		if(shortest && cos < 0)
		{
			cos = -cos;
			correctedDest = new Quaternion(-dest.GetX(), -dest.GetY(), -dest.GetZ(), -dest.GetW());
		}

		if(Math.abs(cos) >= 1 - EPSILON)
			return NLerp(correctedDest, lerpFactor, false);

		float sin = (float)Math.sqrt(1.0f - cos * cos);
		float angle = (float)Math.atan2(sin, cos);
		float invSin =  1.0f/sin;

		float srcFactor = (float)Math.sin((1.0f - lerpFactor) * angle) * invSin;
		float destFactor = (float)Math.sin((lerpFactor) * angle) * invSin;

		return this.Mul(srcFactor).Add(correctedDest.Mul(destFactor));
	}

	//From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
	public Quaternion(Matrix4f rot)
	{
		float trace = rot.Get(0, 0) + rot.Get(1, 1) + rot.Get(2, 2);

		if(trace > 0)
		{
			float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
			w = 0.25f / s;
			x = (rot.Get(1, 2) - rot.Get(2, 1)) * s;
			y = (rot.Get(2, 0) - rot.Get(0, 2)) * s;
			z = (rot.Get(0, 1) - rot.Get(1, 0)) * s;
		}
		else
		{
			if(rot.Get(0, 0) > rot.Get(1, 1) && rot.Get(0, 0) > rot.Get(2, 2))
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.Get(0, 0) - rot.Get(1, 1) - rot.Get(2, 2));
				w = (rot.Get(1, 2) - rot.Get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.Get(1, 0) + rot.Get(0, 1)) / s;
				z = (rot.Get(2, 0) + rot.Get(0, 2)) / s;
			}
			else if(rot.Get(1, 1) > rot.Get(2, 2))
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.Get(1, 1) - rot.Get(0, 0) - rot.Get(2, 2));
				w = (rot.Get(2, 0) - rot.Get(0, 2)) / s;
				x = (rot.Get(1, 0) + rot.Get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.Get(2, 1) + rot.Get(1, 2)) / s;
			}
			else
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.Get(2, 2) - rot.Get(0, 0) - rot.Get(1, 1));
				w = (rot.Get(0, 1) - rot.Get(1, 0) ) / s;
				x = (rot.Get(2, 0) + rot.Get(0, 2) ) / s;
				y = (rot.Get(1, 2) + rot.Get(2, 1) ) / s;
				z = 0.25f * s;
			}
		}

		float length = (float)Math.sqrt(x * x + y * y + z * z + w * w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}

	public Vector4f GetForward()
	{
		return new Vector4f(0,0,1,1).Rotate(this);
	}

	public Vector4f GetBack()
	{
		return new Vector4f(0,0,-1,1).Rotate(this);
	}

	public Vector4f GetUp()
	{
		return new Vector4f(0,1,0,1).Rotate(this);
	}

	public Vector4f GetDown()
	{
		return new Vector4f(0,-1,0,1).Rotate(this);
	}

	public Vector4f GetRight()
	{
		return new Vector4f(1,0,0,1).Rotate(this);
	}

	public Vector4f GetLeft()
	{
		return new Vector4f(-1,0,0,1).Rotate(this);
	}
	
	public float GetX()
	{
		return x;
	}

	public float GetY()
	{
		return y;
	}

	public float GetZ()
	{
		return z;
	}

	public float GetW()
	{
		return w;
	}

	public boolean equals(Quaternion r)
	{
		return x == r.GetX() && y == r.GetY() && z == r.GetZ() && w == r.GetW();
	}
}
