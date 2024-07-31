package com.Greer.Financial_Tracker.controller;

import com.Greer.Financial_Tracker.model.PersonEntity;
import com.Greer.Financial_Tracker.services.impl.PersonServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.Optional;

@Controller
public class ViewController {

    PersonServiceImpl personService;
    FinancesController financesController;

    public ViewController(PersonServiceImpl personService, FinancesController financesController) {
        this.personService = personService;
        this.financesController = financesController;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser principal){
        if (principal != null) {
            model.addAttribute("profile", principal.getClaims());

            Object profileObj = model.getAttribute("profile");

            if (profileObj instanceof Map) {
                Map<String, Object> claims = (Map<String, Object>) profileObj;

                String email = principal.getEmail();

                Optional<PersonEntity> personEntity = personService.findByEmail(email);

                if(personEntity.isEmpty()){
                    System.out.println("createProfile1 return");
                    return"redirect:/createProfile";
                }

                //get id and set model id
                model.addAttribute("userId", personEntity.get().getId());

                PersonEntity newPerson = personEntity.get();
                if(newPerson.getName().isEmpty()){
                    System.out.println("createProfile2 return");
                    return"redirect:/createProfile";
                }
                model.addAttribute("name", personEntity.get().getName());
            }


            System.out.println("dashboard redirect");
            return "redirect:/dashboard";
        }

        System.out.println("index return");
        return "index";

    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal OidcUser principal){

        if(principal == null){
            return "redirect:/login";
        }

        String email = principal.getEmail();
        Optional<PersonEntity> personEntity = personService.findByEmail(email);

        if (personEntity.isEmpty()) {
            return "redirect:/login";
        }

        //get data from finances controller
        ResponseEntity<Map<String, Object>> financialDataResponseEntity = financesController.getFinancialData(personEntity.get());
        if(financialDataResponseEntity.getStatusCode().is2xxSuccessful()){
            System.out.println(financialDataResponseEntity.getBody());
            model.addAttribute("financialData", financialDataResponseEntity.getBody());
        }else{
            //change some attribute so that there isnt a big empty space on the dashboard screen
        }

        model.addAttribute("netWorth", financesController.getNetWorth(personEntity.get()));
        model.addAttribute("name", personEntity.get().getName());
        return "dashboard";
    }

    @GetMapping("/financeForm")
    public String financeForm(){
        return "financeForm";
    }


}
