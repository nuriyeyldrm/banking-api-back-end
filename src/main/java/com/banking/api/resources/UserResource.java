package com.banking.api.resources;

import com.banking.api.config.Constants;
import com.banking.api.domain.User;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

//    @GetMapping(" ")
//    public ResponseEntity<List<User>> getAllUsers(){
//        List<User> users = userService.fetchAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

    @GetMapping("/auth")
    public ResponseEntity<User> getUserById(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        User user = userService.fetchUserById(id);
        return  new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        String ssn = (String) userMap.get("ssn");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(ssn, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(@RequestBody Map<String, Object> userMap){
        String ssn = (String) userMap.get("ssn");
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String address = (String) userMap.get("address");
        String mobilePhoneNumber = (String) userMap.get("mobilePhoneNumber");
        String createdBy = firstName + " " + lastName;
        String lastModifiedBy = firstName + " " + lastName;
        Date date= new Date();
        long time = date.getTime();
        Timestamp createdDate = new Timestamp(time);
        Timestamp lastModifiedDate = new Timestamp(time);
        User user = userService.registerUser(ssn, firstName, lastName, email, password, address, mobilePhoneNumber,
                createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
                                                               @RequestBody User user){
        Long id = (Long) request.getAttribute("id");
        userService.updateUser(id, user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/auth/password")
    public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request,
                                                            @RequestBody Map<String, Object> userMap){
        Long id = (Long) request.getAttribute("id");
        String old_password = (String) userMap.get("currentPassword");
        String new_password = (String) userMap.get("newPassword");
        userService.updatePassword(id, new_password, old_password);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> deleteUser(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        userService.removeUser(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("id", user.getId())
                .claim("snn", user.getSsn())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstname())
                .claim("lastName", user.getLastname())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("id_token", token);
        return map;
    }
}
