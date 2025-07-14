package crud.springboot.repository;

import crud.springboot.model.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testSaveEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("test@example.com");

        Employee saved = employeeRepository.save(emp);

        assertNotNull(saved.getId());
    }

    @Test
    void testFindById() {
        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setEmail("test@example.com");
        Employee saved = employeeRepository.save(emp);

        Optional<Employee> found = employeeRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    void testFindAll() {
        Employee emp1 = new Employee();
        emp1.setFirstName("Harsh");
        emp1.setLastName("Singh");
        emp1.setEmail("Harsh@gmail.com");

        Employee emp2 = new Employee();
        emp2.setFirstName("ravi");
        emp2.setLastName("singh");
        emp2.setEmail("ravi@boolemt.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);

        List<Employee> list = employeeRepository.findAll();

        assertEquals(2, list.size());
    }

    @Test
    void testUpdateEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Madhu");
        emp.setLastName("Mangal");
        emp.setEmail("Madhu@gmail.com");

        Employee saved = employeeRepository.save(emp);
        saved.setFirstName("shiv");

        Employee updated = employeeRepository.save(saved);
        assertEquals("shiv", updated.getFirstName());
    }


    @Test
    void testDeleteById() {
        Employee emp = new Employee();
        emp.setFirstName("priyanshu");
        emp.setLastName("Raghav");
        emp.setEmail("priyanshu@boolment.in");

        Employee saved = employeeRepository.save(emp);
        employeeRepository.deleteById(saved.getId());

        assertFalse(employeeRepository.findById(saved.getId()).isPresent());
    }
}
