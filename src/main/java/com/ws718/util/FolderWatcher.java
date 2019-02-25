package com.ws718.util;

import com.ws718.controller.File718Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
public class FolderWatcher {
    private Logger logger = LoggerFactory.getLogger(FolderWatcher.class);
    /**
     * 文件夹监听
     */
    private WatchService watcher;


    /**
     * url文件名称
     */
    private String url;
    /**
     * 监听到改变的文件
     */
    private Set<String> changeSet = new HashSet<>();

    /**
     * 控制url是否是第一次给予(在之后的文件改变中url可能是不变的,但是此参数必须添加到改变文件的列表中.才能读取)
     */
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * 自定义编码
     */
    private String encoding;

    /**
     * 自定义文件类型
     */
    private String file_extension;


    /**
     * 启动项目判断是否需要进行一次文件遍历读取url
     */
    private AtomicBoolean is_init = new AtomicBoolean(true);

    /**
     * 监听文件新建与修改
     *
     * @param directory 需要监听的目录
     * @throws IOException 目录不存在
     */
    public FolderWatcher(final String directory, String encoding, String file_extension) throws IOException {
        this.encoding = encoding;
        this.file_extension = file_extension;
        watcher = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(directory);
        path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start(directory);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-fileHandler").start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    watcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //
                    // logger.error(" watcher.close error!");
                }
            }
        }));
    }


    /**
     * 监听文件夹文件变化并对改变的文件做处理
     *
     * @param directory 需要监听的目录
     * @throws InterruptedException InterruptedException
     */
    public void start(String directory) throws InterruptedException {
        String preFileName = null;
        if (is_init.getAndSet(false)) {
            initUrl(directory);
        }
        while (true) {
            WatchKey key = watcher.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                //事件可能lost or discarded
                if (kind == OVERFLOW) {
                    continue;
                }
                Path fileName = (Path) event.context();
                if (kind == ENTRY_CREATE) {
                    // 判断文件是否创建成功
                    boolean fileIsCreateSuccess = fileIsCreateSuccess(directory + fileName);
                    if (fileIsCreateSuccess) {
                        logger.info("file is created succeed!:[{}]", fileName);
                    }
                }
                String fileNameString = fileName.toString();
                if (!fileNameString.contains("jb_tmp") && !fileNameString.contains("jb_old") && !fileNameString.contains("swp")) {
                    if (!fileNameString.equalsIgnoreCase(preFileName)) {
                        logger.debug("Event [{}] has happened,which fileName is [{}] ", kind.name(), fileName);
                        preFileName = fileNameString;
                        count.incrementAndGet();
                        changeSet.add(fileNameString);
                        if (preFileName.contains("url")) {
                            url = preFileName;
                        }
                    }
                }
            }
            if (url != null) {
                changeSet.add(url);
                count.incrementAndGet();
            }
            if (hasUrl(changeSet) && count.get() != 1) {
                File718Controller fileController = new File718Controller(changeSet, encoding, file_extension);
                fileController.traverseFolder(File718Controller.InPath);
                changeSet.clear();
                preFileName = null;
                count.set(0);
            }
            //这行必须有，不然不能连续地监控目录
            if (!key.reset()) {
                break;
            }
        }
    }

    /**
     * 判断启动时目录是否含有url文件
     *
     * @param directory 监听目录
     */
    private void initUrl(String directory) {
        File file = new File(directory);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                logger.info("Folder has nothing");
            } else {
                for (File file2 : files) {
                    String name = file2.getName();
                    if (name.contains("url")) {
                        url = file2.getName();
                        break;
                    }
                }
            }
        } else {
            logger.error("Folder is not exists!");
        }
    }

    /**
     * 变化的文件中是否含有url文件,如若没有返回false,有则返回true
     *
     * @param changeSet 指定文件夹下改变的文件列表
     * @return boolean
     */
    private boolean hasUrl(Set<String> changeSet) {
        boolean flag = false;
        for (String key : changeSet) {
            if (key.contains("url")) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    /**
     * 监听的ENTRY_CREATE事件 文件是否创建完成,防止因为有ENTRY_CREATE事件却没有完成文件
     *
     * @param filePath 监听目录
     * @return ENTRY_CREATE文件是否创建完成
     */
    private boolean fileIsCreateSuccess(String filePath) {
        try {
            File file;
            file = new File(filePath);
            long len1, len2;
            len2 = file.length();
            do {
                len1 = len2;
                Thread.sleep(100);
                file = new File(filePath);
                len2 = file.length();
                System.out.println("Before the 500 ms with then:" + len1 + "," + len2);
            } while (len1 < len2);
            return true;
        } catch (Exception e) {
            logger.error("File creation failed", e);
            return false;
        }
    }

}