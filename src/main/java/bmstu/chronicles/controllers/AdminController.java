package bmstu.chronicles.controllers;

import bmstu.chronicles.dao.*;
import bmstu.chronicles.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    //Добавление компонентов, хранящих функции работы с базой данных
    private final PersonDao personDao;
    private final MountainDao mountainDao;
    private final AscensionDao ascensionDao;
    private final SponsorDao sponsorDao;
    private final AscensionSponsorDao ascensionSponsorDao;
    private final RqsDao rqsDao;

    @Autowired
    public AdminController(PersonDao personDao, MountainDao mountainDao, AscensionDao ascensionDao, SponsorDao sponsorDao, AscensionSponsorDao ascensionSponsorDao, RqsDao rqsDao) {
        this.personDao = personDao;
        this.mountainDao = mountainDao;
        this.ascensionDao = ascensionDao;
        this.sponsorDao = sponsorDao;
        this.ascensionSponsorDao = ascensionSponsorDao;
        this.rqsDao = rqsDao;
    }

    //Метод вывода главной страницы
    @GetMapping("/")
    public String home_admin(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        return "admin/home_admin";
    }

    //Метод вывода главной страницы
    @GetMapping("/about")
    //О СИСТЕМЕ
    public String about()
    {
        return "admin/about";
    }

    //Форма добавления новых пользователей
    @GetMapping("/person/new")
    public String add_person(@ModelAttribute("person") Person person)
    {
        return "admin/persons/person_add";
    }

    //POST запрос на добавление нового пользователя с записью в БД
    @PostMapping("person/creating")
    public String create(@ModelAttribute("person") Person person) {
        person.setEnabled(true);
        personDao.save(person);
        return "redirect:/admin/persons";
    }

    //Запрос: ОТОБРАЗИТЬ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ, совершавших восхождение в промежутке дат
    @GetMapping("/persons")
    public String show_all(@RequestParam(value = "from", required = false, defaultValue = "") String from, @RequestParam(value = "to", required = false, defaultValue = "") String to, Model model)
    {
        List<Person> list;
        if (from.equals("") || to.equals(""))
            list = personDao.index("last_name");
        else
            list = personDao.show_climbing_person("last_name", from, to);
        model.addAttribute("persons", list);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "admin/persons/persons";
    }

    //Форма выбора пользователя
    @GetMapping("/person_info")
    public String person_info1(Model model)
    {
        List<Person> list = personDao.index("last_name");
        model.addAttribute("persons", list);
        return "admin/persons/person_info1";
    }

    //Форма отображения информации о пользователе
    @PostMapping("/person")
    public String person_info2(@RequestParam("id") int id, Model model)
    {
        Person person = personDao.show(id);
        model.addAttribute("person", person);
        return "admin/persons/person_info2";
    }

    //Форма изменение данных пользователя
    @PostMapping("/person_change")
    public String person_change(@RequestParam("id") int id, Model model, @ModelAttribute("person") Person person)
    {
        Person person_old = personDao.show(id);
        model.addAttribute("person_old", person_old);
        return "admin/persons/person_change";
    }

    //POST запрос на изменение данных пользователя с зиписью в БД
    @PostMapping("/person/changing")
    public String person_changing(@RequestParam("id") int id, @ModelAttribute("person") Person person)
    {
        //сохранение изменений, принятых с формы.
        System.out.println("открыта форма person_changing");
        System.out.println("получен атрибут id: " + Integer.toString(id));
        personDao.save(id, person);
        return "redirect:/admin/persons";
    }

    //Форма подсчета количества восхождений на каждую гору
    @PostMapping("/person/person_count_ascension_on_mountains/")
    public String person_count_ascension_on_mountains(@RequestParam("id") int id, Model model)
    {
        PersonMntCount pmc = rqsDao.personMntCount(id);
        model.addAttribute("pmc", pmc);
        return "admin/persons/person_mountain_count";
    }

    //Форма отображающая все восхождения альпиниста, отсортированные по дате
    @PostMapping("/person/person_ascensions/")
    public String person_ascensions(@RequestParam("id") int id, Model model)
    {
        PersonAscensions personAscensions = rqsDao.personAscensions(id);
        model.addAttribute("personAscensions", personAscensions);
        return "admin/persons/person_ascensions";
    }

    //Форма записи пользователя администратором на восхождение
    @GetMapping("/sign_person")
    public String sign_person(Model model)
    {
        List<Person> member_list = personDao.index("last_name");
        List<Ascension> ascensions_list = ascensionDao.show_active();
        model.addAttribute("member_list", member_list);
        model.addAttribute("ascensions_list", ascensions_list);
        return "admin/persons/person_sign_by_club";
    }

    //Форма записи самого себя на активное восхождение
    @GetMapping("/sign")
    public String sign_person(HttpServletRequest request, Model model)
    {
        String login = request.getRemoteUser();
        Person user = personDao.show(login);
        List<Ascension> ascensions_list = ascensionDao.show_active();
        model.addAttribute("user", user);
        model.addAttribute("ascensions_list", ascensions_list);
        return "admin/persons/person_sign_by_yourself";
    }

    //POST запрос на запись альпиниста на восхождение
    @PostMapping("persons/singing")
    public String person_singing(@RequestParam("asc_id") int asc_id, @RequestParam("person_id") int person_id, Model model) {
        ascensionDao.sign_person(asc_id, person_id);
        return "redirect:/admin/ascensions/";
    }

    //Горные вершины

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

    //форма отображающая горных вершин, на которые совершались восхождения членами клуба
    @GetMapping("/mountains/asc_by_members")
    public String asc_by_members(Model model)
    {
        List<Mountain> mountains_list = mountainDao.show_asc_by_members();
        model.addAttribute("mountains_list", mountains_list);
        return "admin/mountains/mountains_asc_by_members";
    }

    //форма отображающая количество альпинистов, совершавших восхождения на каждую гору
    @GetMapping("/mountains/mount_per_count")
    public String mount_per_count(Model model)
    {
        List<MountCount> mountCount_list = mountainDao.mount_per_count();
        model.addAttribute("mountCount_list", mountCount_list);
        return "admin/mountains/mountains_per_count";
    }

    //форма отображающая восхождения в хронологическом порядке на горе
    @GetMapping("/mountains/mountain_show_asc_by_date/")
    public String mountain_show_asc_by_date(@RequestParam("mountain_id") int mountain_id, @RequestParam("mountain_name") String mountain_name, Model model)
    {
        List<Ascension> ascensions_list = mountainDao.show_asc_by_date_for_mountain(mountain_id);
        model.addAttribute("ascensions_list", ascensions_list);
        model.addAttribute("mountain_name", mountain_name);
        return "admin/mountains/mountain_show_asc_by_date";
    }

    //Форма отображающая для каждой горы все восхождения в хронологическом порядке.
    @GetMapping("/mountains/mount_asc")
    public String mount_asc(Model model)
    {
        List<MountAsc> mount_asc_list = rqsDao.show_mount_asc();
        model.addAttribute("mount_asc_list", mount_asc_list);
        return "admin/mountains/mount_asc";
    }

    //Восхождения

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
        Ascension ascension = ascensionDao.show(id);
        Person leader = personDao.show(ascension.getLeader_id());
        Mountain mountain = mountainDao.show(ascension.getMountain_id());
        List<SponsorDeposit> ascensionSponsor_list = ascensionSponsorDao.getSponsors(id);
        List<Person> members_list = ascensionDao.getMembers(id);

        model.addAttribute("members_list", members_list);
        model.addAttribute("leader", leader);
        model.addAttribute("mountain", mountain);
        model.addAttribute("ascension", ascension);
        model.addAttribute("asc_sp_list", ascensionSponsor_list);
        return "admin/ascensions/ascension_info2";
    }

    //Форма для добавления восхождения
    @GetMapping("/ascensions/new")
    public String ascension_add(@ModelAttribute("ascension") Ascension ascension, Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        List<Person> leader_list = personDao.index_leader();
        List<Sponsor> sponsors_list = sponsorDao.index();
        String sponsor_deposit = "";
        int sponsor_id = 0;
        model.addAttribute("mountains_list", mountains_list);
        model.addAttribute("leader_list", leader_list);
        model.addAttribute("sponsors_list", sponsors_list);
        model.addAttribute("sponsor_deposit", sponsor_deposit);
        model.addAttribute("sponsor_id", sponsor_id);
        return "admin/ascensions/ascensions_add";
    }

    //POST запрос на сохранение восхождения
    @PostMapping("/ascensions/creating")
    public String ascension_create(@ModelAttribute("ascension") Ascension ascension, @RequestParam("sponsor_id") int sponsor_id, @RequestParam("sponsor_deposit") String sponsor_deposit) {
        //сохранение восхождения
        ascension.setStatus("Активно");
        int leader_id = ascension.getLeader_id();
        ascensionDao.save(ascension);
        //загрузка восхождения из БД с установленным ID
        Ascension ascension2 = ascensionDao.show(ascension.getName(), ascension.getDate());
        int ascension_id = ascension2.getId();
        //сохранение вклада спонсора в таблицу ascension_sponsor
        ascensionSponsorDao.save(ascension2.getId(), sponsor_id, sponsor_deposit);
        ascensionDao.sign_person(ascension_id, leader_id);
        return "redirect:/admin/ascensions";
    }

    //Форма для завершения восхождения
    @GetMapping("/ascensions/complete")
    public String ascension_complete(Model model)
    {
        //получаем список активных восхождений
        List<Ascension> ascensions_list = ascensionDao.show_active();
        //добавляем в модель списки для отображения на странице формы
        model.addAttribute("ascensions_list", ascensions_list);
        return "admin/ascensions/ascension_complete";
    }

    //POST запрос на завершение восхождения
    @PostMapping("/ascensions/ending")
    public String ascension_ending(@RequestParam("asc_id") int asc_id) {
        //запрос на статус восхождения - завершен
        ascensionDao.ascension_end(asc_id);
        //запрос на завершение восхождения у всех участников
        ascensionDao.ascension_members_end(asc_id);
        return "redirect:/admin/ascensions";
    }

    //Форма добавления спонсора к восхождению
    @GetMapping("/ascensions/sponsor_add")
    public String ascension_sponsor_add(Model model)
    {
        //получаем список активных восхождений
        List<Ascension> ascensions_list = ascensionDao.show_active();
        //получаем спосок спонсоров
        List<Sponsor> sponsors_list = sponsorDao.index();
        //добавляем в модель списки для отображения на странице формы
        model.addAttribute("ascensions_list", ascensions_list);
        model.addAttribute("sponsors_list", sponsors_list);
        return "admin/ascensions/ascension_adding_new_sponsor";
    }

    //POST запрос добавление спонсора восхождению
    @PostMapping("/ascensions/sponsor_add")
    public String ascension_sponsor_adding(@RequestParam("ascension_id") int ascension_id, @RequestParam("sponsor_id") int sponsor_id, @RequestParam("deposit") String deposit)
    {
        ascensionSponsorDao.save(ascension_id, sponsor_id, deposit);
        return "redirect:/admin/ascensions";
    }

    //Форма для отмены восхождения
    @GetMapping("/ascensions/cancel")
    public String ascension_cancel(Model model)
    {
        //получаем список активных восхождений
        List<Ascension> ascensions_list = ascensionDao.show_active();
        //добавляем в модель списки для отображения на странице формы
        model.addAttribute("ascensions_list", ascensions_list);
        return "admin/ascensions/ascension_cancel";
    }

    //POST запрос на отмену восхождения
    @PostMapping("/ascensions/cancel")
    public String ascension_canceling(@RequestParam("asc_id") int asc_id) {
        //запрос на статус восхождения - Отменено
        ascensionDao.ascension_cancel(asc_id);
        //запрос на отмену восхождения у всех участников
        ascensionDao.ascension_members_cancel(asc_id);
        return "redirect:/admin/ascensions";
    }

    //Спонсоры

    //форма отображающая всех спонсоров
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
    public String sponsor_create(@ModelAttribute("sponsor") Sponsor sponsor)
    {
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
}
