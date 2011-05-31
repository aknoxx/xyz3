package dst3.dynload;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		IPluginExecutor pe = new PluginExecutor();
		File dir1 = new File("plugins/Folder1/");
		
		pe.monitor(dir1);
		pe.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pe.monitor(new File("plugins/Folder2/"));
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pe.stopMonitoring(dir1);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pe.monitor(dir1);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Stopping PluginExecutor...");
		pe.stop();
	}
}
