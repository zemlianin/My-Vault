package org.example.models.dao;

import org.example.models.HideVersionOfSecret;
import org.example.models.dao.BaseResponse;

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
