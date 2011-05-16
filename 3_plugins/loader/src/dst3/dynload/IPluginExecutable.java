package dst3.dynload;

/**
 * Implementations of this interface are executable by the IPluginExecutor.
 */
public interface IPluginExecutable {

    /**
     * Called when this plugin is executed.
     */
    void execute();
}
