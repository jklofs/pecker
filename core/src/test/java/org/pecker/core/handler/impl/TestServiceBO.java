package org.pecker.core.handler.impl;

import org.pecker.core.support.annotation.Flexible;

public class TestServiceBO {

    @Flexible
    public String playGame(){
        return "start time :" +System.currentTimeMillis();
    }
}
