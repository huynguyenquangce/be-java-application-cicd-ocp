package com.woromedia.api.task.payload;

public class JWTAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long userId;

    public JWTAuthResponse(String accessToken, Long userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}