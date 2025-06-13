package com.example.computer_item_repair.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//ProjectID Exception class to throw exceptions related to ProjectID
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdException extends RuntimeException{

    public ProjectIdException(String message) {
        super(message);
    }
}
