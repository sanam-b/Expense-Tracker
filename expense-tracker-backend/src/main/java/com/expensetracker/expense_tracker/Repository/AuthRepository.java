package com.expensetracker.expense_tracker.Repository;

import com.expensetracker.expense_tracker.Controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.expensetracker.expense_tracker.Model.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class AuthRepository {
    public static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    public boolean checkEmailIfExistsByGivenId(String email , int id){
        String query = " SELECT count(user_id) as count FROM users where email= ?";
        Map<String , Object> res =  jdbcTemplate.queryForMap(query,email,id);
        return (long) res.get("count")>0;
    }
    public User findByEmail(String email){
        String query = "SELECT user_id,username,email,pwd, active_yn,phone FROM users where email=?";
        try{
            User user = jdbcTemplate.queryForObject(query,(resultset,rownum)->{
                User u = new User();
                u.setEmail(resultset.getString("email"));
//                u.setName(resultset.getString("name"));
                u.setUsername(resultset.getString("username"));
                u.setPhone(resultset.getString("phone"));
                u.setPwd(resultset.getString("pwd"));
                u.setUser_id(resultset.getInt("user_id"));
                u.setActiveYN(resultset.getInt("active_yn"));
                return u;
            },email);
            return user;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public User findByUsername(String username){
        String query = "SELECT user_id,username,email,pwd, active_yn FROM users where username=?";
        try{
            User user = jdbcTemplate.queryForObject(query,(resultset,rownum)->{
                User u = new User();
                u.setEmail(resultset.getString("email"));
//                u.setName(resultset.getString("name"));
                u.setUsername(resultset.getString("username"));
                u.setPhone(resultset.getString("phone"));
                u.setPwd(resultset.getString("pwd"));
                u.setUser_id(resultset.getInt("user_id"));
                u.setActiveYN(resultset.getInt("active_yn"));
                return u;
            },username);
            return user;

        }catch (EmptyResultDataAccessException e){
            return null;
        }

    }
    public void registerUser(String username,String email, String pwd,String phone){
        String query = "INSERT INTO users (username, email, pwd, phone) VALUES (?,?,?,?)";
        jdbcTemplate.update(query,username,email,pwd,phone);
        return;
    }
    public User login(String username , String pwd){
        String query = "SELECT username , pwd FROM users where username = ? AND pwd = ?";
        try{
            User user = jdbcTemplate.queryForObject(query,(resultset,rownum)->{
                User u = new User();
                u.setEmail(resultset.getString("email"));
//                u.setName(resultset.getString("name"));
                u.setUsername(resultset.getString("username"));
                u.setPhone(resultset.getString("phone"));
                u.setPwd(resultset.getString("pwd"));
                u.setUser_id(resultset.getInt("user_id"));
                u.setActiveYN(resultset.getInt("active_yn"));
                return u;
            },username , pwd);
            return user;

        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public void saveResetToken(String token, int user_id, LocalDateTime expiry){
        String query = "INSERT INTO token(token ,user_id, expiry) VALUES(?,?,?)";
        jdbcTemplate.update(query , token ,user_id , expiry);
    }
    public Integer validateResetToken(String token){
        // in frontend we would write the validate token in ngOnInit and if it not satisfies we would navigate it
        String query="select user_id from Auth_Tokens where token = ? and expiry > current_timestamp and used_yn=0";
        try{
            return jdbcTemplate.queryForObject(query,Integer.class,token);
            // query for object ek hi object return karega instead of row mapper  / map ka obj return karne se acha we do long.class which would convert the result into long
        }
        catch(EmptyResultDataAccessException e){
            return null;
            // Long is also an object so if we return null its ok
        }
    }
    public void markTokenUsed(String token){
        String query="update Auth_Tokens set used_yn=1 where token=?";
        jdbcTemplate.update(query,token);
    }
    public void updatePassword(Integer userId,String hashPassword){
        String query="update Users set password=? where user_id=?";
        jdbcTemplate.update(query,hashPassword,userId);
        logger.info("password updated");
    }
    public void markLinkUsed(int userId){
        String query="update Auth_Tokens set used_yn=1 where user_id=?";
        jdbcTemplate.update(query,userId);
    }
}
