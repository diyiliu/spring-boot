package com.diyiliu.web.controller;

import com.diyiliu.support.config.UserProfileSession;
import com.diyiliu.support.format.CNLocalDateFormatter;
import com.diyiliu.web.model.ProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

/**
 * Description: ProfileController
 * Author: DIYILIU
 * Update: 2017-10-24 20:03
 */

@Controller
public class ProfileController {

    @Autowired
    private UserProfileSession userProfileSession;

    @ModelAttribute
    public ProfileForm getProfileForm() {
        return userProfileSession.toForm();
    }

    /**
     * 必须添加profileForm 参数，
     * 否则跳转页面会报错
     *
     * @param profileForm
     * @return
     */
    @RequestMapping("/profile")
    public String displayProfile(ProfileForm profileForm) {

        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/profilePage";
        }

        System.out.println("save ok: " + profileForm);
        userProfileSession.saveForm(profileForm);

        return "redirect:/profile";
    }

    @RequestMapping(value = "/profile", params = {"addTaste"})
    public String addRow(ProfileForm profileForm) {
        profileForm.getTastes().add(null);
        return "profile/profilePage";
    }

    @RequestMapping(value = "/profile", params = {"removeTaste"})
    public String removeRow(ProfileForm profileForm, HttpServletRequest req) {
        Integer rowId = Integer.valueOf(req.getParameter("removeTaste"));
        profileForm.getTastes().remove(rowId.intValue());
        return "profile/profilePage";
    }


    @ModelAttribute("dateFormat")
    public String localeFormat(Locale locale) {

        return CNLocalDateFormatter.getPattern(locale);
    }
}
