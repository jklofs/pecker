package org.pecker.proxy.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.pecker.proxy.reflect.InvincibleMethod;
import org.pecker.proxy.reflect.InvincibleMethodFactory;
import org.pecker.proxy.support.handler.ProxyHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@ToString
public class BeanCreateProxy {

    @NonNull
    private Class ctClass;

    @NonNull
    private List<MethodSign> methodSignList;

    @NonNull
    private List<Class> constructorNeedClassList;

    public Object createInstance(ProxyHandler proxyHandler, Object instance) throws NoSuchMethodException, IllegalAccessException
            , InvocationTargetException, InstantiationException {
        List<InvincibleMethod> invincibleMethodList = InvincibleMethodFactory.create(instance.getClass());
       List<InvincibleMethod> methods = methodSignList.stream().map(item->invincibleMethodList.stream()
                .filter(invincibleMethod->invincibleMethod.getName().equals(item.getMethodName())
                        &&Arrays.equals(invincibleMethod.getParameterTypes(),item.getParameterTypes())).findFirst().orElse(null)).collect(Collectors.toList());
       Object[] args = new Object[3];
       args[0] = proxyHandler;
       args[1] = methods.toArray(new InvincibleMethod[0]);
       args[2] = instance;
       return ctClass.getDeclaredConstructor(constructorNeedClassList.toArray(new Class[0])).newInstance(args);
    }

}
