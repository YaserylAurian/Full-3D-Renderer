import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Display display = new Display(800, 600, "3D Software Rendering");
		RenderContext target = display.GetFrameBuffer();

		Bitmap texture = new Bitmap("./res/bricks2.jpg");
		Mesh terrainMesh = new Mesh("./res/cs_collider.obj");
		Transform terrainTransform = new Transform(new Vector4f(0,0,0.0f));

		Camera camera = new Camera(new Matrix4f().InitPerspective((float)Math.toRadians(70.0f),
					   	(float)target.GetWidth()/(float)target.GetHeight(), 0.1f, 1000.0f));

		long previousTime = System.nanoTime();
		while(true)
		{
			long currentTime = System.nanoTime();
			float delta = (float)((currentTime - previousTime)/1000000000.0);
			previousTime = currentTime;

			camera.Update(display.GetInput(), delta);
			Matrix4f vp = camera.GetViewProjection();

			target.Clear((byte)0x00);
			target.ClearDepthBuffer();
			terrainMesh.Draw(target, vp, terrainTransform.GetTransformation(), texture);

			display.SwapBuffers();
		}
	}
}
