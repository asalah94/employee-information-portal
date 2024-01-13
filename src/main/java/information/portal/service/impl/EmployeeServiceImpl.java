package information.portal.service.impl;

import information.portal.mapper.EmployeeMapper;
import information.portal.model.Employee;
import information.portal.repository.EmployeeRepository;
import information.portal.service.EmployeeService;
import information.portal.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private  EmployeeRepository employeeRepository;
    @Autowired
    private  EmployeeMapper employeeMapper;

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        Objects.requireNonNull(employee, "Employee not found with id: " + id);
        return employeeMapper.map(employee);
    }

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employee) {
        try {
            Employee savedEmployee = employeeRepository.save(employeeMapper.unMap(employee));
            return employeeMapper.map(savedEmployee);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error saving employee. Duplicate key violation.", e);
            throw new RuntimeException("Error saving employee. Duplicate key violation.", e);
        } catch (Exception e) {
            logger.error("Error saving employee", e);
            throw new RuntimeException("Error saving employee", e);
        }
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
        if (existingEmployeeOptional.isPresent()) {
            Employee existingEmployee = updateEmployeeFields(updatedEmployee, existingEmployeeOptional);
            try {
                Employee savedEmployee = employeeRepository.save(existingEmployee);
                return employeeMapper.map(savedEmployee);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Error updating employee. Duplicate key violation.", e);
            } catch (Exception e) {
                throw new RuntimeException("Error updating employee", e);
            }
        } else {
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }

    private static Employee updateEmployeeFields(EmployeeDTO updatedEmployee, Optional<Employee> existingEmployeeOptional) {
        Employee existingEmployee = existingEmployeeOptional.get();
        existingEmployee.setFName(updatedEmployee.getFName());
        existingEmployee.setLName(updatedEmployee.getLName());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        return existingEmployee;
    }
}
