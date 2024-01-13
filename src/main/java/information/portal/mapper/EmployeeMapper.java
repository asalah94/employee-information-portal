package information.portal.mapper;

import information.portal.dto.EmployeeDTO;
import information.portal.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDTO map(Employee source) {
        if (source == null) {
            return null;
        }

        EmployeeDTO target = new EmployeeDTO();
        target.setId(source.getId());
        target.setFName(source.getFName());
        target.setLName(source.getLName());
        target.setPhone(source.getPhone());
        target.setEmail(source.getEmail());
        target.setSalary(source.getSalary());

        return target;
    }

    public Employee unMap(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }

        Employee target = new Employee();
        target.setId(employeeDTO.getId());
        target.setFName(employeeDTO.getFName());
        target.setLName(employeeDTO.getLName());
        target.setPhone(employeeDTO.getPhone());
        target.setEmail(employeeDTO.getEmail());
        target.setSalary(employeeDTO.getSalary());

        return target;
    }
}
