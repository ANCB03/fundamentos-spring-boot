package com.fundamentos.springboot.fundamentos.bean;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class MyOperationImplement implements MyOperation{
    Log LOGGER = LogFactory.getLog(MyOperationImplement.class);
    @Override
    public int sum(int number) {
        LOGGER.info("Ingresó a MyOperationImplement");
        return number+1;
    }
}
