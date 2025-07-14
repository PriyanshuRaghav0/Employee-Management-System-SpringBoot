package crud.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import crud.springboot.model.Employee;
import crud.springboot.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        if (employee.getId() == 0) {
            boolean exists = employeeRepository.findAll().stream()
                    .anyMatch(e -> e.getEmail().equalsIgnoreCase(employee.getEmail()));
            if (exists) {
                throw new RuntimeException("Email already exists: " + employee.getEmail());
            }
        }
        this.employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found for id :: " + id));
    }

    @Override
    public void deleteEmployeeById(long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete — Employee not found with ID: " + id);
        }
        this.employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.employeeRepository.findAll(pageable);
    }
}
