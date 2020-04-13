package com.ws718.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author :MysticalYcc
 * @date :16:16 2019/2/25
 */
public enum FileExecutors {
    //实例
    INSTANCE;
    private Logger logger = LoggerFactory.getLogger(FileExecutors.class);
    /**
     * 当前机器cpu核数 -->核心线程数
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    private int maximumPoolSize = 20;
    /**
     * 非核心线程空闲等待回收时间
     */
    private int keepAliveTime = 1;
    /**
     *     如果运行的线程少于 corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。
     *     如果运行的线程等于或多于 corePoolSize，则 Executor 始终首选将请求加入队列，而不添加新的线程。
     *     如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。
     */
    final BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(6);
    private Executor executor = new ThreadPoolExecutor(corePoolSize
            , maximumPoolSize
            , keepAliveTime
            , TimeUnit.SECONDS
            , blockingQueue);

    FileExecutors() {
        //读取配置文件,设置线程池参数
    }

    public Executor getExecutor() {
        return executor;
    }

    public class MyRejected implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.info("被拒绝的任务:[{}]", r.toString());
        }

    }
}
