package org.pecker.proxy.support;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import javassist.*;
import lombok.Getter;
import lombok.Setter;
import org.pecker.common.beanutils.FastField;
import org.pecker.common.beanutils.ReflectUtils;

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
        ctClass.addField(new CtField(pool.get(PersonService.class.getName()),"x",ctClass));
        ctClass.addConstructor(CtNewConstructor.make(null,null,"{x = new org.pecker.proxy.support.ProxyFactory$PersonService();}",ctClass));
        ctClass.addMethod(CtNewMethod.make(pool.get(Object.class.getName()),"invoke",new CtClass[]{pool.get(Object[].class.getName())},null,"{x.setA(((java.lang.Integer)($1[0])).intValue()); return ($r)null;}",ctClass));

        MethodProxy methodProxy = (MethodProxy) ctClass.toClass().newInstance();
        long now = System.currentTimeMillis();
        Integer integer = new Integer(1);
        for (long i=0;i<10000000000L;i++) {
            methodProxy.invoke(integer);
        }
        System.out.println(System.currentTimeMillis()-now);
        PersonService personService = new PersonService();
        now = System.currentTimeMillis();
        for (long i=0;i<10000000000L;i++) {
            personService.setA(integer);
        }
        System.out.println(System.currentTimeMillis()-now);

        Method method = personService.getClass().getDeclaredMethod("setA",int.class);
        for (long i=0;i<50;i++) {
            method.invoke(personService,integer);
        }
        now = System.currentTimeMillis();
        for (long i=0;i<10000000000L;i++) {
            method.invoke(personService,integer);
        }
        System.out.println(System.currentTimeMillis()-now);


        FastField fastField = ReflectUtils.getFieldMethodAccess(PersonService.class,"a");
        now = System.currentTimeMillis();
        for (long i=0;i<10000000000L;i++) {
            fastField.setValue(personService,integer);
        }
        System.out.println(System.currentTimeMillis()-now);
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
        private int a=0;
        PersonService() {
        }

        public void setA(int a) {
            this.a = a;
        }

    }

    static interface MethodProxy{
        Object invoke(Object... args);
    }

    class test implements MethodProxy{

        @Override
        public Object invoke(Object... args) {
            PersonService personService = new PersonService();
            personService.setA((int)args[0]);
            return null;
        }
    }
}
