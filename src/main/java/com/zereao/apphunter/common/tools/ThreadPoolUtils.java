package com.zereao.apphunter.common.tools;

import java.util.concurrent.*;

/**
 * @author Zereao
 * @version 2019/05/09  15:34
 */
public class ThreadPoolUtils {
    private static ExecutorService executor = new ThreadPoolExecutor(
            4,
            5,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadPoolExecutor.AbortPolicy());

    public static ExecutorService getExecutor() {
        return ThreadPoolUtils.executor;
    }

    public static void execute(Runnable task) {
        executor.execute(task);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }
}
