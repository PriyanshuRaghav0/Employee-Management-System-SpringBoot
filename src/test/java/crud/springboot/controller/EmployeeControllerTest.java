package crud.springboot.controller;

import crud.springboot.model.Employee;
import crud.springboot.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void testHomePage_ReturnsEmployees() throws Exception {
        List<Employee> list = List.of(new Employee());
        Page<Employee> page = new PageImpl<>(list);

        when(employeeService.findPaginated(1, 5, "firstName", "asc")).thenReturn(page);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("listEmployees"))
                .andExpect(view().name("index"));
    }

    @Test
    void testShowFormForNewEmployee() throws Exception {
        mockMvc.perform(get("/NewEmployeeForm"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee"))
                .andExpect(view().name("new_employee"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(get("/deleteEmployee/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testSaveEmployee() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("ravi");
        emp.setLastName("singh");
        emp.setEmail("ravi@boolemt.com");

        mockMvc.perform(post("/saveEmployee")
                        .param("firstName", "Priyanshu")
                        .param("lastName", "Raghav")
                        .param("email", "priyanshu@boolment.in"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    void testFormForUpdate() throws Exception {
        Employee emp = new Employee(); emp.setId(1L);
        when(employeeService.getEmployeeById(1L)).thenReturn(emp);

        mockMvc.perform(get("/FormForUpdate/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee"))
                .andExpect(view().name("update_employee"));
    }
}
