package com.expensetracker.expense_tracker.Service;

import com.expensetracker.expense_tracker.Controller.AuthController;
import com.expensetracker.expense_tracker.Exceptions.EmailAlreadyExistsException;
import com.expensetracker.expense_tracker.Exceptions.InvalidEmailException;
import com.expensetracker.expense_tracker.Model.User;
import com.expensetracker.expense_tracker.Repository.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {
    public static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthRepository authRepository;
    @Autowired
    EmailService emailService;
    public boolean checkEmailIsValid(String email){
        if(email==null) return false;

        String EMAIL_PATTERN =  "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    public boolean checkIfEmailExists(String email){
        User user = authRepository.findByEmail(email);
        if(user!=null)
            return true;

        return false;
    }

    //check if username exists
    public boolean checkIfUsernameExists(String username){
        User user = authRepository.findByUsername(username);
        if(user!=null)
            return true;

        return false;

    }
    public String encodedPassword(String pwd){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pwd);
    }
    public void registerUser(User user) throws InvalidEmailException , EmailAlreadyExistsException{
        if(!checkEmailIsValid(user.getEmail())){
            throw new InvalidEmailException();
        }
        if(checkIfEmailExists(user.getEmail())){
            throw new EmailAlreadyExistsException();
        }


        //check if username already exists(function name - find by username)
//        if(checkIfUsernameExists(user.getUsername())){
//            throw new UsernameAlreadyExistsException();
//        }

        authRepository.registerUser(
                user.getUsername(),
                user.getEmail(),
                encodedPassword(user.getPwd()),
                user.getPhone()
        );
        //hash password before register
        //if all passed then call repository to register

//        authRepository.registerUser(user.getUsername(), user.getEmail(), user.getPwd(), user.getPhone());

    }

    public User login(User user) throws InvalidEmailException{
        if(!checkEmailIsValid(user.getEmail())){
            throw new InvalidEmailException();
        }
        return authRepository.login(user.getUsername(), user.getPwd());
    }

    public void forgotPwd(String email) throws InvalidEmailException{
//        String email = body.get("email");
        User user = authRepository.findByEmail(email);
        logger.info(user.getEmail());
        int user_id;
//        if(checkIfEmailExists(email)){
//            throw new EmailAlreadyExistsException();
//        }
        if(user==null) throw new InvalidEmailException();
        else{
            user_id= user.getUser_id();
        }
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
        logger.info(token);

        authRepository.saveResetToken(token,user_id ,expiry );
        emailService.sendEmail(email,token);

        String resetLink = " http://localhost:5173/reset?token="+token;

        emailService.sendResetLink(email , resetLink);

    }

    public void resetPwd(String token , String newPwd){
        Integer user_id = authRepository.validateResetToken(token);
        if(user_id==null){
            throw new RuntimeException("Invalid or expired token");
        }
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String hashedPassword=encoder.encode(newPwd);
        authRepository.updatePassword(user_id,hashedPassword);
        authRepository.markTokenUsed(token);

    }
    public void checkTokenValidity(String token) throws RuntimeException{
        Integer userId=authRepository.validateResetToken(token);
        if(userId==null){
            throw new RuntimeException("Invalid or expired token");
        }

    }
}
