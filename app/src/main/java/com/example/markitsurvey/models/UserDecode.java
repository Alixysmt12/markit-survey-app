package com.example.markitsurvey.models;

import com.example.markitsurvey.helper.JWTUtils;
import com.google.gson.Gson;

import java.io.Serializable;

public class UserDecode implements Serializable {

    private int id;
    private String name;
    private String userName;
    private String avatarPath;
    private String roleGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    public static  UserDecode ConvertToUserEntityUser(String token)
    {
        UserDecode user = null;
        try {
            String json =  JWTUtils.decoded(token);
            user = new Gson().fromJson(json,UserDecode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

    }
}
