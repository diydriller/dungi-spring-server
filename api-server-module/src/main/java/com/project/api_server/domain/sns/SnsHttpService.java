package com.project.api_server.domain.sns;

import com.project.api_server.application.user.dto.SnsTokenDto;

public interface SnsHttpService {
    String getSnsInfo(String token) throws Exception;
    SnsTokenDto getSnsToken(String code) throws Exception;
}
