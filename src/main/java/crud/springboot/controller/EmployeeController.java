// EmployeeController.java
package crud.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import crud.springboot.model.Employee;
import crud.springboot.service.EmployeeService;
import jakarta.validation.Valid;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return findPaginated(1, 5, "firstName", "asc", model);
    }

    @GetMapping("/NewEmployeeForm")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return employee.getId() == 0 ? "new_employee" : "update_employee";
        }

        boolean isUpdate = employee.getId() != 0;
        employeeService.saveEmployee(employee);

        if (isUpdate) {
            redirectAttributes.addFlashAttribute("message", "Employee updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Employee added successfully.");
        }

        return "redirect:/";
    }

    @GetMapping("/FormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployeeById(id);
        redirectAttributes.addFlashAttribute("message", "Employee deleted successfully.");
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo,
                                @RequestParam("size") int pageSize,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Employee> listEmployees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listEmployees", listEmployees);

        return "index";
    }
}

