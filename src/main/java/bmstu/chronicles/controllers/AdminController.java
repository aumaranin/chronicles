package bmstu.chronicles.controllers;

import bmstu.chronicles.dao.PersonDao;
import bmstu.chronicles.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Аннотация контроллер дает классу дополниительные возможности в соответствии с шаблоном
//проектирования MVC (Model-View-Controller). Подобный класс расширяет свои возможности
//и может обрабатывать HTTP-запросы.
@Controller
//Аннотация RequestMapping перед классом-контроллером позволяет перенаправлять все
//запросы, начинающиеся с строки, передаваемой в качестве аргумента, в этот контроллер
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
        return "redirect:/admin/persons";
    }

    @GetMapping("/about")
    public String about()
    {
        return "admin/about";
    }

    @GetMapping("/persons")
    public String show_all(@RequestParam(value = "from", required = false, defaultValue = "") String from, @RequestParam(value = "to", required = false, defaultValue = "") String to, Model model)
    {
        List<Person> list;
        if (from.equals("") || to.equals(""))
            list = personDao.index();
        else
            list = personDao.index(from, to);
        model.addAttribute("persons", list);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "admin/persons";
    }

    @GetMapping("/person_info")
    public String person_info1(Model model)
    {
        List<Person> list = personDao.index();
        model.addAttribute("persons", list);
        return "admin/info_person1";
    }

    @PostMapping("/person")
    public String person_info2(@RequestParam("id") int id, Model model)
    {
        Person person = personDao.show(id);
        model.addAttribute("person", person);
        return "admin/info_person2";
    }

    @PostMapping("/person_change")
    public String person_change(@RequestParam("id") int id, Model model, @ModelAttribute("person") Person person)
    {
        Person person_old = personDao.show(id);
        model.addAttribute("person_old", person_old);
        //System.out.println(person);
        //return "admin/info_person1";
        //return "redirect:admin/info_person1";
        return "/admin/person_change";
    }

    @GetMapping("/test_page")
    public String test_page(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        List<Person> list = new ArrayList<Person>();
        list = personDao.index();
        for (Person person : list)
        {
            System.out.println(person);
        }
        //return "admin/test_page";
        return "admin/test_page";
    }
}
