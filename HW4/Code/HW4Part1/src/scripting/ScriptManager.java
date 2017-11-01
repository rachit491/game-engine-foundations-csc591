package scripting;

import javax.script.*;

public class ScriptManager {
	
	private static ScriptEngine js_engine = new ScriptEngineManager().getEngineByName("JavaScript");
	private static Invocable js_invocable = (Invocable) js_engine;

	/**
	 * Used to bind the provided object to the name in the scope of the scripts
	 * being executed by this engine.
	 */
	public static void bindArgument(String name, Object obj) {
		js_engine.put(name,obj);
	}
	
	/**
	 * Will load the script source from the provided filename.
	 */
	public static void loadScript(String script_name) {
		try {
			js_engine.eval(new java.io.FileReader(script_name));
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(java.io.IOException iox) {
			iox.printStackTrace();
		}
	}

	/**
	 * Will invoke the "update" function of the script loaded by this engine
	 * without any parameters.
	 */
	public static void executeScript(String func, Object... objects) {
		try {
			js_invocable.invokeFunction(func, objects);
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
}
