package com.ws718.abst;

/**
 * @author :MysticalYcc
 * @date :10:04 2019/2/21
 */
public interface Strategy718 {
    /**
     * 同步查询;
     */
    String QUERY_DATA_SYC = "queryData1";
    /**
     *异步查询
     */
    String QUERY_DATA_ASY = "queryData2";
    /**
     * 查询状态
     */
    String QUERY_STATUS = "queryStatus";
    /**
     *
     */
    String STATISTICS = "statistics";
    /**
     *
     */
    String BASIC_RESOURCE = "basicResource";
    /**
     * 中标查询
     */
    String ALARM = "alarm";

    void doOperate(String target);
}
