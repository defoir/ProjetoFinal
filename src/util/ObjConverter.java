package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public abstract class ObjConverter {
	
	public static int[] convertToSimpleIndices(List<Integer> indices) {
		int[] simpleIndices = new int[indices.size()];
		for(int i=0;i<indices.size();i++) {
			simpleIndices[i] = indices.get(i);
		}
		return simpleIndices;
	}
	
	public static float[] convertToGiantFloat(List<Vertex> vertices) {
		float[] allIn = new float[vertices.size() * 14];
		int j = 0;
		for(int i=0;i<vertices.size();i++) {
			//position
			allIn[j++] = vertices.get(i).Position.x;
			allIn[j++] = vertices.get(i).Position.y;
			allIn[j++] = vertices.get(i).Position.z;
			//textura
			allIn[j++] = vertices.get(i).TexCoords.x;
			allIn[j++] = vertices.get(i).TexCoords.y;
			//normal
			allIn[j++] = vertices.get(i).Normal.x;
			allIn[j++] = vertices.get(i).Normal.y;
			allIn[j++] = vertices.get(i).Normal.z;
			//tangent
			allIn[j++] = vertices.get(i).Tangent.x;
			allIn[j++] = vertices.get(i).Tangent.y;
			allIn[j++] = vertices.get(i).Tangent.z;
			//bitangent
			allIn[j++] = vertices.get(i).Bitangent.x;
			allIn[j++] = vertices.get(i).Bitangent.y;
			allIn[j++] = vertices.get(i).Bitangent.z;
		}
		return allIn;
	}
	
	public static float[] convertVertexPositionToFloat(List<Vertex> vertices) {
		float[] posicoes = new float[vertices.size() * 3];
		int j = 0;
		for(int i=0;i<vertices.size();i++) {
			posicoes[j++] = vertices.get(i).Position.x;
			posicoes[j++] = vertices.get(i).Position.y;
			posicoes[j++] = vertices.get(i).Position.z;
		}
		
		return posicoes;
	}
	
	public static float[] convertVertexNormalToFloat(List<Vertex> vertices) {
		float[] normal = new float[vertices.size() * 3];
		int j = 0;
		for(int i=0;i<vertices.size();i++) {
			normal[j++] = vertices.get(i).Normal.x;
			normal[j++] = vertices.get(i).Normal.y;
			normal[j++] = vertices.get(i).Normal.z;
		}
		return normal;
	}
	
	public static float[] convertVertexTexCoordsToFloat(List<Vertex> vertices) {
		float[] texcoords = new float[vertices.size() * 2];
		int j = 0;
		for(int i=0;i<vertices.size();i++) {
			texcoords[j++] = vertices.get(i).TexCoords.x;
			texcoords[j++] = vertices.get(i).TexCoords.y;
		}
		return texcoords;
	}
	
	public static float[] convertVertexTangentToFloat(List<Vertex> vertices) {
		float[] tangent = new float[vertices.size() * 3];
		int j = 0;
		for(int i=0;i<vertices.size();i++) {
			tangent[j++] = vertices.get(i).Tangent.x;
			tangent[j++] = vertices.get(i).Tangent.y;
			tangent[j++] = vertices.get(i).Tangent.z;
		}
		return tangent;
	}
	
	public static float[] convertVertexBitangentToFloat(List<Vertex> vertices) {
		float[] bitangent = new float[vertices.size() * 3];
		int j = 0;
		for(int i=0;i<vertices.size();i++) {
			bitangent[j++] = vertices.get(i).Bitangent.x;
			bitangent[j++] = vertices.get(i).Bitangent.y;
			bitangent[j++] = vertices.get(i).Bitangent.z;
		}
		return bitangent;
	}
	
	/**
	 * Create buffer for floats
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 * @param data
	 * @return
	 */
	public static FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	/**
	 * create buffer for integers
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 * @param data
	 * @return
	 */
	public static IntBuffer createBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
