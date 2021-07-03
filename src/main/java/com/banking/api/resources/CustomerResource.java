package com.banking.api.resources;

import com.banking.api.domain.Customer;
import com.banking.api.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    @Autowired
    CustomerService customerService;

    @GetMapping(" ")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = customerService.fetchAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(HttpServletRequest request,
                                                    @PathVariable("id") Long id){
        Long userId = (Long) request.getAttribute("user_id");
        Customer customer = customerService.fetchCustomerById(userId, id);
        return  new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping(" ")
    public ResponseEntity<Customer> addCustomer(HttpServletRequest request,
                                                @RequestBody Map<String, Object> customerMap){
        Long userId = (Long) request.getAttribute("id");
        String firstName = (String) customerMap.get("firstName");
        String lastName = (String) customerMap.get("lastName");
        String middleInitial = (String) customerMap.get("middleInitial");
        String email = (String) customerMap.get("email");
        String mobilePhoneNumber = (String) customerMap.get("mobilePhoneNumber");
        String phoneNumber = (String) customerMap.get("phoneNumber");
        String zipCode = (String) customerMap.get("zipCode");
        String address = (String) customerMap.get("address");
        String state = (String) customerMap.get("state");
        String city = (String) customerMap.get("city");
        String country = (String) customerMap.get("country");
        String ssn = (String) customerMap.get("ssn");
        Date date= new Date();
        long time = date.getTime();
        Timestamp createdDate = new Timestamp(time);
        Customer customer = customerService.addCustomer(userId, firstName, lastName, middleInitial, email,
                mobilePhoneNumber, phoneNumber, zipCode, address, state, city, country, ssn, createdDate);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> updateCustomer(HttpServletRequest request,
                                                               @PathVariable("id") Long id,
                                                               @RequestBody Customer customer){
        Long userId = (Long) request.getAttribute("user_id");
        customerService.updateCustomer(userId, id, customer);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCustomer(HttpServletRequest request,
                                                               @PathVariable("id") Long id){
        Long userId = (Long) request.getAttribute("user_id");
        customerService.removeCustomer(userId, id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
