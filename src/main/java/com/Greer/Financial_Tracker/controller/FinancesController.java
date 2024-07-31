package com.Greer.Financial_Tracker.controller;

import com.Greer.Financial_Tracker.model.FinancesEntity;
import com.Greer.Financial_Tracker.model.PersonEntity;
import com.Greer.Financial_Tracker.services.FinanceService;
import com.Greer.Financial_Tracker.services.PersonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class FinancesController {

    PersonService personService;
    FinanceService financeService;

    public FinancesController(PersonService personService, FinanceService financeService) {
        this.personService = personService;
        this.financeService = financeService;
    }
    /*
    Creates a finance entry and stores it in the database. Will redirect to /createProfile if
    a user has not been created in the database. Otherwise, this will redirect to the dashboard
     */
    @PostMapping("/newFinanceEntry")
    public String newFinanceEntry(Model model, @AuthenticationPrincipal OidcUser principal,
                                  @RequestParam BigDecimal cash, @RequestParam BigDecimal assets,
                                  @RequestParam BigDecimal investments, @RequestParam BigDecimal debt,
                                  HttpSession httpSession){

        String userEmail = principal.getEmail();
        Optional<PersonEntity> personEntity = personService.findByEmail(userEmail);

        if(personEntity.isEmpty()){
            return "redirect:/createProfile";
        }

        FinancesEntity financesEntity = FinancesEntity.builder()
                .userId(personEntity.get())
                .date(LocalDate.now())
                .cash(cash)
                .assets(assets)
                .investments(investments)
                .debt(debt)
                .build();


        FinancesEntity savedFinanceEntity = financeService.save(financesEntity);

        System.out.println("Saved new finance entry: " + savedFinanceEntity);

        return "redirect:/dashboard";

    }

    /*
    Responsible for returning a list of financial data for a user. This data is packaged so that it can be used with chart.js
     */
    public ResponseEntity<Map<String, Object>> getFinancialData(PersonEntity personEntity) {

        Map<String, Object> data = new HashMap<>();
        List<FinancesEntity> financesEntityList = financeService.findAllRecordsWithUserId(personEntity.getId());

        if (financesEntityList.size() < 2) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Insufficient data to create graph");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        List<String> dates = extractDates(financesEntityList);
        List<BigDecimal> netWorths = extractNetWorth(financesEntityList);

        Map<String, Object> dataset = new HashMap<>();
        dataset.put("label", "Net Worth");
        dataset.put("data", netWorths);  // Properly structured as an array of data
        dataset.put("backgroundColor", "rgba(75, 192, 192, 0.2)");
        dataset.put("borderColor", "rgba(75, 192, 192, 1)");
        dataset.put("borderWidth", 1);

        List<Map<String, Object>> datasets = new ArrayList<>();
        datasets.add(dataset);

        data.put("labels", dates);
        data.put("datasets", datasets);
        return ResponseEntity.ok(data);
    }



    /*
    Extracts the dates from the financeEntities and returns the list of dates.
     */
    private List<String> extractDates(List<FinancesEntity> financesEntityList) {
        List<String> dates = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(FinancesEntity entity : financesEntityList){
            LocalDate date = entity.getDate();
            if (date != null) {
                String formattedDate = date.format(formatter);
                dates.add(formattedDate);
            }
        }
        return dates;
    }


    /*
    extracts the net worth from the finance entities and returns a list of net worths
     */
    private List<BigDecimal> extractNetWorth(List<FinancesEntity> financesEntityList) {

        List<BigDecimal> netWorth = new ArrayList<>();

        for(FinancesEntity entity : financesEntityList){
            netWorth.add(entity.getNetWorth());
        }
        return netWorth;
    }


    /*
    Gets the net worth from the most recent finance input
     */
    public BigDecimal getNetWorth(PersonEntity personEntity) {
        //extract most recent finance entity
        FinancesEntity financesEntity = financeService.getRecentFinanceEntity(personEntity);
        if(financesEntity != null){
            return financesEntity.getNetWorth();
        }
        return new BigDecimal(0);
    }
}
