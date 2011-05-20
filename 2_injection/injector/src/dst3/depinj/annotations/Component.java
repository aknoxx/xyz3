package dst3.depinj.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
	ScopeType scope();
}
