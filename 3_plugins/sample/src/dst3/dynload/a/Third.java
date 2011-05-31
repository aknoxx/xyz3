package dst3.dynload.a;

import dst3.dynload.IPluginExecutable;

public class Third implements IPluginExecutable {

	@Override
	public void execute() {
		System.out.println("[A : Third] Executing...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[A : Third] Finished.");
	}
}
