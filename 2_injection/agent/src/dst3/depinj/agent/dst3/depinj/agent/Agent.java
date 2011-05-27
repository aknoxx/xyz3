package dst3.depinj.agent;

import java.lang.instrument.Instrumentation;

public class Agent {

	public static void premain(String command, Instrumentation instrumentation) {
		
		instrumentation.addTransformer(new SimpleTransformer());
	}
}
