package org.pecker.proxy.reflect;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Test;
import org.pecker.proxy.support.factory.ProxyBuilder;
import org.pecker.proxy.support.factory.ProxyConditionFilter;
import org.pecker.proxy.support.handler.ProxyHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class InvincibleMethodFactoryTest {

    @Test
    public void test() throws Throwable {
        List<InvincibleMethod> invincibleMethods = InvincibleMethodFactory.create(TestLL.class);
        InvincibleMethod invincibleMethod = invincibleMethods.stream().filter(item->item.getName().equals("testA")).findFirst().orElse(null);
        TestLL test = new TestLL();
        System.out.println(invincibleMethod.getParameterTypes().length);
        invincibleMethod.invoke(test);
    }


    public static class User{
        public Object test(String a){
            a="1123";
            System.out.println(a);
            return 0;
        }
    }

    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProxyBuilder builder = new ProxyBuilder("UserPeox", User.class, null, new ProxyConditionFilter() {
            @Override
            public boolean shouldProxy(Method method) {
                if (method.getName().equalsIgnoreCase("test")) {
                    return true;
                }else {
                    return false;
                }
            }
        });
        User user = new User();
        User proxy = (User) builder.build().createInstance(new ProxyHandler() {

            @Override
            public Object invoke(Object proxy, InvincibleMethod method, Object... args) {
                try {
                    System.out.println("21212");
                    return user.test((String) args[0]);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }
        },user);
        long now = System.currentTimeMillis();
        for (long i = 0 ;i<1;i++) {
            proxy.test("12121");
        }
        System.out.println(System.currentTimeMillis()-now);

        now = System.currentTimeMillis();
        for (long i = 0 ;i<1;i++) {
            user.test("12121");
        }
        System.out.println(System.currentTimeMillis()-now);
    }


    public static class TestLL{
        public void testA(){
            System.out.println("12312312");
            return;
        }
    }
}