package com.rotar.PhotoEditorWeb.Services;


public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);

}
