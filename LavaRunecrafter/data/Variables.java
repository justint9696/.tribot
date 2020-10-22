package scripts.LavaRunecrafter.data;

public class Variables {
	private static boolean full = false;
	private static boolean imbue = false;
	private static boolean running = true;
	
	public static void imbueActive() {
		imbue = true;
	}
	
	public static void imbueInactive() {
		imbue = false;
	}
	
	public static boolean imbueState() {
		return imbue;
	}
	
	public static void fullPouches() {
		full = true;
	}
	
	public static void emptyPouches() {
		full = false;
	}
	
	public static boolean pouchState() {
		return full;
	}
	
	public static boolean isRunning() {
		return running;
	}
	
	public static void stopRunning() {
		running = false;
	}
}
