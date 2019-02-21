
package com.dxss.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "QueryServicePortType", targetNamespace = "http://ws.dxss.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface QueryServicePortType {


    /**
     * 
     * @param queryXml
     * @param errorInfo
     * @param errorFlag
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Alarm")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "Alarm", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.Alarm")
    @ResponseWrapper(localName = "AlarmResponse", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.AlarmResponse")
    public String alarm(
        @WebParam(name = "QueryXml", targetNamespace = "")
        String queryXml,
        @WebParam(name = "ErrorFlag", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<Integer> errorFlag,
        @WebParam(name = "ErrorInfo", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> errorInfo);

    /**
     * 
     * @param queryXml
     * @param errorInfo
     * @param errorFlag
     * @param queryID
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "QueryData")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "QueryData", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.QueryData")
    @ResponseWrapper(localName = "QueryDataResponse", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.QueryDataResponse")
    public String queryData(
        @WebParam(name = "QueryXml", targetNamespace = "")
        String queryXml,
        @WebParam(name = "QueryID", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> queryID,
        @WebParam(name = "ErrorFlag", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<Integer> errorFlag,
        @WebParam(name = "ErrorInfo", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> errorInfo);

    /**
     * 
     * @param queryID
     * @return
     *     returns java.lang.Integer
     */
    @WebMethod(operationName = "QueryStatus")
    @WebResult(name = "Status", targetNamespace = "")
    @RequestWrapper(localName = "QueryStatus", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.QueryStatus")
    @ResponseWrapper(localName = "QueryStatusResponse", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.QueryStatusResponse")
    public Integer queryStatus(
        @WebParam(name = "QueryID", targetNamespace = "")
        String queryID);

    /**
     * 
     * @param queryXml
     * @param errorInfo
     * @param errorFlag
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Statistics")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "Statistics", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.Statistics")
    @ResponseWrapper(localName = "StatisticsResponse", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.StatisticsResponse")
    public String statistics(
        @WebParam(name = "QueryXml", targetNamespace = "")
        String queryXml,
        @WebParam(name = "ErrorFlag", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<Integer> errorFlag,
        @WebParam(name = "ErrorInfo", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> errorInfo);

    /**
     * 
     * @param queryXml
     * @param errorInfo
     * @param errorFlag
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "BasicResource")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "BasicResource", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.BasicResource")
    @ResponseWrapper(localName = "BasicResourceResponse", targetNamespace = "http://ws.dxss.com/", className = "com.dxss.ws.BasicResourceResponse")
    public String basicResource(
        @WebParam(name = "QueryXml", targetNamespace = "")
        String queryXml,
        @WebParam(name = "ErrorFlag", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<Integer> errorFlag,
        @WebParam(name = "ErrorInfo", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> errorInfo);

}
