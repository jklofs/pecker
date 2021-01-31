package org.pecker.proxy.support;

import lombok.*;

@Data
@AllArgsConstructor
public class MethodSign {

    private String methodName;

    private Class[] parameterTypes;
}
