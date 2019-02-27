package com.ws718.client;


import ch.qos.logback.core.net.server.Client;
import com.alibaba.fastjson.JSON;
import com.dxss.ws.QueryServicePortType;
import com.dxss.ws.Queryservice;
import com.ws718.abst.Strategy718;
import com.ws718.bean.ResultBean;
import com.ws718.controller.File718;
import com.ws718.controller.File718Controller;
import com.ws718.util.ActMutexExecutorServiceUtil;
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
import java.util.concurrent.*;
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
    private static void callWebservice( final String queryId,final String methodName, final QueryServicePortType queryPort, final DataHandler dataHandler) {
        ExecutorService executors = ActMutexExecutorServiceUtil.getExecutorServiceInstance();
        Future<String> submit = executors.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String result = null;
                switch (methodName) {
                    case Strategy718.QUERY_DATA_SYC:
                        result = queryPort.queryData(dataHandler.getQueryXml(), dataHandler.getQueryID(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                        break;
                    case Strategy718.QUERY_DATA_ASY:
                        result = queryPort.queryData(dataHandler.getQueryXml(), dataHandler.getQueryID(), dataHandler.getErrorFlag(), dataHandler.getErrorInfo());
                        String Id = dataHandler.getQueryID().value;
                        int queryStatus = queryPort.queryStatus(Id);
                        log.debug("queryData2 return status is [{}]", queryStatus);
                        while (DataHandler.COMPLETE_STATUS != queryStatus) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                log.error("Thread error:[{}]", Thread.currentThread().getName());
                            }
                            queryStatus = queryPort.queryStatus(Id);
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
                return result;
            }

        });

        String result = null;
        try {
            result = submit.get(File718.timeout, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            log.error("query timeout! queryMethod:[{}]", methodName);
            result = "query timeout! queryMethod:[]" + methodName;
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        final DataHandler dataHandler = new DataHandler();
        try {
            dataHandler.setQueryXml(queryParam);
            url = new URL(realUrl);
            queryservice = new com.dxss.ws.Queryservice(url);
            QueryServicePortType queryPort = queryservice.getQueryServiceFor718ImplPort();
            callWebservice(queryParam
                    , fileName.contains(".") ? fileName.substring(0, fileName.indexOf(".")) : fileName
                    , queryPort
                    , dataHandler);
        } catch (MalformedURLException e) {
            log.error("client error!:[]", e);
            dataHandler.setReturnValue(new Holder<>("client error!"));
            dataHandler.setErrorFlag(new Holder<>(0));
        }
    }


}
