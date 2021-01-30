package org.pecker.proxy.support;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import javassist.*;

public class ProxyFactory {
    public ProxyFactory() {
    }

    public static void update() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("org.pecker.proxy.support.ProxyFactory$PersonService");
        CtMethod personFly = cc.getDeclaredMethod("personFly");
        personFly.insertBefore("System.out.println(\"起飞之前准备降落伞\");");
        personFly.insertAfter("System.out.println(\"成功落地。。。。\");");
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "joinFriend", new CtClass[0], cc);
        ctMethod.setModifiers(1);
        ctMethod.setBody("{System.out.println(\"i want to be your friend\");}");
        cc.addMethod(ctMethod);
        ProxyFactory.PersonService person = (ProxyFactory.PersonService)cc.toClass().newInstance();
        person.personFly();
        Method execute = person.getClass().getMethod("joinFriend");
        execute.invoke(person);
    }

    public static void create() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass ctClass = pool.makeClass("org.pecker.proxy.support.MyCC");

        // 添加一个参数
        CtField ctField = new CtField(CtClass.intType, "id", ctClass);
        ctField.setModifiers(Modifier.PUBLIC);
        ctClass.addField(ctField);
        ctClass.addInterface(pool.get("org.pecker.proxy.support.ProxyFactory$MethodProxy"));
//        CtMethod ctMethod = new CtMethod(pool.get(Object.class.getName()),"invoke",new CtClass[]{pool.get(Object[].class.getName())},ctClass);

        ctClass.addMethod(CtNewMethod.make(pool.get(Object.class.getName()),"invoke",new CtClass[]{pool.get(Object[].class.getName())},null,"{String a = (String)$1[0] ;System.out.print(a); return null;}",ctClass));

        MethodProxy methodProxy = (MethodProxy) ctClass.toClass().newInstance();
        methodProxy.invoke("dsadsadsa");
//        // 把生成的class文件写入文件
//        byte[] byteArr = ctClass.toBytecode();
//        FileOutputStream fos = new FileOutputStream(new File(".//MyCC.class"));
//        fos.write(byteArr);
//        fos.close();
//        System.out.println("over!!");
    }

    public static void main(String[] args) {
        try {
            create();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    static class PersonService {
        PersonService() {
        }

        public void getPerson() {
            System.out.println("get Person");
        }

        public void personFly() {
            System.out.println("oh my god,I can fly");
        }
    }

    static interface MethodProxy{
        Object invoke(Object... args);
    }
}
