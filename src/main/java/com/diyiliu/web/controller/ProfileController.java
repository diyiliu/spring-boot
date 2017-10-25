package com.diyiliu.web.controller;

import com.diyiliu.support.format.CNLocalDateFormatter;
import com.diyiliu.web.model.ProfileForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

/**
 * Description: ProfileController
 * Author: DIYILIU
 * Update: 2017-10-24 20:03
 */

@Controller
public class ProfileController {

    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm){

        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(ProfileForm profileForm){
        System.out.println("save ok: " + profileForm);

        return "redirect:/profile";
    }


    @ModelAttribute("dateFormat")
    public String localeFormat(Locale locale){

        return CNLocalDateFormatter.getPattern(locale);
    }
}
