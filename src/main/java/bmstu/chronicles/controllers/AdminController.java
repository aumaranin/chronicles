package bmstu.chronicles.controllers;

import bmstu.chronicles.dao.*;
import bmstu.chronicles.models.*;
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
    private final MountainDao mountainDao;
    private final RqsDao rqsDao;

    @Autowired
    public AdminController(PersonDao personDao, MountainDao mountainDao, RqsDao rqsDao) {
        this.personDao = personDao;
        this.mountainDao = mountainDao;
        this.rqsDao = rqsDao;
    }

    @GetMapping("/")
    public String home_admin(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        //return "admin/test_page";
        return "admin/home_admin";
    }

    @GetMapping("/person/new")
    public String add_person(@ModelAttribute("person") Person person)
    {
        return "admin/add_person";
    }

    @PostMapping("person/creating")
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
    //О СИСТЕМЕ
    public String about()
    {
        return "admin/about";
    }

    @GetMapping("/persons")
    //ОТОБРАЗИТЬ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ
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
    //Форма выбора пользователя
    public String person_info1(Model model)
    {
        List<Person> list = personDao.index();
        model.addAttribute("persons", list);
        return "admin/info_person1";
    }

    @PostMapping("/person")
    //Форма отображения информации о пользователе
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
        return "admin/person_change";
    }

    @PostMapping("/person/changing")
    public String person_changing(@RequestParam("id") int id, @ModelAttribute("person") Person person)
    {
        //сохранение изменений, принятых с формы.
        System.out.println("открыта форма person_changing");
        System.out.println("получен атрибут id: " + Integer.toString(id));
        personDao.save(id, person);
        //Person person_old = personDao.show(id);
        //model.addAttribute("person_old", person_old);
        //System.out.println(person);
        //return "admin/info_person1";
        //return "redirect:admin/info_person1";
        //person.setEnabled(true);
        //personDao.save(person);
        return "redirect:/admin/persons";
    }


    //Форма подсчета количества восхождений на каждую гору
    @PostMapping("/person/person_count_assention_on_mountains/")
    public String person_count_ascention_on_mountains(@RequestParam("id") int id, Model model)
    {
        System.out.println("открыта функция спец.запроса на подсчет гор пользователем");
        PersonMntCount pmc = rqsDao.personMntCount(id);
        model.addAttribute("pmc", pmc);
        return "admin/person_mountain_count";
    }





    //ГОРЫ ГОРЫ ГОРЫ

    //форма отображающая все горные вершины
    @GetMapping("/mountains")

    public String show_mountains(Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        model.addAttribute("mountains_list", mountains_list);
        return "admin/mountains";
    }

    //Форма добавления горы
    @GetMapping("/mountains/new")
    public String add_mountain(@ModelAttribute("mountain") Mountain mountain)
    {
        return "admin/add_mountain";
    }

    //POST запрос на сохранение горы
    @PostMapping("/mountains/creating")
    public String mountain_create(@ModelAttribute("mountain") Mountain mountain) {
        mountainDao.save(mountain);
        return "redirect:/admin/";
    }

    //Форма выбора горы
    @GetMapping("/mountain_info")
    //Форма выбора пользователя
    public String mountain_info1(Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        model.addAttribute("mountains_list", mountains_list);
        return "admin/info_mountain1";
    }

    //Форма отображения информации о горной вершине
    @PostMapping("/mountain")
    public String mountain_info2(@RequestParam("id") int id, Model model)
    {
        Mountain mountain = mountainDao.show(id);
        model.addAttribute("mountain", mountain);
        return "admin/info_mountain2";
    }

    //Форма изменения информации о горной вершине
    @PostMapping("/mountain_change")
    public String mountain_change(@RequestParam("id") int id, Model model, @ModelAttribute("mountain") Mountain mountain)
    {
        Mountain mountain_old = mountainDao.show(id);
        model.addAttribute("mountain_old", mountain_old);
        return "admin/mountain_change";
    }

    //POST запрос для внесения изменения в базу данных о горной вершины
    @PostMapping("/mountain/changing")
    public String person_changing(@RequestParam("id") int id, @ModelAttribute("mountain") Mountain mountain)
    {
        //сохранение изменений, принятых с формы.
        mountainDao.save(id, mountain);
        return "redirect:/admin/mountains";
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
