package com.banking.api.resources;

import com.banking.api.domain.Account;
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
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id){
        Customer customer = customerService.fetchCustomerById(id);
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
    public ResponseEntity<Map<String, Boolean>> updateCustomer(@PathVariable("id") Long id,
                                                               @RequestBody Customer customer){
        customerService.updateCustomer(id, customer);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCustomer(@PathVariable("id") Long id){
        customerService.removeCustomer(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllCustomersAccounts(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        List<Account> account = customerService.fetchAllCustomersAccount(userId);
        return  new ResponseEntity<>(account, HttpStatus.OK);
    }
}
