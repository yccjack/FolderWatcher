package com.ws718.client;


import com.alibaba.fastjson.JSON;
import com.dxss.ws.QueryServicePortType;
import com.dxss.ws.Queryservice;
import com.ws718.abst.Strategy718;
import com.ws718.bean.ResultBean;
import com.ws718.controller.File718Controller;
import com.ws718.util.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Holder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.ws718.abst.Strategy718.QUERY_STATUS;


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

    private static String FILE_EXTENSION = "";
    /**
     * 服务返回错误信息标志
     */
    private static final int ERROR_FAIL_FLAG = 0;

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
        try {
            callWebService(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataHandler dataHandler = new DataHandler();
        return handler(dataHandler);
    }


    /**
     * 根据不同的接口名调用不同的方法;
     *
     * @param param 前端json参数
     */
    private static void callWebService(String param) throws IOException {
        log.info("Request Param:[]", param);
        String realUrl = "";
        URL url;
        Queryservice queryservice;
        WEB_SERVICE_REQUEST_LOCK.lock();
        DataHandler dataHandler = new DataHandler();
        try {
            Map<String, String> map = JSON.parseObject(param, Map.class);
            String wsdl = map.get(WSDL);
            if (wsdl != null) {
                if (wsdl.contains(WSDL)) {
                    realUrl = wsdl.substring(0, wsdl.indexOf("?wsdl"));
                } else {
                    realUrl = wsdl;
                }
            }
            String methodName = map.get("method");

            dataHandler.setQueryXml(map.get("param"));
            url = new URL(realUrl);
            queryservice = new com.dxss.ws.Queryservice(url);
            QueryServicePortType queryPort = queryservice.getQueryServiceFor718ImplPort();
            callWebservice(map.get("queryId"), methodName, queryPort, dataHandler);

        } catch (MalformedURLException e) {
            log.info("client error!:[]", e);
            dataHandler.setReturnValue(new Holder<>("client error!"));
            e.printStackTrace();
        } finally {
            WEB_SERVICE_REQUEST_LOCK.unlock();
        }
    }

    /**
     * 调用远程服务
     *
     * @param queryId     查询ID
     * @param methodName  查询方法
     * @param queryPort   查询的webservice服务
     * @param dataHandler 封装参数实体类
     */
    private static void callWebservice(String queryId, String methodName, QueryServicePortType queryPort, DataHandler dataHandler) throws IOException {
        String result = null;
        switch (methodName) {
            case Strategy718.QUERY_DATA_SYC:
                result = queryPort.queryData(dataHandler.getQueryXml(), dataHandler.getQueryID(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                break;
            case Strategy718.QUERY_DATA_ASY:
                result = queryPort.queryData(dataHandler.getQueryXml(), dataHandler.getQueryID(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                int queryStatus = queryPort.queryStatus(queryId);
                log.debug("queryData2 return status is [{}]", queryStatus);
                while (DataHandler.COMPLETE_STATUS != queryStatus) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log.error("Thread error:[{}]", Thread.currentThread().getName());
                    }
                    queryStatus = queryPort.queryStatus(queryId);
                }
                result += "  queryStatus:[" + queryStatus + "]";
                break;
            case Strategy718.BASIC_RESOURCE:
                result = queryPort.basicResource(dataHandler.getQueryXml(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                break;
            case Strategy718.STATISTICS:
                result = queryPort.statistics(dataHandler.getQueryXml(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                break;
            case Strategy718.ALARM:
                result = queryPort.alarm(dataHandler.getQueryXml(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                break;
            case Strategy718.QUERY_STATUS:
                int Status = queryPort.queryStatus(queryId);
                result = Status + "";
                break;
            default:
                log.error("methodName error!");
                break;
        }
        if (result != null) {
            if (result.contains("{") && result.contains("}")) {
                List list = JSON.parseObject(result, List.class);
                dataHandler.setOtherResult(list);
            }
            if (logAndFile) {
                disposeLogAndFile(methodName, dataHandler, result);
            }
        } else {
            dataHandler.setReturnValue(new Holder<>(""));
        }
    }

    /**
     * 将结果写入文件
     *
     * @param methodName  文件名前缀
     * @param dataHandler 封装对象
     * @param result      结果
     * @throws IOException IOException
     */
    private static void disposeLogAndFile(String methodName, DataHandler dataHandler, String result) throws IOException {
        File file = new File(File718Controller.outPath + "/" + methodName + "/" + methodName + FILE_EXTENSION);
        File parentFile = file.getParentFile();
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
                    log.error("Create file failed ! [{]]", methodName + FILE_EXTENSION);
                }
            }
        }
        String fileString;
        if (ERROR_FAIL_FLAG == dataHandler.getErrorFlag().value) {
            fileString = "--------------------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "--------------\n"
                    + "param: [" + dataHandler.getQueryXml() + "]\n result:[ " + result + " ]\n"
                    + "ERROR: [" + dataHandler.getErrorInfo() + "]\n"
                    + "\n";
        } else {
            fileString = "--------------------" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "--------------\n"
                    + "param: [" + dataHandler.getQueryXml() + "]\n result:[ " + result + " ]\n"
                    + "\n";
        }
        log.info("query param:[{}],query result:[{]]", dataHandler.getQueryXml(), result);
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(fileString);
        log.info("write file succeed!");
        bw.close();

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


    /**
     * 以文件的方式调用远程服务
     *
     * @param fileName   文件名
     * @param queryParam 查询参数(文件内容)
     * @param wsdl       url文件的内容
     */
    public static void fileToWebService(String fileName, String queryParam, String wsdl) {
        log.info("Request Param:fileName:[{}],queryParam:[{}]", fileName, queryParam);
        String realUrl = "";
        URL url;
        Queryservice queryservice;

        if (wsdl != null) {
            if (wsdl.contains(WSDL)) {
                realUrl = wsdl.substring(0, wsdl.indexOf("?wsdl"));
            } else {
                realUrl = wsdl;
            }
        } else {
            log.error("url is error! url:[{}]");
        }
        DataHandler dataHandler = new DataHandler();
        try {

            dataHandler.setQueryXml(queryParam);
            url = new URL(realUrl);
            queryservice = new com.dxss.ws.Queryservice(url);
            QueryServicePortType queryPort = queryservice.getQueryServiceFor718ImplPort();
            try {
                callWebservice(queryParam
                        , fileName.contains(".")?fileName.substring(0, fileName.indexOf(".")):fileName
                        , queryPort
                        ,dataHandler);
            } catch (IOException e) {
                log.error("process the file error!:[{}]---[{}]", Thread.currentThread().getName(), fileName);
            }
        } catch (MalformedURLException e) {
            log.info("client error!:[]", e);
            dataHandler.setReturnValue(new Holder<>("client error!"));
            dataHandler.setErrorFlag(new Holder<>(0));
        }
    }


}
