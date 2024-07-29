package com.ezeeinfo.dto;

/**
 * AuthResponseDto class
 */
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    /**
     *
     * @return
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     *
     * @param tokenType
     */

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     *
     * @param accessToken
     */
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}