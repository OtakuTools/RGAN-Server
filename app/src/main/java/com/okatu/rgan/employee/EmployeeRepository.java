package com.okatu.rgan.employee;

import com.okatu.rgan.employee.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {
}


