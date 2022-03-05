package com.project.api_server.perfomance.extractor;

import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

public class GetMappingDataExtractor implements AnnotationDataExtractor {
    @Override
    public boolean isAssignable(Method method) {
        return method.isAnnotationPresent(GetMapping.class);
    }

    @Override
    public RequestApi extractRequestApi(Method method, String url) {
        final GetMapping getMapping = method.getAnnotation(GetMapping.class);
        final String api = Arrays.stream(getMapping.value())
                .findAny()
                .orElseGet(() ->
                        Arrays.stream(getMapping.path()).findAny().orElse("")
                );
        return new RequestApi(url + api, "GET");
    }
}
