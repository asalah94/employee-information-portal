package information.portal.service.impl;

import information.portal.dto.EmployeeDTO;
import information.portal.exception.GlobalExceptionHandler;
import information.portal.mapper.EmployeeMapper;
import information.portal.model.Employee;
import information.portal.repository.EmployeeRepository;
import information.portal.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Employee not found with id: " + id)
        );
        return employeeMapper.map(employee);
    }

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employee) {
        try {
            Employee savedEmployee = employeeRepository.save(employeeMapper.unMap(employee));
            return employeeMapper.map(savedEmployee);
        } catch (RuntimeException e) {
            GlobalExceptionHandler.handleRuntimeException(e);
        }
        throw new RuntimeException("Unexpected error occurred while saving employee");
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> searchEmployees(String filter, Pageable pageable) {
        return employeeRepository.searchEmployees(filter, pageable);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployee) {
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
        Employee existingEmployee = existingEmployeeOptional.orElseThrow(() ->
                new RuntimeException("Employee not found with id: " + id)
        );

        updateEmployeeFields(updatedEmployee, existingEmployee);
        try {
            Employee savedEmployee = employeeRepository.save(existingEmployee);
            return employeeMapper.map(savedEmployee);
        } catch (RuntimeException e) {
            GlobalExceptionHandler.handleRuntimeException(e);
        }
        throw new RuntimeException("Unexpected error occurred while updating employee");
    }

    private static void updateEmployeeFields(EmployeeDTO updatedEmployee, Employee existingEmployee) {
        existingEmployee.setFName(updatedEmployee.getFName());
        existingEmployee.setLName(updatedEmployee.getLName());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setSalary(updatedEmployee.getSalary());
    }
}
