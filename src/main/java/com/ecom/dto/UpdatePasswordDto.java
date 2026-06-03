package com.ecom.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
