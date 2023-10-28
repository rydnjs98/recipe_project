package com.example.recipe_project;

import java.util.List;

public class User {
    private String Username;
    private String Student_id;
    private String ID;
    private String Password;
    private String Email;
    private String is_professor;
    private String Time;
    private String CsCheck;

    public static List<User> userList;

    public void Username(String username) {
        this.Username = username;
    }

    public void Student_id(String sid) {
        this.Student_id = sid;
    }

    public void ID(String id) {
        this.ID = id;
    }

    public void Password(String pwd) {
        this.Password = pwd;
    }

    public void Email(String email) {
        this.Email = email;
    }

    public void is_professor(String isp) {
        this.is_professor = isp;
    }

    public void Time(String time) {
        this.Time = time;
    }

    public void CsCheck(String csc) {
        this.CsCheck = csc;
    }

    // get 메소드
    public static String getUsername() {
        if (!userList.isEmpty()) {
            return userList.get(0).Username;
        }
        return null;
    }

    public static String getStudentId() {
        if (!userList.isEmpty()) {
            return userList.get(0).Student_id;
        }
        return null;
    }

    public static String getID() {
        if (!userList.isEmpty()) {
            return userList.get(0).ID;
        }
        return null;
    }

    public static String getPassword() {
        if (!userList.isEmpty()) {
            return userList.get(0).Password;
        }
        return null;
    }

    public static String getEmail() {
        if (!userList.isEmpty()) {
            return userList.get(0).Email;
        }
        return null;
    }

    public static String getpro() {
        if (!userList.isEmpty()) {
            return userList.get(0).is_professor;
        }
        return null;
    }
    public static boolean is_professor() {
        if (!userList.isEmpty() && userList.get(0).is_professor.equals("1")) {
            return true;
        } else {
            return false;
        }
    }


    public static String getTime() {
        if (!userList.isEmpty()) {
            return userList.get(0).Time;
        }
        return null;
    }

    public static String getCsCheck() {
        if (!userList.isEmpty()) {
            return userList.get(0).CsCheck;
        }
        return null;
    }
}