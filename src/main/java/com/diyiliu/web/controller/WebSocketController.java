package com.diyiliu.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;

/**
 * Description: WebSocketController
 * Author: DIYILIU
 * Update: 2018-01-16 10:57
 */

@Controller
public class WebSocketController {

    @GetMapping("/greeting")
    public String index(){

        return "ws";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String say(Principal principal, HashMap map) {
        System.out.println(map.get("name"));

        return "Hello, " + map.get("name") + " !";
    }
}
