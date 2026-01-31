package com.expensetracker.expense_tracker.Controller;

import com.expensetracker.expense_tracker.Exceptions.EmailAlreadyExistsException;
import com.expensetracker.expense_tracker.Exceptions.InvalidEmailException;
import com.expensetracker.expense_tracker.Model.User;
import com.expensetracker.expense_tracker.Service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    public static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerUser(@RequestBody User user) {
        try{
            authService.registerUser(user);
            return ResponseEntity.ok().body(Map.of("body" , "Inserted succesfully"));
        }catch(InvalidEmailException | EmailAlreadyExistsException e){
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("body" ,e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String , Object>> login(@RequestBody User user){
        try{
            User loggedInUser = authService.login(user);
            return ResponseEntity.ok().body(Map.of("body" ,loggedInUser ));
        }catch(InvalidEmailException e){
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("body" ,e.getMessage()));
        }
    }
    @PostMapping("/forgot-password")
    public void forgotpwd(@RequestBody Map<String , String>body){
        String email = body.get("email");
        try{
            authService.forgotPwd(email);
        } catch (InvalidEmailException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPwd(@RequestBody Map<String , String>body){
        authService.resetPwd(body.get("token"), body.get("pwd"));
        return ResponseEntity.ok().body("Password reset successfully");
    }
    @PostMapping("/check-token-validity")
    public ResponseEntity<Map<String,String>> checkTokenValidity(@RequestBody Map<String,String> body){
        try{
            authService.checkTokenValidity(body.get("token"));
            logger.info("token is valid");
            return ResponseEntity.ok().body(Map.of("body","token is valid"));

        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("body",e.getMessage()));
        }

    }
}
