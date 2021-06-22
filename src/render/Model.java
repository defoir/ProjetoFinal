package render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIPropertyStore;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AITexel;
import org.lwjgl.assimp.AITexture;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.NVXGPUMemoryInfo;

import main.Principal;
import util.LogUtil;
import util.ObjConverter;
import util.Texture;
import util.Vertex;

public class Model {
	List<Texture> textures_loaded = new ArrayList<>();
	List<Mesh> meshes = new ArrayList<>();
	String diretorio;

	public Model(String path) {
		loadModel(path);
	}

	// draw all the meshes along with the model
	public void Draw(Shader shader) {
//		long past = System.currentTimeMillis();
		
		for (int i = 0; i < meshes.size(); i++) {
			meshes.get(i).Draw(shader);
		}
//		long after = System.currentTimeMillis();
//		System.out.println("Tempo para carregar o modelo:"+ (after - past)+"ms");
	}

	private void loadModel(String path) {
		Assimp.aiEnableVerboseLogging(true);
		AIPropertyStore config = Assimp.aiCreatePropertyStore();
		Assimp.aiSetImportPropertyInteger(config, Assimp.AI_CONFIG_PP_SLM_VERTEX_LIMIT, 65535);

		AIScene cena = Assimp.aiImportFile(path, Assimp.aiProcess_Triangulate 
				| Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace
                | Assimp.aiProcess_GenSmoothNormals);
		if (cena == null) {
			System.out.println("Falha ao carregar arquivo para a cena");
			return;
		}
		if(cena.mFlags() == Assimp.AI_SCENE_FLAGS_INCOMPLETE)
			System.out.println("erro na cena"+ Assimp.aiGetErrorString());
		
		diretorio = path.substring(0, path.lastIndexOf("/"));

		processNode(cena.mRootNode(), cena);
		LogUtil.memoryUsageNow();
		System.out.println("Tempo que demorou para carregar modelo:"+ (LogUtil.logTimeUsage())+ "ms");
//		int[] total_memorys = new int[1];
//		IntBuffer total_memory_buffer = ObjConverter.createBuffer(total_memorys);
//		glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX, total_memory_buffer);
//		int total_memory = total_memory_buffer.get(0);
//		System.out.println(total_memory);
//		System.out.println("Quanto está sendo utilizado no momento:" + (Principal.TOTAL_MEMORY - total_memory)+ "kb");
//		long time_now = System.currentTimeMillis();
//		System.out.println("Tempo que demorou para carregar modelo:"+ (time_now - Principal.TIME_START)+ "ms");
	}

	private void processNode(AINode node, AIScene cena) {
//		System.out.println("numero de meshes:"+cena.mNumMeshes());
//		for (int i = 0; i < cena.mNumMeshes(); i++) {
//			AIMesh mesh = AIMesh.create(cena.mMeshes().get(i));
//			meshes.add(processMesh(mesh, cena));
//		}
		for (int i = 0; i < node.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh.create(cena.mMeshes().get(node.mMeshes().get(i)));
			meshes.add(processMesh(mesh, cena));
		}
		
		PointerBuffer aiChildren = node.mChildren();
		for(int i = 0;i< node.mNumChildren();i++) {
			processNode(AINode.create(aiChildren.get(i)), cena);
		}
	}

