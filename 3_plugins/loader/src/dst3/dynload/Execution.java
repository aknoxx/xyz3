package dst3.dynload;

public class Execution implements Runnable {

	private IPluginExecutable pluginExecutable;
	private Executor executor;
	private String pluginName;
	
	public Execution(IPluginExecutable pluginExecutable, String pluginName, Executor executor) {
		this.pluginExecutable = pluginExecutable;
		this.executor = executor;
		this.pluginName = pluginName;
	}
	
	@Override
	public void run() {
		pluginExecutable.execute();		
		pluginExecutable = null;
		executor.pluginFinished(pluginName);
	}
}
