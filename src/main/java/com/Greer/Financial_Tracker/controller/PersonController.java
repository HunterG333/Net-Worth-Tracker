package com.Greer.Financial_Tracker.controller;

import com.Greer.Financial_Tracker.model.PersonEntity;
import com.Greer.Financial_Tracker.services.impl.PersonServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class PersonController {

    PersonServiceImpl personService;

    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @PostMapping("/updateProfile")
    public String updateProfile(Model model, @AuthenticationPrincipal OidcUser principal, @RequestParam String name, @RequestParam LocalDate date){
        String email = principal.getEmail();

        System.out.println("update profile");

        //user already exists, redirect back to the dashboard
        if(!personService.findByEmail(email).isEmpty()){
            System.out.println("redirect dashboard");
            return "redirect:/dashboard";
        }

        PersonEntity newPerson = PersonEntity.builder()
                .dateOfBirth(date)
                .email(email)
                .name(name)
                .build();

        personService.save(newPerson);

        model.addAttribute("name", name);

        System.out.println("redirect dashboard but success");

        return "redirect:/dashboard";
    }

    @GetMapping("/createProfile")
    public String createProfile(Model model, @AuthenticationPrincipal OidcUser principal){

        //user already exists, redirect back to the dashboard
        String email = principal.getEmail();
        if(!personService.findByEmail(email).isEmpty()){
            System.out.println("dashboard redirect");
            return "redirect:/dashboard";
        }

        System.out.println("info form");
        return "profileInformationForm";
    }


}
