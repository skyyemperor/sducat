package com.sducat.common.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class TaskExecutorUtil<C> {

    public TaskExecutorUtil() {

    }

    private static final ThreadFactory factory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };

    private static final ThreadPoolExecutor cachePool = new ThreadPoolExecutor(
            4, 40,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            factory
    );

    public void run(Runnable r) {
        cachePool.execute(r);
    }

    public Future<C> submit(Callable<C> c) {
        return cachePool.submit(c);
    }

}
