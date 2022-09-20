import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Mesh
{
	private List<Vertex>  vertices;
	private List<Integer> indices;
	
	public Mesh(String fileName) throws IOException
	{
		IndexedModel model = new OBJModel(fileName).ToIndexedModel();

		vertices = new ArrayList<Vertex>();
		for(int i = 0; i < model.GetPositions().size(); i++)
		{
			vertices.add(new Vertex(
						model.GetPositions().get(i),
						model.GetTexCoords().get(i),
						model.GetNormals().get(i)));
		}

		indices = model.GetIndices();
	}

	public void Draw(RenderContext context, Matrix4f viewProjection, Matrix4f transform, Bitmap texture)
	{
		Matrix4f mvp = viewProjection.Mul(transform);
		for(int i = 0; i < indices.size(); i += 3)
		{
			context.DrawTriangle(
					vertices.get(indices.get(i)).Transform(mvp, transform),
					vertices.get(indices.get(i + 1)).Transform(mvp, transform),
					vertices.get(indices.get(i + 2)).Transform(mvp, transform),
					texture);
		}
	}
}
