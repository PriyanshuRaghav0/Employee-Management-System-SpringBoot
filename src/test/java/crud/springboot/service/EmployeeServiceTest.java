package crud.springboot.service;

import crud.springboot.model.Employee;
import crud.springboot.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> list = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(list);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveEmployee() {
        Employee emp = new Employee();
        employeeService.saveEmployee(emp);

        verify(employeeRepository, times(1)).save(emp);
    }

    @Test
    void testGetEmployeeById_Found() {
        Employee emp = new Employee(); emp.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = employeeService.getEmployeeById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class,
                () -> employeeService.getEmployeeById(5L));

        assertTrue(ex.getMessage().contains("Employee not found"));
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployeeById(10L);

        verify(employeeRepository).deleteById(10L);
    }

    @Test
    void testFindPaginatedEmptyResult() {
        List<Employee> emptyList = Collections.emptyList();
        Page<Employee> page = new PageImpl<>(emptyList);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("lastName").descending());

        when(employeeRepository.findAll(pageable)).thenReturn(page);

        Page<Employee> result = employeeService.findPaginated(1, 10, "lastName", "desc");

        assertEquals(0, result.getContent().size());
    }

}
