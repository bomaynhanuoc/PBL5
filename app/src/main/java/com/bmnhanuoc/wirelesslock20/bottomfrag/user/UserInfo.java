package com.bmnhanuoc.wirelesslock20.bottomfrag.user;

public class UserInfo {

    String email;
    String password;
    String kind;
    String localId;
    String displayName;
//
    String idToken;
    boolean registered;
    String refreshToken;
    long expiresln;

    public UserInfo(String email, String password, String kind, String localId, String displayName, String idToken, boolean registered, String refreshToken, long expiresln) {
        this.email = email;
        this.password = password;
        this.kind = kind;
        this.localId = localId;
        this.displayName = displayName;
        this.idToken = idToken;
        this.registered = registered;
        this.refreshToken = refreshToken;
        this.expiresln = expiresln;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresln() {
        return expiresln;
    }

    public void setExpiresln(long expiresln) {
        this.expiresln = expiresln;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", kind='" + kind + '\'' +
                ", localId='" + localId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", idToken='" + idToken + '\'' +
                ", registered=" + registered +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresln=" + expiresln +
                '}';
    }
}
