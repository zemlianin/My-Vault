package org.example.models;

import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;
import java.util.List;

public class ListSecretsResponse extends BaseResponse {
    List<HideVersionOfSecret> secrets;

    public ListSecretsResponse() {
    }

    public ListSecretsResponse(List<HideVersionOfSecret> secrets) {
        this.secrets = secrets;
    }

    public List<HideVersionOfSecret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<HideVersionOfSecret> secrets) {
        this.secrets = secrets;
    }
}
