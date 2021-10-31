package com.sylvain.safetyAlerts.controller;

import com.sylvain.safetyAlerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class URLController {

    @Autowired
    private IPersonService personService;

    @GetMapping("communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city){
        return personService.getCommunityEmail(city);
    }

}
