package org.pecker.common.code;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class CodeUtilsTest {

    @Test
    public void cast() {
       System.out.println(CodeUtils.fillMethodReturnCode(float.class,Object.class,"111"));
        System.out.println(CodeUtils.fillMethodReturnCode(float.class,void.class,"111"));
    }
}
