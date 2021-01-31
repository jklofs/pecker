package org.pecker.proxy.reflect;

import org.junit.Test;

import java.util.List;

public class InvincibleMethodFactoryTest {

    @Test
    public void test() throws Exception {
        List<InvincibleMethod> invincibleMethods = InvincibleMethodFactory.create(TestLL.class);
        InvincibleMethod invincibleMethod = invincibleMethods.stream().filter(item->item.getName().equals("testA")).findFirst().orElse(null);
        TestLL test = new TestLL();
        System.out.println(invincibleMethod.getParameterTypes().length);
        invincibleMethod.invoke(test);
    }


    public static class TestLL{
        public void testA(){
            System.out.println("12312312");
            return;
        }
    }
}