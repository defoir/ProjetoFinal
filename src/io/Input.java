package io;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

public class Input {
	private long window;
	private int newState;
	private int oldState;
	
	private boolean keys[];
	
	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW_KEY_LAST + 1];
	}
	
	public boolean isKeyDown(int key) {
		return glfwGetKey(window, key) == 1;
	}
	
	public boolean isKeyPressed(int key) {
		return (isKeyDown(key) && !keys[key]); 
	}
	
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key) && keys[key]);
	}
	
	public boolean isMouseButtonDown(int button) {
		newState = glfwGetMouseButton(window, button);
		if(newState == GLFW_RELEASE && oldState == GLFW_PRESS) {
			oldState = newState;
			return true;
		}
		oldState = newState;
		return false;
	}	
	
	public void update() {
		for(int i=GLFW_KEY_SPACE;i<keys.length;i++) {
			keys[i] = isKeyDown(i);
		}
	}
}
