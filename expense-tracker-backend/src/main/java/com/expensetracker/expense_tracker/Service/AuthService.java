package com.expensetracker.expense_tracker.Service;

import com.expensetracker.expense_tracker.Model.User;
import com.expensetracker.expense_tracker.Repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
public class AuthService {
    @Autowired
    AuthRepository authRepository;
    public void checkEmailIsValid(String email){

    }
    public void register(User user){
        if(user.email==null) return;


    }
    public void add(String username , String email, String pwd ,String phone , int activeYN){

    }

}
