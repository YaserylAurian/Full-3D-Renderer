import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.image.DataBufferByte;
import javax.swing.JFrame;

public class Display extends Canvas
{
	private final JFrame         frame;
	private final RenderContext  frameBuffer;
	private final BufferedImage  displayImage;
	private final byte[]         displayComponents;
	private final BufferStrategy bufferStrategy;
	private final Graphics       graphics;

	private final Input          input;

	public RenderContext GetFrameBuffer() { return frameBuffer; }
	public Input GetInput() { return input; }

	public Display(int width, int height, String title)
	{
		frame = new JFrame();
		initWindow(title,width,height);

		frameBuffer = new RenderContext(width, height);
		displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		displayComponents = 
			((DataBufferByte)displayImage.getRaster().getDataBuffer()).getData();

		frameBuffer.Clear((byte)0x80);
		frameBuffer.DrawPixel(100, 100, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF);

		createBufferStrategy(1);
		bufferStrategy = getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();

		input = new Input();
		initInputs();
	}

	public void initWindow(String title, int width, int height)
	{
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle(title);
		frame.setSize(width, height);
		frame.setVisible(true);
	}

	public void initInputs()
	{
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		setFocusable(true);
		requestFocus();
	}

	public void SwapBuffers()
	{

		frameBuffer.CopyToByteArray(displayComponents);
		graphics.drawImage(displayImage, 0, 0, 
			frameBuffer.GetWidth(), frameBuffer.GetHeight(), null);
		bufferStrategy.show();
	}
}
