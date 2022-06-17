package bmstu.chronicles.controllers;

import bmstu.chronicles.dao.*;
import bmstu.chronicles.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/climber")
public class ClimberController
{
    //Добавление компонентов, хранящих функции работы с базой данных
    private final PersonDao personDao;
    private final MountainDao mountainDao;
    private final AscensionDao ascensionDao;
    private final SponsorDao sponsorDao;
    private final AscensionSponsorDao ascensionSponsorDao;
    private final RqsDao rqsDao;

    @Autowired
    public ClimberController(PersonDao personDao, MountainDao mountainDao, AscensionDao ascensionDao, SponsorDao sponsorDao, AscensionSponsorDao ascensionSponsorDao, RqsDao rqsDao) {
        this.personDao = personDao;
        this.mountainDao = mountainDao;
        this.ascensionDao = ascensionDao;
        this.sponsorDao = sponsorDao;
        this.ascensionSponsorDao = ascensionSponsorDao;
        this.rqsDao = rqsDao;
    }

    //Метод вывода главной страницы
    @GetMapping("/")
    public String home_climber(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
    {
        model.addAttribute("name", name);
        return "climber/home_climber";
    }

    //Метод вывода главной страницы
    @GetMapping("/about")
    //О СИСТЕМЕ
    public String about()
    {
        return "climber/about";
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
        return "climber/persons/persons";
    }

    //Форма выбора пользователя
    @GetMapping("/person_info")
    public String person_info1(Model model)
    {
        List<Person> list = personDao.index("last_name");
        model.addAttribute("persons", list);
        return "climber/persons/person_info1";
    }

    //Форма отображения информации о пользователе
    @PostMapping("/person")
    public String person_info2(@RequestParam("id") int id, Model model)
    {
        Person person = personDao.show(id);
        model.addAttribute("person", person);
        return "climber/persons/person_info2";
    }

    //Форма подсчета количества восхождений на каждую гору
    @PostMapping("/person/person_count_ascension_on_mountains/")
    public String person_count_ascension_on_mountains(@RequestParam("id") int id, Model model)
    {
        PersonMntCount pmc = rqsDao.personMntCount(id);
        model.addAttribute("pmc", pmc);
        return "climber/persons/person_mountain_count";
    }

    //Форма отображающая все восхождения альпиниста, отсортированные по дате
    @PostMapping("/person/person_ascensions/")
    public String person_ascensions(@RequestParam("id") int id, Model model)
    {
        PersonAscensions personAscensions = rqsDao.personAscensions(id);
        model.addAttribute("personAscensions", personAscensions);
        return "climber/persons/person_ascensions";
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
        return "climber/persons/person_sign_by_yourself";
    }

    //POST запрос на запись альпиниста на восхождение
    @PostMapping("persons/singing")
    public String person_singing(@RequestParam("asc_id") int asc_id, @RequestParam("person_id") int person_id, Model model) {
        ascensionDao.sign_person(asc_id, person_id);
        return "redirect:/climber/ascensions/";
    }

    //Горные вершины

    //форма отображающая все горные вершины
    @GetMapping("/mountains")

    public String show_mountains(Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        model.addAttribute("mountains_list", mountains_list);
        return "climber/mountains/mountains";
    }

    //Форма выбора горы
    @GetMapping("/mountain_info")
    //Форма выбора пользователя
    public String mountain_info1(Model model)
    {
        List<Mountain> mountains_list = mountainDao.index();
        model.addAttribute("mountains_list", mountains_list);
        return "climber/mountains/mountain_info1";
    }

    //Форма отображения информации о горной вершине
    @PostMapping("/mountain")
    public String mountain_info2(@RequestParam("id") int id, Model model)
    {
        Mountain mountain = mountainDao.show(id);
        model.addAttribute("mountain", mountain);
        return "climber/mountains/mountain_info2";
    }

    //форма отображающая горных вершин, на которые совершались восхождения членами клуба
    @GetMapping("/mountains/asc_by_members")
    public String asc_by_members(Model model)
    {
        List<Mountain> mountains_list = mountainDao.show_asc_by_members();
        model.addAttribute("mountains_list", mountains_list);
        return "climber/mountains/mountains_asc_by_members";
    }

    //форма отображающая количество альпинистов, совершавших восхождения на каждую гору
    @GetMapping("/mountains/mount_per_count")
    public String mount_per_count(Model model)
    {
        List<MountCount> mountCount_list = mountainDao.mount_per_count();
        model.addAttribute("mountCount_list", mountCount_list);
        return "climber/mountains/mountains_per_count";
    }

    //форма отображающая восхождения в хронологическом порядке на горе
    @GetMapping("/mountains/mountain_show_asc_by_date/")
    public String mountain_show_asc_by_date(@RequestParam("mountain_id") int mountain_id, @RequestParam("mountain_name") String mountain_name, Model model)
    {
        List<Ascension> ascensions_list = mountainDao.show_asc_by_date_for_mountain(mountain_id);
        model.addAttribute("ascensions_list", ascensions_list);
        model.addAttribute("mountain_name", mountain_name);
        return "climber/mountains/mountain_show_asc_by_date";
    }

    //Форма отображающая для каждой горы все восхождения в хронологическом порядке.
    @GetMapping("/mountains/mount_asc")
    public String mount_asc(Model model)
    {
        List<MountAsc> mount_asc_list = rqsDao.show_mount_asc();
        model.addAttribute("mount_asc_list", mount_asc_list);
        return "climber/mountains/mount_asc";
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
        return "climber/ascensions/ascensions";
    }

    //Форма выбора восхождения
    @GetMapping("/ascension_info")
    //Форма выбора восхождения
    public String ascension_info1(Model model)
    {
        List<Ascension> ascensions_list = ascensionDao.index();
        model.addAttribute("ascensions_list", ascensions_list);
        return "climber/ascensions/ascension_info1";
    }

    //Форма отображения информации о восхождении
    @PostMapping("/ascension")
    public String ascension_info2(@RequestParam("id") int id, Model model)
    {
        Ascension ascension = ascensionDao.show(id);
        Person climber = personDao.show(ascension.getLeader_id());
        Mountain mountain = mountainDao.show(ascension.getMountain_id());
        List<SponsorDeposit> ascensionSponsor_list = ascensionSponsorDao.getSponsors(id);
        List<Person> members_list = ascensionDao.getMembers(id);

        model.addAttribute("members_list", members_list);
        model.addAttribute("climber", climber);
        model.addAttribute("mountain", mountain);
        model.addAttribute("ascension", ascension);
        model.addAttribute("asc_sp_list", ascensionSponsor_list);
        return "climber/ascensions/ascension_info2";
    }

    //Спонсоры

    //форма отображающая всех спонсоров
    @GetMapping("/sponsors")

    public String show_sponsors(Model model)
    {
        List<Sponsor> sponsors_list = sponsorDao.index();
        model.addAttribute("sponsors_list", sponsors_list);
        return "climber/sponsors/sponsors";
    }

    //Форма выбора спонсора
    @GetMapping("/sponsor_info")
    public String sponsor_info1(Model model)
    {
        List<Sponsor> sponsors_list = sponsorDao.index();
        model.addAttribute("sponsors_list", sponsors_list);
        return "climber/sponsors/sponsor_info1";
    }

    //Форма отображения информации о спонсоре
    @PostMapping("/sponsor")
    public String sponsor_info2(@RequestParam("id") int id, Model model)
    {
        Sponsor sponsor = sponsorDao.show(id);
        model.addAttribute("sponsor", sponsor);
        return "climber/sponsors/sponsor_info2";
    }
}
