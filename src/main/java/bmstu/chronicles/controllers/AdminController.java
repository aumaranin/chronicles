package bmstu.chronicles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class AdminController
{
    @GetMapping("/")
    public String home_admin(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        //return "admin/test_page";
        return "admin/home_admin";
    }

    @GetMapping("/add_person")
    public String add_person(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        return "admin/add_person";
    }

    @GetMapping("/about")
    public String about()
    {
        return "admin/about";
    }

    @GetMapping("/test_page")
    public String test_page(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        //return "admin/test_page";
        return "admin/test_page";
    }
}
