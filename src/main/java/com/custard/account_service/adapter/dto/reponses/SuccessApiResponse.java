package com.custard.account_service.adapter.dto.reponses;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class SuccessApiResponse<T> extends ApiResponse<T> {
    @PostConstruct
    private void setCode(){
        super.setCode("00");
    }
}
