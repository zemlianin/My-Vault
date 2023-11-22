package org.example.models.dao.response;

public class JwtAuthenticationResponse {
    private String token;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    private JwtAuthenticationResponse(Builder builder) {
        this.token = builder.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public static class Builder {
        private String token;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public JwtAuthenticationResponse build() {
            return new JwtAuthenticationResponse(this);
        }
    }
}