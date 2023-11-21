package org.example.models.dao;

public class JwtAuthenticationResponse {
    private String token;

    private JwtAuthenticationResponse(Builder builder) {
        this.token = builder.token;
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