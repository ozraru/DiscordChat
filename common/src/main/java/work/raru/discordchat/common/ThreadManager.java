package work.raru.discordchat.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private ThreadManager() {}

	private static ExecutorService pool = Executors.newCachedThreadPool();
	
	public static void execute(Runnable command) {
		pool.execute(command);
	}
}
