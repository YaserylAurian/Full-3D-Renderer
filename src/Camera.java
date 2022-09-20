import java.awt.event.KeyEvent;

public class Camera
{
	private static final Vector4f Y_AXIS = new Vector4f(0,1,0);

	private Transform transform;
	private Matrix4f projection;

	private Transform GetTransform()
	{
		return transform;
	}

	public Camera(Matrix4f projection)
	{
		this.projection = projection;
		this.transform = new Transform();
	}

	public Matrix4f GetViewProjection()
	{
		Matrix4f cameraRotation = GetTransform().GetTransformedRot().Conjugate().ToRotationMatrix();
		Vector4f cameraPos = GetTransform().GetTransformedPos().Mul(-1);

		Matrix4f cameraTranslation = new Matrix4f().InitTranslation(cameraPos.GetX(), cameraPos.GetY(), cameraPos.GetZ());

		return projection.Mul(cameraRotation.Mul(cameraTranslation));
	}

	public void Update(Input input, float delta)
	{
		final float sensitivityX = 2.66f * delta;
		final float sensitivityY = 2.0f * delta;
		final float movAmt = 5.0f * delta;

		if(input.GetKey(KeyEvent.VK_W))
			Move(GetTransform().GetRot().GetForward(), movAmt);
		if(input.GetKey(KeyEvent.VK_S))
			Move(GetTransform().GetRot().GetForward(), -movAmt);
		if(input.GetKey(KeyEvent.VK_A))
			Move(GetTransform().GetRot().GetLeft(), movAmt);
		if(input.GetKey(KeyEvent.VK_D))
			Move(GetTransform().GetRot().GetRight(), movAmt);
		
		if(input.GetKey(KeyEvent.VK_RIGHT))
			Rotate(Y_AXIS, sensitivityX);
		if(input.GetKey(KeyEvent.VK_LEFT))
			Rotate(Y_AXIS, -sensitivityX);
		if(input.GetKey(KeyEvent.VK_DOWN))
			Rotate(GetTransform().GetRot().GetRight(), sensitivityY);
		if(input.GetKey(KeyEvent.VK_UP))
			Rotate(GetTransform().GetRot().GetRight(), -sensitivityY);
	}

	private void Move(Vector4f dir, float amt)
	{
		transform = GetTransform().SetPos(GetTransform().GetPos().Add(dir.Mul(amt)));
	}

	private void Rotate(Vector4f axis, float angle)
	{
		transform = GetTransform().Rotate(new Quaternion(axis, angle));
	}
}
