package com.project.api_server.perfomance.extractor;

import java.lang.reflect.Method;

public interface AnnotationDataExtractor {
    // http method annotation의 유무를 알아낸다.
    boolean isAssignable(Method method);
    // http method annotation에서 value를 읽어온다.
    RequestApi extractRequestApi(Method method, String url);
}