	private Mesh processMesh(AIMesh mesh, AIScene cena) {
		List<Vertex> vertices = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		List<Texture> textures = new ArrayList<>();

		try {
			for (int i = 0; i < mesh.mNumVertices(); i++) {
				Vertex vertex = new Vertex();
				Vector3f vector = new Vector3f();
				// positions
				vector.x = mesh.mVertices().get(i).x();
				vector.y = mesh.mVertices().get(i).y();
				vector.z = mesh.mVertices().get(i).z();
				vertex.Position = new Vector3f(vector);

				// normals
				vector.x = mesh.mNormals().get(i).x();
				vector.y = mesh.mNormals().get(i).y();
				vector.z = mesh.mNormals().get(i).z();
				vertex.Normal = new Vector3f(vector);
				// texture
				AIVector3D.Buffer textCoords = mesh.mTextureCoords(0);
				int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
				if (numTextCoords > 0) {
					Vector2f vec = new Vector2f();
					vec.x = mesh.mTextureCoords(0).get(i).x();
					vec.y = mesh.mTextureCoords(0).get(i).y();
					vertex.TexCoords = new Vector2f(vec);
				} else {
					vertex.TexCoords = new Vector2f(0.0f, 0.0f);
				}
				// tangent
				vector.x = mesh.mTangents().get(i).x();
				vector.y = mesh.mTangents().get(i).y();
				vector.z = mesh.mTangents().get(i).z();
				vertex.Tangent = new Vector3f(vector);
				// bitangent
				vector.x = mesh.mBitangents().get(i).x();
				vector.y = mesh.mBitangents().get(i).y();
				vector.z = mesh.mBitangents().get(i).z();
				vertex.Bitangent = new Vector3f(vector);
				vertices.add(vertex);
			}

			// look for indices
			for (int i = 0; i < mesh.mNumFaces(); i++) {
				AIFace face = mesh.mFaces().get(i);
				// take the indices and put on arrayList indices
				for (int j = 0; j < face.mNumIndices(); j++)
					indices.add(face.mIndices().get(j));
			}

			// process materials

			AIMaterial material = AIMaterial.create(cena.mMaterials().get(mesh.mMaterialIndex()));

//		Assimp.aiTextureType
			// diffuse maps
			List<Texture> diffuseMaps = loadMaterialTextures(material, Assimp.aiTextureType_DIFFUSE, "texture_diffuse");
			textures.addAll(diffuseMaps);
			// specular maps
			List<Texture> specularMaps = loadMaterialTextures(material, Assimp.aiTextureType_SPECULAR, "texture_specular");
			textures.addAll(specularMaps);
			// normal maps
			List<Texture> normalMaps = loadMaterialTextures(material, Assimp.aiTextureType_HEIGHT, "texture_normal");
			textures.addAll(normalMaps);
			// height maps
			List<Texture> heightMaps = loadMaterialTextures(material, Assimp.aiTextureType_AMBIENT, "texture_height");
			textures.addAll(heightMaps);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Mesh(vertices, indices, textures);
	}

	private List<Texture> loadMaterialTextures(AIMaterial mat, int material, String typeName) {
		List<Texture> textures = new ArrayList<>();
		for (int i = 0; i < Assimp.aiGetMaterialTextureCount(mat, material); i++) {
			AIString str = AIString.calloc();
			Assimp.aiGetMaterialTexture(mat, material, i, str, (IntBuffer) null, null, null, null, null, null);
			boolean skip = false;
			for (int j = 0; j < textures_loaded.size(); j++) {
				if (textures_loaded.get(j).path.equals(str)) {
					textures.add(textures_loaded.get(j));
					skip = true;
					break;
				}
			}
			if (!skip) {
				Texture texture = new Texture();
				texture.id = TextureFromFile(str.dataString(), this.diretorio);
				texture.type = typeName;
				texture.path = str;
				textures.add(texture);
				textures_loaded.add(texture);
			}
		}
		return textures;
	}

	private int TextureFromFile(String path, String diretorio) {
		String filename = path;
		filename = diretorio + "/" + filename;

		int textureID;
		textureID = glGenTextures();

		IntBuffer bufferwidth = BufferUtils.createIntBuffer(1);
		IntBuffer bufferheight = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		ByteBuffer data = stbi_load(filename, bufferwidth, bufferheight, comp, 4);

		int width = bufferwidth.get();
		int height = bufferheight.get();

		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		stbi_image_free(data);

		return textureID;
	}
}
