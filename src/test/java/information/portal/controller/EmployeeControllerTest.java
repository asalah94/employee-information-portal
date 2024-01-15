package information.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import information.portal.dto.EmployeeDTO;
import information.portal.model.Employee;
import information.portal.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void getAllEmployees_shouldReturnListOfEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(new EmployeeDTO()));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(employeeService, times(1)).getAllEmployees();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getEmployeeById_shouldReturnEmployeeById() throws Exception {
        long id = 1L;
        when(employeeService.getEmployeeById(id)).thenReturn(new EmployeeDTO());

        mockMvc.perform(get("/api/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(employeeService, times(1)).getEmployeeById(id);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void saveEmployee_shouldCreateNewEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(employeeService, times(1)).saveEmployee(any(EmployeeDTO.class));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void updateEmployee_shouldUpdateExistingEmployee() throws Exception {
        long id = 1L;
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        when(employeeService.updateEmployee(eq(id), any(EmployeeDTO.class))).thenReturn(updatedEmployeeDTO);

        mockMvc.perform(put("/api/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(employeeService, times(1)).updateEmployee(eq(id), any(EmployeeDTO.class));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void deleteEmployee_shouldDeleteEmployee() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/api/employees/{id}", id))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(id);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void searchEmployees_shouldReturnPagedResult() throws Exception {
        int page = 0;
        int size = 10;
        String sortField = "id";
        String sortOrder = "asc";
        String filter = "John";

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        Page<Employee> employeesPage = new PageImpl<>(Collections.singletonList(new Employee()));

        when(employeeService.searchEmployees(eq(filter), eq(pageable))).thenReturn(employeesPage);

        mockMvc.perform(get("/api/employees/search")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortField", sortField)
                        .param("sortOrder", sortOrder)
                        .param("filter", filter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(employeeService, times(1)).searchEmployees(eq(filter), eq(pageable));
        verifyNoMoreInteractions(employeeService);
    }
}
