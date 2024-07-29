package com.ezeeinfo.dto;


/**
 * AuthResponseDto class.
 */
public class AuthResponseDto {

    /**
     * accessToken.
     */
    private String accessToken;
    /**
     * tokenType initialization.
     */
    private String tokenType = "Bearer ";

    /**
     * getAccessToken method.
     * @return accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * SetAccessToken method.
     * @param token
     */
    public void setAccessToken(final String token) {
        this.accessToken = token;
    }
    /**
     * getTokenType method.
     * @return tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * setTokenType method.
     * @param type
     */

    public void setTokenType(final String type) {
        this.tokenType = type;
    }

    /**
     * AuthResponseDto constructor.
     * @param token
     */
    public AuthResponseDto(final String token) {
        this.accessToken = token;
    }
}