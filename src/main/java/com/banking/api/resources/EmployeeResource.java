package com.banking.api.resources;

import com.banking.api.domain.Employee;
import com.banking.api.services.EmployeeService;
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
@RequestMapping("/api/employees")
public class EmployeeResource {

    @Autowired
    EmployeeService employeeService;

    @GetMapping(" ")
    public ResponseEntity<List<Employee>> getAllEmployees(){
        List<Employee> employees = employeeService.fetchAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.fetchEmployeeById(id);
        return  new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping(" ")
    public ResponseEntity<Employee> addEmployee(HttpServletRequest request,
                                                @RequestBody Map<String, Object> employeeMap){
        Long userId = (Long) request.getAttribute("id");
        String firstName = (String) employeeMap.get("firstName");
        String lastName = (String) employeeMap.get("lastName");
        String email = (String) employeeMap.get("email");
        Date date= new Date();
        long time = date.getTime();
        Timestamp hiredDate = new Timestamp(time);
        String mobilePhoneNumber = (String) employeeMap.get("mobilePhoneNumber");
        String phoneNumber = (String) employeeMap.get("phoneNumber");
        String zipCode = (String) employeeMap.get("zipCode");
        String address = (String) employeeMap.get("address");
        String state = (String) employeeMap.get("state");
        String city = (String) employeeMap.get("city");
        String country = (String) employeeMap.get("country");
        String ssn = (String) employeeMap.get("ssn");
        Timestamp createdDate = new Timestamp(time);
        Employee employee = employeeService.addEmployee(userId, firstName, lastName, email, hiredDate,
                mobilePhoneNumber, phoneNumber, zipCode, address, state, city, country, ssn, createdDate);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> updateEmployee(@PathVariable("id") Long id,
                                                               @RequestBody Employee employee){
        employeeService.updateEmployee(id, employee);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable("id") Long id){
        employeeService.removeEmployee(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
