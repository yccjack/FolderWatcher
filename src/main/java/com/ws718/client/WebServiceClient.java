package com.ws718.client;


import com.alibaba.fastjson.JSON;
import com.ws718.bean.ResultBean;
import com.ws718.controller.File718Controller;
import com.ws718.util.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ClassName WebServiceClient
 * Description Todo
 *
 * @author MysticalYcc
 * Version 1.0
 */
public class WebServiceClient {
    private static Logger log = LoggerFactory.getLogger(WebServiceClient.class);
    /**
     *
     */
    private final static Lock WEB_SERVICE_REQUEST_LOCK = new ReentrantLock();

    private static final String WSDL = "wsdl";

    private static boolean logAndFile = false;


    private static String encoding = "GBK";

    private static String FILE_EXTENSION = ".txt";

    public static void setConfig(boolean logAndFile, String file_extension, String encoding) {

        WebServiceClient.logAndFile = logAndFile;
        if (file_extension != null) {
            WebServiceClient.FILE_EXTENSION = file_extension;
        }
        if (encoding != null) {
            WebServiceClient.encoding = encoding;
        }
    }

    public static ResultBean getWebServer(String param) {
        callWebService(param);
        DataHandler dataHandler = new DataHandler();
        return handler(dataHandler);
    }


    /**
     * 根据不同的接口名调用不同的方法;
     *
     * @param param 前端json参数
     */
    private static void callWebService(String param) {
        log.info("Request Param:[]", param);
        try {
            WEB_SERVICE_REQUEST_LOCK.lock();
        } finally {
            WEB_SERVICE_REQUEST_LOCK.unlock();
        }
    }


    /**
     * 将结果写入文件
     *
     * @param methodName  文件名前缀
     * @param dataHandler 封装对象
     * @param result      结果
     */
    private static void disposeLogAndFile(String methodName, DataHandler dataHandler, String result) {
        File file = new File(File718Controller.outPath + "/" + methodName + "/" + methodName + FILE_EXTENSION);
        FileWriter fw = null;
        BufferedWriter bw = null;
        File parentFile = file.getParentFile();
        try {
            if (!parentFile.exists()) {
                if (parentFile.mkdir()) {
                    if (file.createNewFile()) {
                        log.info("Create file succeed!");
                    } else {
                        log.error("Create file failed ! [{]]", methodName + FILE_EXTENSION);
                    }
                }
            } else {
                if (!file.exists()) {
                    if (file.createNewFile()) {
                        log.info("Create file succeed!");
                    } else {
                        log.error("Create file failed ! [{}]", methodName + FILE_EXTENSION);
                    }
                }
            }
            String fileString;

            fileString = "--------------------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "--------------\n[ "
                    + JSON.toJSONString(dataHandler) + "\n: ]"
                    + "result:[ " + result + " ]\n";
            WEB_SERVICE_REQUEST_LOCK.lock();
            log.info("query param:[{}],query result:[{}]", dataHandler.getQueryXml(), result);
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(fileString);
            bw.flush();
            log.info("write file succeed!");
            bw.close();
            fw.close();
        } catch (IOException e) {
            log.error("Dispose file or folder error!:[{}]", e);
        } finally {
            WEB_SERVICE_REQUEST_LOCK.unlock();
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    log.error("BufferedWriter closed error!:[{}]", e);
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    log.error("FileWriter closed error!:[{}]", e);
                }
            }
        }
    }

    /**
     * 处理webService返回结果
     *
     * @param dataHandler 通讯实体类
     * @return ResultBean 业务实体
     */
    private static ResultBean handler(DataHandler dataHandler) {
        ResultBean result = new ResultBean();
        result.setErrorFlag(dataHandler.getErrorFlag() == null ? null : dataHandler.getErrorFlag().value);
        result.setErrorInfo(dataHandler.getErrorInfo() == null ? null : dataHandler.getErrorInfo().value);
        result.setQueryId(dataHandler.getQueryID() == null ? null : dataHandler.getQueryID().value);
        result.setResult(dataHandler.getReturnValue() == null ? null : dataHandler.getOtherResult());
        return result;
    }

}
