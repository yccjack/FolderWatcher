package com.ws718.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 单例模式
 * @author :MysticalYcc
 * @date :9:02 2019/2/27
 */

public class ActMutexExecutorServiceUtil {
    /**
     * 等待子线程超时时间
     */
    private static final int AWAIT_TIME = 500;
    /**
     * 设置线程数量
     */
    public static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

    private static Object obj = new Object();

    private static ExecutorService executor = null;

    private ActMutexExecutorServiceUtil() {

    }

    /**
     * 获得一个固定线程池对象executor </br> 线程池数量设定 {@link ActMutexExecutorServiceUtil#THREAD_NUM}
     *
     * @return ExecutorService
     */
    public static ExecutorService getExecutorServiceInstance() {
        if (executor == null) {
            synchronized (obj) {

                if (executor == null) {
                    executor = Executors.newFixedThreadPool(THREAD_NUM);
                }
            }
        }
        return executor;

    }

    /**
     * 重置线程池
     */
    public static void restartExecutor() {
        executor = null;
        getExecutorServiceInstance();
    }

    /**
     * 关闭并等待子线程在指定时间内来完成处理</br> 时间设定 {@link ActMutexExecutorServiceUtil#AWAIT_TIME} </br> 超时强制关闭子线程</br>

     */
    public static void shutDown() throws RuntimeException{
        executor.shutdown();
        try {
            executor.awaitTermination(AWAIT_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            shutDownNowAndRestart();
            throw new RuntimeException("SP-SERVICE-ExecutorServiceUtil.shutDown", e);
        }
    }

    /**
     * 立即关闭子线程并重置线程池
     */
    public static void shutDownNowAndRestart() {
        executor.shutdownNow();
        restartExecutor();
    }

}
