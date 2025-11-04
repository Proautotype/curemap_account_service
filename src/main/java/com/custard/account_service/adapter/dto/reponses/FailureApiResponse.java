package com.custard.account_service.adapter.dto.reponses;

import jakarta.annotation.PostConstruct;

public class FailureApiResponse<String> extends ApiResponse<String> {
    @PostConstruct
    public void setCode() {
        super.setCode("01");
    }
}
