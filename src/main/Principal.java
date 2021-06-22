package main;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIPropertyStore;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.NVXGPUMemoryInfo;

import io.Window;
import render.Camera;
import render.Model;
import render.Shader;
import render.Transform;
import util.LogUtil;
import util.ObjConverter;

public class Principal implements Runnable{
	private Thread thread;
	private boolean rodando = false;
	private Window window = null;
	private Model model = null;
	private Model model2 = null;
	private Camera camera;
	private Transform transform;
	private Transform transform2;
	public static int TOTAL_MEMORY = 0;
	public static long TIME_START = 0;
	
	public void start() {
		thread = new Thread(this, "Jogo");
		rodando = true;
		thread.start();
	}
	
	private void init() {
		if (!glfwInit()) {
			return;
		}

		window = new Window();
		window.setFullscreen(false);
		window.createWindow("game");

		GL.createCapabilities();
		
		glGetError();

		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		camera = new Camera();
		transform = new Transform();
		transform2 = new Transform();
		
		camera.setPerspective((float)Math.toRadians(90), (float)window.getWidth()/ (float)window.getHeight() , 0.01f, 1000.0f);
		camera.setPosicao(new Vector3f(0.1f, 1.0f, 5.0f));
		Shader.loadAllShaders();
		Shader.ASSIMP_LOADER.bind();

		
		transform2.getRotacao().rotateAxis((float)Math.toRadians(90), 25, 25, -50);
		
		LogUtil.logMemory();
		LogUtil.logTimeStart();
		transform.getRotacao().rotateAxis((float)Math.toRadians(90), 25, 25, -50);
		System.out.println("Modelo com 50%");
		model = new Model("./tennis/tennis.obj");
		LogUtil.logMemory();
		LogUtil.logTimeStart();
		System.out.println("Modelo com 20%");
		model2 = new Model("./tennis20/tennis20.obj");
	}
	
	private void update() {
		glfwPollEvents();
		if (window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
			System.exit(1);
		}
		if(window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(1), 1, 1, 1);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(1), 1, 1, 1);
		}
		if(window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(-1), 1, 1, 1);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(-1), 1, 1, 1);
		}
		//numbers
		
		//x
		if(window.getInput().isKeyDown(GLFW_KEY_KP_4)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(1), 1, 0, 0);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(1), 1, 0, 0);
		}
		if(window.getInput().isKeyDown(GLFW_KEY_KP_6)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(-1), 1, 0, 0);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(-1), 1, 0, 0);
		}
		
		//y
		if(window.getInput().isKeyDown(GLFW_KEY_KP_8)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(-1), 0, 1, 0);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(-1), 0, 1, 0);
		}
		if(window.getInput().isKeyDown(GLFW_KEY_KP_2)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(1), 0, 1, 0);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(1), 0, 1, 0);
		}
		
		//z
		if(window.getInput().isKeyDown(GLFW_KEY_KP_1)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(1), 0, 0, 1);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(1), 0, 0, 1);
		}
		if(window.getInput().isKeyDown(GLFW_KEY_KP_9)) {
			transform.getRotacao().rotateAxis((float)Math.toRadians(-1), 0, 0, 1);
			transform2.getRotacao().rotateAxis((float)Math.toRadians(-1), 0, 0, 1);
		}
		
		window.update();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		Shader.ASSIMP_LOADER.bind();
		Shader.ASSIMP_LOADER.setCamera(camera);
		Shader.ASSIMP_LOADER.setTransformacao(transform);
		model.Draw(Shader.ASSIMP_LOADER);
		transform2.setPosicao(new Vector3f(-1.0f, 1.0f, 1.0f));
		
		Shader.ASSIMP_LOADER.setTransformacao(transform2);
		model2.Draw(Shader.ASSIMP_LOADER);
		window.swapBuffers();
	}
	
	@Override
	public void run() {
		init();
		while (rodando) {
			update();
			render();
		}
		
	}
	
	public static void main(String[] args) {
		new Principal().start();
	}

}
