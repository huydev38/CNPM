package com.example.uet_tty.controller;

import com.example.uet_tty.entity.User;
import com.example.uet_tty.service.ExpertService;
import com.example.uet_tty.service.LoginService;
import com.example.uet_tty.service.StudentService;
import com.example.uet_tty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;

    @Autowired
    ExpertService expertService;

    @GetMapping("/login")
    public String login(){
        return "login-page.html";
    }

    @PostMapping("/login")
    public String login(Model model, HttpSession session, @RequestParam(value = "username", required = false) String username
            , @RequestParam("password") String password){

        if(loginService.check(username,password)){
            session.setAttribute("user_id",userService.findByUsername(username).getUser_id());
            session.setAttribute("user_name", username);
                return "redirect:/homepage";
            }
        String message="Incorrect username or password";
        model.addAttribute("msg",message);
        return "login-page.html";
    }




    @GetMapping({"/homepage","/"})
    public String homepage(HttpSession session, Model model){
        model.addAttribute("username",session.getAttribute("user_name"));
        if(userService.findByUsername((String) session.getAttribute("user_name")).getRole()==1) {
            model.addAttribute("id", studentService.searchByUserId((int) session.getAttribute("user_id")).getStudent_id());
            return "homepage.html";
        }else {
            model.addAttribute("id", expertService.getIdByUserId ((int) session.getAttribute("user_id")));
            return "homepage-expert.html";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
