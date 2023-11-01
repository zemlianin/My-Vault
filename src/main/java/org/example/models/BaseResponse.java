package org.example.models;

import org.springframework.http.HttpStatus;

public class BaseResponse {

    private HttpStatus status;

    public BaseResponse(){
    }
    public BaseResponse(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}