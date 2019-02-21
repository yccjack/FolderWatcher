
package com.dxss.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.dxss.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryDataResponse_QNAME = new QName("http://ws.dxss.com/", "QueryDataResponse");
    private final static QName _StatisticsResponse_QNAME = new QName("http://ws.dxss.com/", "StatisticsResponse");
    private final static QName _QueryStatus_QNAME = new QName("http://ws.dxss.com/", "QueryStatus");
    private final static QName _Statistics_QNAME = new QName("http://ws.dxss.com/", "Statistics");
    private final static QName _BasicResource_QNAME = new QName("http://ws.dxss.com/", "BasicResource");
    private final static QName _BasicResourceResponse_QNAME = new QName("http://ws.dxss.com/", "BasicResourceResponse");
    private final static QName _QueryStatusResponse_QNAME = new QName("http://ws.dxss.com/", "QueryStatusResponse");
    private final static QName _Alarm_QNAME = new QName("http://ws.dxss.com/", "Alarm");
    private final static QName _AlarmResponse_QNAME = new QName("http://ws.dxss.com/", "AlarmResponse");
    private final static QName _QueryData_QNAME = new QName("http://ws.dxss.com/", "QueryData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.dxss.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BasicResourceResponse }
     * 
     */
    public BasicResourceResponse createBasicResourceResponse() {
        return new BasicResourceResponse();
    }

    /**
     * Create an instance of {@link QueryStatusResponse }
     * 
     */
    public QueryStatusResponse createQueryStatusResponse() {
        return new QueryStatusResponse();
    }

    /**
     * Create an instance of {@link AlarmResponse }
     * 
     */
    public AlarmResponse createAlarmResponse() {
        return new AlarmResponse();
    }

    /**
     * Create an instance of {@link QueryData }
     * 
     */
    public QueryData createQueryData() {
        return new QueryData();
    }

    /**
     * Create an instance of {@link Alarm }
     * 
     */
    public Alarm createAlarm() {
        return new Alarm();
    }

    /**
     * Create an instance of {@link StatisticsResponse }
     * 
     */
    public StatisticsResponse createStatisticsResponse() {
        return new StatisticsResponse();
    }

    /**
     * Create an instance of {@link QueryDataResponse }
     * 
     */
    public QueryDataResponse createQueryDataResponse() {
        return new QueryDataResponse();
    }

    /**
     * Create an instance of {@link BasicResource }
     * 
     */
    public BasicResource createBasicResource() {
        return new BasicResource();
    }

    /**
     * Create an instance of {@link QueryStatus }
     * 
     */
    public QueryStatus createQueryStatus() {
        return new QueryStatus();
    }

    /**
     * Create an instance of {@link Statistics }
     * 
     */
    public Statistics createStatistics() {
        return new Statistics();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "QueryDataResponse")
    public JAXBElement<QueryDataResponse> createQueryDataResponse(QueryDataResponse value) {
        return new JAXBElement<QueryDataResponse>(_QueryDataResponse_QNAME, QueryDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatisticsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "StatisticsResponse")
    public JAXBElement<StatisticsResponse> createStatisticsResponse(StatisticsResponse value) {
        return new JAXBElement<StatisticsResponse>(_StatisticsResponse_QNAME, StatisticsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "QueryStatus")
    public JAXBElement<QueryStatus> createQueryStatus(QueryStatus value) {
        return new JAXBElement<QueryStatus>(_QueryStatus_QNAME, QueryStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Statistics }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "Statistics")
    public JAXBElement<Statistics> createStatistics(Statistics value) {
        return new JAXBElement<Statistics>(_Statistics_QNAME, Statistics.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "BasicResource")
    public JAXBElement<BasicResource> createBasicResource(BasicResource value) {
        return new JAXBElement<BasicResource>(_BasicResource_QNAME, BasicResource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BasicResourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "BasicResourceResponse")
    public JAXBElement<BasicResourceResponse> createBasicResourceResponse(BasicResourceResponse value) {
        return new JAXBElement<BasicResourceResponse>(_BasicResourceResponse_QNAME, BasicResourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "QueryStatusResponse")
    public JAXBElement<QueryStatusResponse> createQueryStatusResponse(QueryStatusResponse value) {
        return new JAXBElement<QueryStatusResponse>(_QueryStatusResponse_QNAME, QueryStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Alarm }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "Alarm")
    public JAXBElement<Alarm> createAlarm(Alarm value) {
        return new JAXBElement<Alarm>(_Alarm_QNAME, Alarm.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AlarmResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "AlarmResponse")
    public JAXBElement<AlarmResponse> createAlarmResponse(AlarmResponse value) {
        return new JAXBElement<AlarmResponse>(_AlarmResponse_QNAME, AlarmResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dxss.com/", name = "QueryData")
    public JAXBElement<QueryData> createQueryData(QueryData value) {
        return new JAXBElement<QueryData>(_QueryData_QNAME, QueryData.class, null, value);
    }

}
