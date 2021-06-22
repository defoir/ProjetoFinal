package util;

import static org.lwjgl.opengl.GL11.glGetIntegerv;

import java.nio.IntBuffer;

import org.lwjgl.opengl.NVXGPUMemoryInfo;

import main.Principal;

public abstract class LogUtil {
	public static int TOTAL_MEMORY = 0;
	public static long TIME_START = 0;
	
	/**
	 * Log on to the console the total memory available for Nvidia GPU
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 */
	public static void logMemory() {
		int[] total_memorys = new int[1];
		IntBuffer total_memory_buffer = ObjConverter.createBuffer(total_memorys);
		glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX, total_memory_buffer);
		TOTAL_MEMORY = total_memory_buffer.get(0);
		System.out.println("Mémoria total:"+TOTAL_MEMORY);
	}
	 
	/**
	 * Return variable with the amount of total memory available the moment this method is called
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 * @return
	 */
	public static int saveMemoryNow() {
		int[] total_memorys = new int[1];
		IntBuffer total_memory_buffer = ObjConverter.createBuffer(total_memorys);
		glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX, total_memory_buffer);
		int total_memory = total_memory_buffer.get(0);
		return total_memory;
	}
	
	/**
	 * Calculate between memory now and memory start how much in kbs it is being used
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 * @return
	 */
	public static int memoryUsageNow() {
		int memoryNow = saveMemoryNow();
		System.out.println("Quanto está sendo utilizado no momento:" + (TOTAL_MEMORY - memoryNow)+ "kb");
		return TOTAL_MEMORY - memoryNow;
	}
	
	/**
	 * Variation of the calculation by passing the memory by parameter
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 * @param memoryNow
	 * @return
	 */
	public static int memoryUsageNow(int memoryNow) {
		System.out.println("Quanto está sendo utilizado no momento:" + (TOTAL_MEMORY - memoryNow)+ "kb");
		return TOTAL_MEMORY - memoryNow;
	}
	
	public static void logTimeStart() {
		TIME_START = System.currentTimeMillis();
	}
	
	public static long logTimeNow() {
		long time_now = System.currentTimeMillis();
		return time_now;
	}
	/**
	 * calculate between time start and time as of right now and return in ms
	 * @author Lucas Sommer <lucas.sommer@multi24h.com.br>
	 * @since  19 de jun. de 2021
	 * @return
	 */
	public static long logTimeUsage() {
		return logTimeNow() - TIME_START;
	}
}
