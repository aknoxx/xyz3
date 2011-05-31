package dst3.dynload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginExecutor implements IPluginExecutor {

	private List<File> dirPaths = new ArrayList<File>();
	private Thread et;
	private Executor executor;
	private boolean stopped = false;

	public PluginExecutor() {
	}
	
	@Override
	public void monitor(File dir) {
		boolean exists = false;
		
		if(!dir.exists()) {
			System.err.println("Directory does not exist: " + dir.getAbsolutePath());
		}
		else if(dir.isDirectory()) {
			for (int i = 0; i < dirPaths.size(); i++) {
				if(dir.getAbsolutePath().equals(dirPaths.get(i).getAbsolutePath())) {
					exists = true;				
				}
			}
			if(!exists) {
				dirPaths.add(dir);
			}	
		}
		else {
			System.err.println("Path is no Directory: " + dir.getAbsolutePath());
		}
	}

	@Override
	public void stopMonitoring(File dir) {
		int i = 0;
		for (File d : dirPaths) {
			if(dir.getAbsolutePath().equals(d.getAbsolutePath())) {
				break;
			}
			i++;
		}
		dirPaths.remove(i);
	}

	@Override
	public void start() {
		executor = new Executor(this);
		et = new Thread(executor);
		et.start();
	}

	@Override
	public void stop() {
		stopped = true;
	}

	public List<File> getDirs() {
		return dirPaths;
	}
	
	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
