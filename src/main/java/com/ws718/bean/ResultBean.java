package com.ws718.bean;

/**
 *  * @author ycc
 */
public class ResultBean {
    private String queryXml;
    private String queryId;
    private Integer errorFlag;
    private String errorInfo;
    private Object result;

    public String getQueryXml() {
        return queryXml;
    }

    public void setQueryXml(String queryXml) {
        this.queryXml = queryXml;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Integer getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(Integer errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
