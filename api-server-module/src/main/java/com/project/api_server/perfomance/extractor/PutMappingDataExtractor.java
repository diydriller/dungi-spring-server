package com.project.api_server.perfomance.extractor;

import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PutMappingDataExtractor implements AnnotationDataExtractor{
    @Override
    public boolean isAssignable(Method method) {
        return method.isAnnotationPresent(PutMapping.class);
    }

    @Override
    public RequestApi extractRequestApi(Method method, String url) {
        final PutMapping putMapping = method.getAnnotation(PutMapping.class);
        final String api = Arrays.stream(putMapping.value())
                .findAny()
                .orElseGet(() ->
                        Arrays.stream(putMapping.path()).findAny().orElse("")
                );
        return new RequestApi(url + api, "POST");
    }
}
