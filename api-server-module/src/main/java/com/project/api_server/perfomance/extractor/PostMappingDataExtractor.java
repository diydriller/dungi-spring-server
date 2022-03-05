package com.project.api_server.perfomance.extractor;

import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PostMappingDataExtractor implements AnnotationDataExtractor{
    @Override
    public boolean isAssignable(Method method) {
        return method.isAnnotationPresent(PostMapping.class);
    }

    @Override
    public RequestApi extractRequestApi(Method method, String url) {
        final PostMapping postMapping = method.getAnnotation(PostMapping.class);
        final String api = Arrays.stream(postMapping.value())
                .findAny()
                .orElseGet(() ->
                        Arrays.stream(postMapping.path()).findAny().orElse("")
                );
        return new RequestApi(url + api, "POST");
    }
}
