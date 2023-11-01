package org.example.models;

import org.springframework.http.HttpStatus;

public class BaseResponse {

    private String Description;
    private HttpStatus status;

    public BaseResponse(){
    }
    public BaseResponse(HttpStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}