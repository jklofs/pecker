package org.pecker.proxy.support;

import lombok.*;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MethodSign {

    private String methodName;

    private Class[] parameterTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodSign)) return false;
        MethodSign that = (MethodSign) o;
        return Objects.equals(getMethodName(), that.getMethodName()) &&
                Arrays.equals(getParameterTypes(), that.getParameterTypes());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getMethodName());
        result = 31 * result + Arrays.hashCode(getParameterTypes());
        return result;
    }
}
