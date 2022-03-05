package com.project.api_server.perfomance.extractor;

import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

public class DeleteMappingDataExtractor implements AnnotationDataExtractor{
    @Override
    public boolean isAssignable(Method method) {
        return method.isAnnotationPresent(DeleteMapping.class);
    }

    @Override
    public RequestApi extractRequestApi(Method method, String url) {
        final DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        final String api = Arrays.stream(deleteMapping.value())
                .findAny()
                .orElseGet(() ->
                        Arrays.stream(deleteMapping.path()).findAny().orElse("")
                );
        return new RequestApi(url + api, "DELETE");
    }
}
