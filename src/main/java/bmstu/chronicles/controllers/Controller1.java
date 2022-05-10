package bmstu.chronicles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Controller1
{
        @GetMapping("/")
        public String greeting(HttpServletRequest request, Model model) {

            System.out.println("type = " + request.getUserPrincipal());
            //model.addAttribute("name", name);
            return "menu";
        }
/*
    @GetMapping("/login")
    public String login(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "login2";
    }
*/


    @GetMapping("/main")
    public String main(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "main";
    }

    @GetMapping("/home/guest")
    public String home_guest(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "guest";
    }

    @GetMapping("/page1")
    public String page1(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "page1";
    }

    @GetMapping("/add")
    public String add_user(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "add_user";
    }
    @GetMapping("/home/admin")
    public String home_admin(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "admin";
    }

/*
    @GetMapping("/admin/")
    public String test_page(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "admin/test_page";
    }
    */

}
