package com.example.customermanagementprojectteam11.admin.config;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
