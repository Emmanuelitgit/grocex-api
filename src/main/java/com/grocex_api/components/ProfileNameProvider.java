package com.grocex_api.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileNameProvider {

    @Autowired
    private Environment environment;

    public String getActiveProfileName() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            return activeProfiles[0];
        }
        return "No active profile found";
    }

    public String getFilePropertyPath(){
        if (getActiveProfileName().equals("dev")){
            return "http://localhost:5000/";
        }else {
            return "https://rent-management-system-uyyb.onrender.com/";
        }
    }

}