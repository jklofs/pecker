package org.pecker.proxy.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.pecker.proxy.reflect.InvincibleMethod;
import org.pecker.proxy.reflect.InvincibleMethodFactory;
import org.pecker.proxy.support.handler.AroundProxyHandler;

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

    public Object createInstance(AroundProxyHandler aroundProxyHandler, Object... instances) throws NoSuchMethodException, IllegalAccessException
            , InvocationTargetException, InstantiationException {
        List<InvincibleMethod> invincibleMethodList = Arrays.stream(instances).map(item-> InvincibleMethodFactory.create(item.getClass()))
                .flatMap(Collection::stream).collect(Collectors.toList());
       List<InvincibleMethod> methods = methodSignList.stream().map(item->invincibleMethodList.stream()
                .filter(invincibleMethod->invincibleMethod.getName().equals(item.getMethodName())
                        &&Arrays.equals(invincibleMethod.getParameterTypes(),item.getParameterTypes())).findFirst().orElse(null)).collect(Collectors.toList());
       Object[] args = new Object[instances.length+2];
       args[0] = aroundProxyHandler;
       args[1] = methods.toArray(new InvincibleMethod[0]);
       for (int i = 0 ;i<instances.length;i++){
           args[i+2]=instances[i];
       }
       return ctClass.getDeclaredConstructor(constructorNeedClassList.toArray(new Class[0])).newInstance(args);
    }

}
