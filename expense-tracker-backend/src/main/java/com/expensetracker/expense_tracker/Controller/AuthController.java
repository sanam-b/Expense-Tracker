package com.expensetracker.expense_tracker.Controller;

import com.expensetracker.expense_tracker.Model.User;
import com.expensetracker.expense_tracker.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody User user){
        try{
            authService.register(user);
        }
    }

}
