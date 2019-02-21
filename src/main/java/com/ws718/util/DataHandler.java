package com.ws718.util;

import javax.xml.ws.Holder;
import java.io.Serializable;

/**
 * * @author ycc
 */
public class DataHandler implements Serializable {
    private String queryXml;
    private Holder<String> queryID = new Holder<>();
    private Holder<Integer> errorFlag = new Holder<>(0);
    private Holder<String> errorInfo = new Holder<>();
    transient private Holder<String> returnValue = new Holder<>();
    transient private Object otherResult;

    /**
     * 等待中
     */
    transient public final static int WAIT_STATUS = 1;
    /**
     * 查询中
     */
    transient public final static int QUERY_STATUS = 2;
    /**
     * 完成
     */
    transient public final static int COMPLETE_STATUS = 3;

    public String getQueryXml() {
        return queryXml;
    }

    public void setQueryXml(String queryXml) {
        this.queryXml = queryXml;
    }

    public Holder<String> getQueryID() {
        return queryID;
    }

    public void setQueryID(Holder<String> queryID) {
        this.queryID = queryID;
    }

    public Holder<Integer> getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(Holder<Integer> errorFlag) {
        this.errorFlag = errorFlag;
    }

    public Holder<String> getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(Holder<String> errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Holder<String> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Holder<String> returnValue) {
        this.returnValue = returnValue;
    }

    public Object getOtherResult() {
        return otherResult;
    }

    public void setOtherResult(Object otherResult) {
        this.otherResult = otherResult;
    }
}
