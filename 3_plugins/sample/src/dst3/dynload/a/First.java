package dst3.dynload.a;

import dst3.dynload.IPluginExecutable;

public class First implements IPluginExecutable {

	@Override
	public void execute() {
		System.out.println("[A : First] Executing...");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[A : First] Finished.");
	}
}
