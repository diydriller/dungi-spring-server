package com.project.api_server.auth.sns;

import com.project.api_server.auth.dto.SnsInfoDto;
import com.project.api_server.auth.dto.SnsTokenDto;

public interface SnsHttpService {
    String getSnsInfo(String token) throws Exception;
    SnsTokenDto getSnsToken(String code) throws Exception;
}
