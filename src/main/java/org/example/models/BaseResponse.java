package org.example.models;

import org.springframework.http.HttpStatus;

public class BaseResponse {

    private String Description;

    public BaseResponse(){
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}