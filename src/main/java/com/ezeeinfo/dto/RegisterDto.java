package com.ezeeinfo.dto;


/**
 * RegisterDto class.
 */
public class RegisterDto {
    /**
     * username declaration using String.
     */
    private String username;
    /**
     * password declaration using String.
     */
    private String password;
    /**
     * role declaration using String.
     */
    private String role;

    /**
     * getUsername method.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setUsername method.
     * @param user
     */
    public void setUsername(final String user) {
        this.username = user;
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

    /**
     * getRole method.
     * @return role
     */
    public final String getRole() {
        return role;
    }

    /**
     * setRole method.
     * @param roles
     */
    public void setRole(final String roles) {
        this.role = roles;
    }
}
