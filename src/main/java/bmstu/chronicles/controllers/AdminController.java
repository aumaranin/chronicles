package bmstu.chronicles.controllers;

import bmstu.chronicles.dao.PersonDao;
import bmstu.chronicles.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminController
{

    private final PersonDao personDao;

    @Autowired
    public AdminController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping("/")
    public String home_admin(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        //return "admin/test_page";
        return "admin/home_admin";
    }

    @GetMapping("/add_person")
    public String add_person(@ModelAttribute("person") Person person)
    {
        return "admin/add_person";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") Person person) {
        /*
        person.setFirst_name("Громов");
        person.setSecond_name("Василий");
        person.setLast_name("Викторович");
        person.setDate_of_birth("1960-02-01");
        person.setLogin("log5");
        person.setRole("ROLE_LEADER");
        person.setPassword("pas5");
        person.setEnabled(true);
         */
        person.setEnabled(true);
        personDao.save(person);
        System.out.println("Добавлен пользователь:");
        System.out.println(person);
        return "redirect:/admin/";
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
