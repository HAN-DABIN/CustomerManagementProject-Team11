package com.example.customermanagementprojectteam11.admin.service;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
