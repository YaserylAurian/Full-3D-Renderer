import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bitmap
{
	private final int  width;
	private final int  height;
	private final byte components[];

	public int GetWidth() { return width; }
	public int GetHeight() { return height; }
	public byte GetComponent(int index) { return components[index]; }

	public Bitmap(int width, int height)
	{
		this.width      = width;
		this.height     = height;
		components = new byte[width * height * 4];
	}

	public Bitmap(String fileName) throws IOException
	{
		int width = 0;
		int height = 0;
		byte[] components = null;

		BufferedImage image = ImageIO.read(new File(fileName));

		width = image.getWidth();
		height = image.getHeight();

		int imgPixels[] = new int[width * height];
		image.getRGB(0, 0, width, height, imgPixels, 0, width);
		components = new byte[width * height * 4];

		for(int i = 0; i < width * height; i++)
		{
			int pixel = imgPixels[i];

			components[i * 4]     = (byte)((pixel >> 24) & 0xFF); // A
			components[i * 4 + 1] = (byte)((pixel      ) & 0xFF); // B
			components[i * 4 + 2] = (byte)((pixel >> 8 ) & 0xFF); // G
			components[i * 4 + 3] = (byte)((pixel >> 16) & 0xFF); // R
		}

		this.width = width;
		this.height = height;
		this.components = components;
	}

	public void Clear(byte shade)
	{
		Arrays.fill(components, shade);
	}
	public void DrawPixel(int x, int y, byte a, byte b, byte g, byte r)
	{
		int index = (x + y * width) * 4;
		components[index    ] = a;
		components[index + 1] = b;
		components[index + 2] = g;
		components[index + 3] = r;
	}

	public void CopyPixel(int destX, int destY, int srcX, int srcY, Bitmap src, float lightAmt)
	{
		int destIndex = (destX + destY * width) * 4;
		int srcIndex = (srcX + srcY * src.GetWidth()) * 4;
		srcIndex = (int)Vertex.clamp(srcIndex, 1, 1048572);

		components[destIndex    ] = (byte)((src.GetComponent(srcIndex) & 0xFF) * lightAmt);
		components[destIndex + 1] = (byte)((src.GetComponent(srcIndex + 1) & 0xFF) * lightAmt);
		components[destIndex + 2] = (byte)((src.GetComponent(srcIndex + 2) & 0xFF) * lightAmt);
		components[destIndex + 3] = (byte)((src.GetComponent(srcIndex + 3) & 0xFF) * lightAmt);
	}

	public void CopyToByteArray(byte[] dest)
	{
		for(int i = 0; i < width * height; i++)
		{
			dest[i * 3    ] = components[i * 4 + 1];
			dest[i * 3 + 1] = components[i * 4 + 2];
			dest[i * 3 + 2] = components[i * 4 + 3];
		}
	}
}
