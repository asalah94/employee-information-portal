package information.portal.service;



import information.portal.dto.EmployeeDTO;
import information.portal.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO saveEmployee(EmployeeDTO employee);
    void deleteEmployee(Long id);
    Page<Employee> searchEmployees(String filter, Pageable pageable);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployee);
}
