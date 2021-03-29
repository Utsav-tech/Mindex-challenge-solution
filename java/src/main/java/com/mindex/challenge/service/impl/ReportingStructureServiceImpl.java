package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

  

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure generate(String employeeId) {
        Employee employee = employeeService.read(employeeId);
        return generate(employee);
    }

    @Override
    public ReportingStructure generate(Employee employee) {
       

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);

        int numberOfReports = calculateNumberOfReports(employee);
        reportingStructure.setNumberOfReports(numberOfReports);

        return reportingStructure;
    }

    //calculate number of reports for any given employee
    private int calculateNumberOfReports(Employee employee) {
        employee = ensureEmployeeData(employee);

        int numberOfReports = 0;
        if (employee.getDirectReports() != null) {
            for (Employee report : employee.getDirectReports()) {
                numberOfReports++;
                numberOfReports += calculateNumberOfReports(report);
            }
        }
        return numberOfReports;
    }

    
     // Uses the EmployeeService to ensure that Employee information is present.
    
    private Employee ensureEmployeeData(Employee employee) {
        return employee.getLastName() != null
                ? employee
                : employeeService.read(employee.getEmployeeId());
    }
}
