package com.ws718.controller;

import com.ws718.client.WebServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Set;

/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
@Repository("file718Controller")
public class File718Controller {
    private static Logger logger = LoggerFactory.getLogger(File718Controller.class);
    /**
     * 监听目录
     */
    public final static String InPath = "InFile";
    /**
     * 结果集存放目录
     */
    public final static String outPath = "OutFile";
    /**
     * 远程webservice地址
     */
    private volatile static String url;
    /**
     * 变化的文件列表
     */
    private Set<String> changeSet;

    private String encoding = "GBK";


    public File718Controller(Set<String> changeSet, String encoding, String file_extension) {
        WebServiceClient.setConfig(true, file_extension, encoding);
        this.changeSet = changeSet;
        if (encoding != null) {
            this.encoding = encoding.toUpperCase();
        }

    }

    public File718Controller() {

    }

    /**
     * @param path
     */
    public void traverseFolder(String path) {
        if (changeSet == null) {
            File file = new File(path);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (null == files || files.length == 0) {
                    logger.info("Folder has nothing");
                    return;
                } else {
                    disposeFile(files);
                }
            } else {
                logger.error("Folder is not exists!");
            }
        } else {
            for (String key : changeSet) {
                File file = new File(path + "/" + key);
                handlerFile(file);
            }
        }
    }

    private void disposeFile(File[] files) {
        for (File file2 : files) {
            if (file2.isDirectory()) {
                logger.debug("Folder:" + file2.getAbsolutePath());
                logger.debug("Folder:" + file2.getName());
                traverseFolder(file2.getAbsolutePath());
            } else {
                handlerFile(file2);
            }
        }
    }

    /**
     * 处理每一个被监听文件的变化
     *
     * @param file
     */
    private void handlerFile(File file) {
        BufferedReader bufferedReader = null;
        final StringBuilder finalString = new StringBuilder();
        String fileName = file.getName();
        logger.debug("File:" + fileName);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);

            bufferedReader = new BufferedReader(inputStreamReader);
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) {
                finalString.append(temp);
            }
            logger.debug("File contents:" + finalString);
            if (fileName.contains("url")) {
                url = finalString.toString().trim();
            } else {
                new Thread(new MyRunnable(fileName, finalString), "Thread-CallWebService").start();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            logger.error("File not exists");
        } catch (IOException e) {
            logger.error("File reader failed");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    logger.error("close exception,[{}]", e);
                }
            }
        }
    }

    /**
     * 调用webService
     */
    class MyRunnable implements Runnable {
        private Logger logger = LoggerFactory.getLogger(MyRunnable.class);
        private StringBuilder queryParam;
        private String fileName;

        private MyRunnable(String fileName, StringBuilder queryParam) {
            this.fileName = fileName;
            this.queryParam = queryParam;
        }

        @Override
        public void run() {
            logger.info("==================================query start===================================");
            while (true) {
                if (url != null) {
                    WebServiceClient.fileToWebService(fileName, queryParam.toString(), url);
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            logger.info("===================================query end===================================");
        }
    }
}
