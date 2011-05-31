package dst3.dynload.b;

import dst3.dynload.IPluginExecutable;

public class MegaPlugin implements IPluginExecutable {

	@Override
	public void execute() {
		System.out.println("[b : MegaPlugin] Executing...");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[b : MegaPlugin] Finished.");
	}
}
