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
    private final AscensionDao ascensionDao;
    private final SponsorDao sponsorDao;
    private final RqsDao rqsDao;

    @Autowired
    public AdminController(PersonDao personDao, MountainDao mountainDao, AscensionDao ascensionDao, SponsorDao sponsorDao, RqsDao rqsDao) {
        this.personDao = personDao;
        this.mountainDao = mountainDao;
        this.ascensionDao = ascensionDao;
        this.sponsorDao = sponsorDao;
        this.rqsDao = rqsDao;
    }

    @GetMapping("/")
    public String home_admin(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        return "admin/home_admin";
    }

    @GetMapping("/about")
    //О СИСТЕМЕ
    public String about()
    {
        return "admin/about";
    }

    @GetMapping("/person/new")
    public String add_person(@ModelAttribute("person") Person person)
    {
        return "admin/persons/person_add";
    }

    @PostMapping("person/creating")
    public String create(@ModelAttribute("person") Person person) {
        person.setEnabled(true);
        personDao.save(person);
        return "redirect:/admin/persons/persons";
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
        return "admin/persons/persons";
    }

    @GetMapping("/person_info")
    //Форма выбора пользователя
    public String person_info1(Model model)
    {
        List<Person> list = personDao.index();
        model.addAttribute("persons", list);
        return "admin/persons/person_info1";
    }

    @PostMapping("/person")
    //Форма отображения информации о пользователе
    public String person_info2(@RequestParam("id") int id, Model model)
    {
        Person person = personDao.show(id);
        model.addAttribute("person", person);
        return "admin/persons/person_info2";
    }

    @PostMapping("/person_change")
    public String person_change(@RequestParam("id") int id, Model model, @ModelAttribute("person") Person person)
    {
        Person person_old = personDao.show(id);
        model.addAttribute("person_old", person_old);
        return "admin/persons/person_change";
    }

    @PostMapping("/person/changing")
    public String person_changing(@RequestParam("id") int id, @ModelAttribute("person") Person person)
    {
        //сохранение изменений, принятых с формы.
        System.out.println("открыта форма person_changing");
        System.out.println("получен атрибут id: " + Integer.toString(id));
        personDao.save(id, person);
        return "redirect:/admin/persons/persons";
    }


    //Форма подсчета количества восхождений на каждую гору
    @PostMapping("/person/person_count_ascension_on_mountains/")
    public String person_count_ascension_on_mountains(@RequestParam("id") int id, Model model)
    {
        System.out.println("открыта функция спец.запроса на подсчет гор пользователем");
        PersonMntCount pmc = rqsDao.personMntCount(id);
        model.addAttribute("pmc", pmc);
        return "admin/person_mountain_count";
    }

    //Форма отображающая все восхождения пользователя, отсортированные по дате
    @PostMapping("/person/person_ascensions/")
    public String person_ascensions(@RequestParam("id") int id, Model model)
    {
        System.out.println("открыта функция спец.запроса на отображение восхождений пользователя");
        PersonAscensions personAscensions = rqsDao.personAscensions(id);
        model.addAttribute("personAscensions", personAscensions);
        return "admin/person_ascensions";
    }

    //ГОРЫ ГОРЫ ГОРЫ

    //форма отображающая все горные вершины
    @GetMapping("/mountains")

    public String show_mountains(Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        model.addAttribute("mountains_list", mountains_list);
        return "admin/mountains/mountains";
    }

    //Форма добавления горы
    @GetMapping("/mountains/new")
    public String add_mountain(@ModelAttribute("mountain") Mountain mountain)
    {
        return "admin/mountains/mountain_add";
    }

    //POST запрос на сохранение горы
    @PostMapping("/mountains/creating")
    public String mountain_create(@ModelAttribute("mountain") Mountain mountain) {
        mountainDao.save(mountain);
        return "redirect:/admin/mountains/mountains";
    }

    //Форма выбора горы
    @GetMapping("/mountain_info")
    //Форма выбора пользователя
    public String mountain_info1(Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        model.addAttribute("mountains_list", mountains_list);
        return "admin/mountains/mountain_info1";
    }

    //Форма отображения информации о горной вершине
    @PostMapping("/mountain")
    public String mountain_info2(@RequestParam("id") int id, Model model)
    {
        Mountain mountain = mountainDao.show(id);
        model.addAttribute("mountain", mountain);
        return "admin/mountains/mountain_info2";
    }

    //Форма изменения информации о горной вершине
    @PostMapping("/mountain_change")
    public String mountain_change(@RequestParam("id") int id, Model model, @ModelAttribute("mountain") Mountain mountain)
    {
        Mountain mountain_old = mountainDao.show(id);
        model.addAttribute("mountain_old", mountain_old);
        return "admin/mountains/mountain_change";
    }

    //POST запрос для внесения изменения в базу данных о горной вершины
    @PostMapping("/mountain/changing")
    public String person_changing(@RequestParam("id") int id, @ModelAttribute("mountain") Mountain mountain)
    {
        //сохранение изменений, принятых с формы.
        mountainDao.save(id, mountain);
        return "redirect:/admin/mountains/mountains";
    }


    //ВОСХОЖДЕНИЯ ВОСХОЖДЕНИЯ ВОСХОЖДЕНИЯ

    //форма отображающая все восхождения
    @GetMapping("/ascensions")
    public String show_ascensions(@RequestParam(value = "from", required = false, defaultValue = "") String from, @RequestParam(value = "to", required = false, defaultValue = "") String to, Model model)
    {
        List<Ascension> ascensions_list;
        if (from.equals("") || to.equals(""))
            ascensions_list = ascensionDao.index();
        else
            ascensions_list = ascensionDao.index(from, to);
        model.addAttribute("ascensions_list", ascensions_list);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "admin/ascensions/ascensions";
    }

    //Форма выбора восхождения
    @GetMapping("/ascension_info")
    //Форма выбора восхождения
    public String ascension_info1(Model model)
    {
        List<Ascension> ascensions_list = ascensionDao.index();
        model.addAttribute("ascensions_list", ascensions_list);
        return "admin/ascensions/ascension_info1";
    }

    //Форма отображения информации о восхождении
    @PostMapping("/ascension")
    public String ascension_info2(@RequestParam("id") int id, Model model)
    {
        //Ascen = mountainDao.show(id);
        //model.addAttribute("mountain", mountain);
        return "admin/ascensions/info_ascension2";
    }

    //СПОНСОРЫ СПОНСОРЫ СПОНСОРЫ

    //форма отображающая все горные вершины
    @GetMapping("/sponsors")

    public String show_sponsors(Model model)
    {
        List<Sponsor> sponsors_list = sponsorDao.index();
        model.addAttribute("sponsors_list", sponsors_list);
        return "admin/sponsors/sponsors";
    }

    //Форма добавления спонсора
    @GetMapping("/sponsors/new")
    public String sponsor_add(@ModelAttribute("sponsor") Sponsor sponsor)
    {
        return "admin/sponsors/sponsor_add";
    }

    //POST запрос на сохранение спонсора
    @PostMapping("/sponsors/creating")
    public String sponsor_create(@ModelAttribute("sponsor") Sponsor sponsor) {
        sponsorDao.save(sponsor);
        return "redirect:/admin/sponsors";
    }

    //Форма выбора спонсора
    @GetMapping("/sponsor_info")
    public String sponsor_info1(Model model)
    {
        List<Sponsor> sponsors_list = sponsorDao.index();
        model.addAttribute("sponsors_list", sponsors_list);
        return "admin/sponsors/sponsor_info1";
    }

    //Форма отображения информации о спонсоре
    @PostMapping("/sponsor")
    public String sponsor_info2(@RequestParam("id") int id, Model model)
    {
        Sponsor sponsor = sponsorDao.show(id);
        model.addAttribute("sponsor", sponsor);
        return "admin/sponsors/sponsor_info2";
    }


    //Форма изменения информации о спонсоре
    @PostMapping("/sponsor_change")
    public String sponsor_change(@RequestParam("id") int id, Model model, @ModelAttribute("sponsor") Sponsor sponsor)
    {
        Sponsor sponsor_old = sponsorDao.show(id);
        model.addAttribute("sponsor_old", sponsor_old);
        return "admin/sponsors/sponsor_change";
    }

    //POST запрос для внесения изменения в базу данных о спонсоре
    @PostMapping("/sponsor/changing")
    public String sponsor_changing(@RequestParam("id") int id, @ModelAttribute("mountain") Sponsor sponsor)
    {
        //сохранение изменений, принятых с формы.
        sponsorDao.save(id, sponsor);
        return "redirect:/admin/sponsors";
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
