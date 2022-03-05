package com.project.api_server.perfomance.extractor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class RequestPathExtractor {

    private final List<AnnotationDataExtractor> dataExtractorList=new ArrayList<>();

    RequestPathExtractor(){
        dataExtractorList.add(new PostMappingDataExtractor());
        dataExtractorList.add(new GetMappingDataExtractor());
        dataExtractorList.add(new PutMappingDataExtractor());
        dataExtractorList.add(new DeleteMappingDataExtractor());
    }

    // RestController에서 RequestMapping의 value를 읽어온다.
    public String getUrl(JoinPoint joinPoint){
        final Class<?> targetClass=joinPoint.getTarget().getClass();

        if(targetClass.isAnnotationPresent(RequestMapping.class)){
            final RequestMapping requestMapping=targetClass.getAnnotation(RequestMapping.class);
            return Arrays.stream(requestMapping.value())
                    .findAny()
                    .orElse("");
        }
        return "";
    }

    // RequestMapping과 http method annotation를 통해서 전체 url과 http method를 알아낸다.
    public RequestApi extractRequestApi(JoinPoint joinPoint){
        String url=getUrl(joinPoint);
        final MethodSignature methodSignature=(MethodSignature) joinPoint.getSignature();
        final Optional<AnnotationDataExtractor> extractor = dataExtractorList.stream()
                .filter(annotationDataExtractor -> annotationDataExtractor
                        .isAssignable(methodSignature.getMethod()))
                .findAny();
        return extractor.get().extractRequestApi(methodSignature.getMethod(),url);
    }
}
