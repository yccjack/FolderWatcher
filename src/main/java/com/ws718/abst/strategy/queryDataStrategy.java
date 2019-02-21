package com.ws718.abst.strategy;

import com.ws718.abst.Strategy718;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author :MysticalYcc
 * @date :10:05 2019/2/21
 */
public abstract class queryDataStrategy implements Strategy718 {
    private Logger logger = LoggerFactory.getLogger(queryDataStrategy.class);

    @Override
    public void doOperate(String target) {

    }
}
