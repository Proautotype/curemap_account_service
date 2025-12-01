package com.custard.account_service.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AppInfo {
   private  String mode;
   private String message;
   private String contactDetails;
}
