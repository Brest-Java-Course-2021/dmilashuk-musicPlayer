package com.epam.brest.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DepartmentController {

    /**
     * Goto departments list page.
     *
     * @return view name
     */
    @GetMapping(value = "/departments")
    public final String departments(Model model) {
        return "departments";
    }

    /**
     * Goto edit department page.
     *
     * @return view name
     */
    @GetMapping(value = "/department/{id}")
    public final String gotoEditDepartmentPage(@PathVariable Integer id, Model model) {
        return "department";
    }

    /**
     * Goto new department page.
     *
     * @return view name
     */
    @GetMapping(value = "/department/add")
    public final String gotoAddDepartmentPage(Model model) {
        return "department";
    }
}
