package com.ezeeinfo.dto;


/**
 * LoginDto class.
 */
public class LoginDto {
    /**
     * username declaration using String.
     */
    private String username;
    /**
     * password declaration using String.
     */
    private String password;

    /**
     * getUsername method.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setUsername method.
     * @param userName
     */
    public void setUsername(final String userName) {
        this.username = userName;
    }

    /**
     * getPassword method.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword method.
     * @param pwd
     */
    public void setPassword(final String pwd) {
        this.password = pwd;
    }

}
