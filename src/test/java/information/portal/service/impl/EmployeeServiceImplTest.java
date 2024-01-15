package information.portal.service.impl;

import information.portal.dto.EmployeeDTO;
import information.portal.exception.GlobalExceptionHandler;
import information.portal.mapper.EmployeeMapper;
import information.portal.model.Employee;
import information.portal.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    GlobalExceptionHandler exceptionHandler;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(new Employee()));
        when(employeeMapper.map(any(Employee.class))).thenReturn(new EmployeeDTO());

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).map(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void getEmployeeById_existingId_shouldReturnEmployeeDTO() {
        long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(new Employee()));
        when(employeeMapper.map(any(Employee.class))).thenReturn(new EmployeeDTO());

        EmployeeDTO result = employeeService.getEmployeeById(id);

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeMapper, times(1)).map(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void getEmployeeById_nonExistingId_shouldThrowException() {
        long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(id));

        verify(employeeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void saveEmployee_successfulSave_shouldReturnEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = new Employee();
        when(employeeMapper.unMap(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(new Employee());
        when(employeeMapper.map(any(Employee.class))).thenReturn(new EmployeeDTO());

        EmployeeDTO result = employeeService.saveEmployee(employeeDTO);

        assertNotNull(result);
        verify(employeeMapper, times(1)).unMap(employeeDTO);
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper, times(1)).map(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void saveEmployee_exceptionDuringSave_shouldThrowException() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        when(employeeMapper.unMap(employeeDTO)).thenReturn(new Employee());

        doThrow(new RuntimeException("Simulated exception")).when(exceptionHandler);

        assertThrows(RuntimeException.class, () -> employeeService.saveEmployee(employeeDTO));

        verifyNoMoreInteractions(employeeRepository, employeeMapper, exceptionHandler);
    }

    @Test
    void deleteEmployee() {
        long id = 1L;

        assertDoesNotThrow(() -> employeeService.deleteEmployee(id));

        verify(employeeRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void searchEmployees() {
        String filter = "John";
        Pageable pageable = mock(Pageable.class);
        Page<Employee> page = mock(Page.class);

        when(employeeRepository.searchEmployees(filter, pageable)).thenReturn(page);

        Page<Employee> result = employeeService.searchEmployees(filter, pageable);

        assertNotNull(result);
        assertEquals(page, result);
        verify(employeeRepository, times(1)).searchEmployees(filter, pageable);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void updateEmployee_existingId_shouldReturnUpdatedEmployeeDTO() {
        long id = 1L;
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        Employee existingEmployee = new Employee();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
        when(employeeMapper.map(existingEmployee)).thenReturn(updatedEmployeeDTO);

        EmployeeDTO result = employeeService.updateEmployee(id, updatedEmployeeDTO);

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).save(existingEmployee);
        verify(employeeMapper, times(1)).map(existingEmployee);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void updateEmployee_nonExistingId_shouldThrowException() {
        long id = 1L;
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.updateEmployee(id, updatedEmployeeDTO));

        verify(employeeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }
}
