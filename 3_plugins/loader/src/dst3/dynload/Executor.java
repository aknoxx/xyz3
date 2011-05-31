package dst3.dynload;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Executor implements Runnable {
	
	private static final Logger logger =
        Logger.getLogger(Executor.class.getPackage().getName());	
	
	private PluginExecutor pluginExecutor;
	private final ExecutorService pool;
	
	private Map<String, Long> jarPaths = new HashMap<String, Long>();
	
	private List<String> runningPlugins = new ArrayList<String>();
	
	public Executor(PluginExecutor pluginExecutor) {
		logger.setLevel(Level.OFF);
		
		this.pluginExecutor = pluginExecutor;
		pool = Executors.newCachedThreadPool();
	}
	
	public synchronized void pluginFinished(String pluginName) {
		int i;
		for (i=0; i<runningPlugins.size(); i++) {
			if(runningPlugins.get(i).equals(pluginName)) {
				break;
			}
		}
		runningPlugins.remove(i);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {		
		boolean isModified;
		while(!pluginExecutor.isStopped()) {
			for (File dir : pluginExecutor.getDirs()) {
				File[] jars = dir.listFiles(new FilenameFilter() {
					public boolean accept(File d, String name) {
						return name.endsWith(".jar");
						}});
				
				
				for (File jar : jars) {
					isModified = true;
					
					log("First time: Path: " + jar.getPath() + ", time: " + jar.lastModified());
					
					if(jarPaths.containsKey(jar.getAbsolutePath())) {
						Long timestamp = jarPaths.get(jar.getAbsolutePath());
						log("Path: " + jar.getPath() + ", time: " + timestamp);
						if(jar.lastModified() == timestamp) {
							isModified = false;
						}
					}
					
					if(isModified) {
						jarPaths.put(jar.getAbsolutePath(), new Long(jar.lastModified()));
						
						JarFile jarFile = null;
						try {
							jarFile = new JarFile(jar);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						log("Search in "+ jarFile.getName());
						
						if(jarFile != null) {
							Enumeration<JarEntry> jarEntries = jarFile.entries();
							
							while(jarEntries.hasMoreElements()) {
								JarEntry jarEntry = (JarEntry) jarEntries.nextElement();							
								
								String entryName = jarEntry.getName();
								
								log("EntryName: " + entryName);
								
								if(entryName.endsWith(".class")) {
						
									URL[] url = null;
									try {
										url = new URL[] { jar.toURL()};
									} catch (MalformedURLException e1) {
										e1.printStackTrace();
									}

									entryName = entryName.replace("/", ".");	
									String className = entryName.substring(0, entryName.length()-6);
									
									ClassLoader loader = URLClassLoader.newInstance(url);
									Class<?> dynmicallyLoadedClass = null;
									try {
										dynmicallyLoadedClass = Class.forName(className, false, loader);
										log("Name: " + dynmicallyLoadedClass.getName());
									} catch (ClassNotFoundException e1) {
										System.err.println("Error loading plugin class.");
									}
									
									if(!dynmicallyLoadedClass.isInterface()) {
										Class<?>[] ifs = dynmicallyLoadedClass.getInterfaces();
										for (Class<?> interfase : ifs) {
											if(interfase.equals(IPluginExecutable.class)) {
												log("Interface found: " + interfase.getName());
												
												IPluginExecutable plugin = null;
												try {
													plugin = (IPluginExecutable) dynmicallyLoadedClass.newInstance();
												} catch (InstantiationException e) {
													System.err.println("Error instantiating plugin.");
												} catch (IllegalAccessException e) {
													System.err.println("Error instantiating plugin.");
												}
												
												boolean isRunning = false;
												if(plugin != null) {
													for (String name : runningPlugins) {
														if(name.equals(className)) {
															isRunning = true;
															break;
														}
													}
													if(!isRunning) {
														runningPlugins.add(className);
														Execution execution = new Execution(plugin, className, this);
														pool.execute(execution);
													}													
												}
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		shutdownAndAwaitTermination();
	}
	
	public void shutdownAndAwaitTermination() {
         pool.shutdown(); // Disable new tasks from being submitted
         
         try {
           // Wait a while for existing tasks to terminate
           if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
             pool.shutdownNow(); // Cancel currently executing tasks
             // Wait a while for tasks to respond to being canceled
             if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                 System.err.println("Pool did not terminate");
             }
           }
         } catch (InterruptedException ie) {
           // (Re-)Cancel if current thread also interrupted
           pool.shutdownNow();
           // Preserve interrupt status
           Thread.currentThread().interrupt();
         }
         System.out.println("PluginExecutor stopped.");
	}
	
	private void log(String msg) {
		logger.log(Level.INFO, msg);
	}
}	


