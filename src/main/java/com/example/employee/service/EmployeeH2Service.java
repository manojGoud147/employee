/*
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 * 
 */

// Write your code here
package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRowMapper;
import com.example.employee.repository.EmployeeRepository;

@Service
public class EmployeeH2Service implements EmployeeRepository{
    @Autowired
    JdbcTemplate db;

    @Override
    public ArrayList<Employee> getEmployees(){
        List<Employee> employeeList = db.query("select * from EMPLOYEELIST", new EmployeeRowMapper());
        ArrayList<Employee> employees = new ArrayList<>(employeeList);
        return employees;
    }
    @Override
    public Employee getEmployeeById(int employeeId){
        try{
            Employee employee = db.queryForObject("select * from EMPLOYEELIST where employeeId = ?", new EmployeeRowMapper(),employeeId );
            return employee;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public Employee addEmployee(Employee employee){
        
		db.update("insert into employeelist(employeeid,employeename,email,department) values (?,?,?,?)",employee.getEmployeeId() , employee.getEmployeeName(), employee.getEmail(),employee.getDepartment());

        Employee savingEmployee = db.queryForObject("select * from employeelist where employeename = ? and email = ? and department = ?", new EmployeeRowMapper(), employee.getEmployeeName(),employee.getEmail(),employee.getDepartment());
        return savingEmployee;
    }
    @Override
    public Employee updateEmployee(int employeeId, Employee employee){

        if(employee.getEmployeeName() != null){
            db.update("update employeelist set employeename = ? where employeeid = ?", employee.getEmployeeName(), employeeId);
        }
        if(employee.getEmail() != null){
            db.update("update employeelist set email = ? where employeeid = ?", employee.getEmail(), employeeId);
        }
        if(employee.getDepartment() != null){
            db.update("update employeelist set department = ? where employeeid = ?", employee.getDepartment(), employeeId);
        }
        return getEmployeeById(employeeId);
    }
    @Override
    public void deleteEmployee(int employeeId){
        db.update("delete from employeelist where employeeid = ?", employeeId);
    }
}