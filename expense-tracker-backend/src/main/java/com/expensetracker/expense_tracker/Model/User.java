package com.expensetracker.expense_tracker.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    int user_id;
    String email;
    String username;
    String pwd;
    String phone;
    int activeYN;
//    public int getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(int user_id) {
//        this.user_id = user_id;
//    }
}
