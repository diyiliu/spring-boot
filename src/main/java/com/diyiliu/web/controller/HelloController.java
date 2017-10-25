package com.diyiliu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: HelloController
 * Author: DIYILIU
 * Update: 2017-10-24 14:03
 */

@Controller
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    public String hello() {

        return "Hello, world!";
    }

    @RequestMapping("/index")
    public String index(@RequestParam(defaultValue = "world") String name, Model model){
        model.addAttribute("message", "你好, " + name);

        return "index";
    }

    @RequestMapping("/search")
    public String search(){

        return "search";
    }
}
