package org.pecker.core.support.handler.impl;

import com.esotericsoftware.reflectasm.MethodAccess;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import org.pecker.core.support.handler.FlexibleHandler;

public class FlexibleHandlerImpl implements FlexibleHandler {

    @Override
    public Object invoke(Object proxy, MethodAccess method, Object[] args) {
        return null;
    }

    public static void main(String[] args) {
        //该设置用于输出cglib动态代理产生的类
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\cglibProxyClass");
//        Enhancer enhancer = new Enhancer();
//        //继承被代理类
//        enhancer.setSuperclass(User.class);
//        //设置回调
//        enhancer.setCallback(new UserMethodInterceptor());
//        //生成代理类对象
//        User user = (User)enhancer.create();
//        //在调用代理类中方法时会被我们实现的方法拦截器进行拦截
//        user.methodPublic1();
//        user.methodPublic2("22222222");
//        user.defaultMethod1(111);
//        user.defaultMethod2();

    }
}
