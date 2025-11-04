package com.custard.account_service.adapter.dto.reponses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String code = "00";
    private String message = "";

    private T data;
}
