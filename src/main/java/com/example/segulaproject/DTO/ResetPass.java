package com.example.segulaproject.DTO;

import lombok.Data;

@Data
public class ResetPass {
    String oldPassword;
    String newPassword;
    String code;
}
