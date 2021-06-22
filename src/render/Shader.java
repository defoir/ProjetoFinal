package render;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
	public static final int ATRIBUTO_VERTEX = 0;
	public static final int ATRIBUTO_TEX_COORD = 1;

	public static Shader ASSIMP_LOADER;

	private int projection_matrix, transform_object_matrix, transform_world_matrix;
	public int program;
	private int vs;
	private int fs;
	private Map<String, Integer> cacheLocal = new HashMap<String, Integer>();

	public Shader(String filename, boolean vertex, boolean fragment) {
		program = glCreateProgram();
		if (vertex) {
			vs = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vs, readFile(filename + ".vs"));
			glCompileShader(vs);
			if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
				System.err.println(glGetShaderInfoLog(vs));
				System.exit(1);
			}
		}
		if (fragment) {
			fs = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fs, readFile(filename + ".fs"));
			glCompileShader(fs);
			if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
				System.err.println(glGetShaderInfoLog(fs));
				System.exit(1);
			}
		}

		glAttachShader(program, vs);
		glAttachShader(program, fs);

		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
		
		projection_matrix = glGetUniformLocation(program, "projection");
		transform_world_matrix = glGetUniformLocation(program, "view");
		transform_object_matrix = glGetUniformLocation(program, "model");
		
	}

	public static void loadAllShaders() {
		ASSIMP_LOADER = new Shader("assimp_load", true, true);
	}

	public void bind() {
		glUseProgram(program);
	}

	public void detach() {
		glUseProgram(0);
	}

	public void setCamera(Camera camera) {
		if(projection_matrix != -1) {
			float matrix[] = new float[16];
			camera.getProjecao().get(matrix);
			glUniformMatrix4fv(projection_matrix, false, matrix);
		}
		
		if(transform_world_matrix != -1) {
			float matrix[] = new float[16];
			camera.getTransformacao().get(matrix);
			glUniformMatrix4fv(transform_world_matrix, false, matrix);
		}
	}
	
	public void setTransformacao(Transform transform) {
		if(transform_object_matrix != -1) {
			float matrix[] = new float[16];
			transform.getTransformacao().get(matrix);
			glUniformMatrix4fv(transform_object_matrix, false, matrix);
		}
	}

	public void setUniform(String name, Matrix4f value) {
		int location = glGetUniformLocation(program, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);

		if (location != -1) {
			glUniformMatrix4fv(location, false, buffer);
		}
	}

	public void setUniform(String name, Vector4f value) {
		int location = glGetUniformLocation(program, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		value.get(buffer);

		if (location != -1) {
			glUniformMatrix4fv(location, false, buffer);
		}
	}
	
	public void setUniform(String name, Vector3f value) {
		int location = glGetUniformLocation(program, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		value.get(buffer);

		if (location != -1) {
			glUniform3fv(location, buffer);
		}
	}

	public void setUniform(String name, int value) {
		int location = glGetUniformLocation(program, name);
		if (location != -1) {
			glUniform1i(location, value);
		}
	}

	public void setUniform(String name, float value) {
		int location = glGetUniformLocation(program, name);
		if (location != -1) {
			glUniform1f(program, value);
		}
	}

	public int getUniform(String name) {
		if (cacheLocal.containsKey(name)) {
			return cacheLocal.get(name);
		}
		int result = glGetUniformLocation(program, name);
		if (result == -1) {
			System.out.println("Erro não foi possível encontrar a variável uniform:" + name);
		} else {
			cacheLocal.put(name, result);
		}
		return result;
	}

	private String readFile(String filename) {
		StringBuilder string = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
			String line;
			while ((line = br.readLine()) != null) {
				string.append(line);
				string.append("\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string.toString();
	}
}
