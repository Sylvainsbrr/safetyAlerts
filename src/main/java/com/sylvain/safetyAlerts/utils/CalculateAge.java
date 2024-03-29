package com.sylvain.safetyAlerts.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculateAge {
    public static boolean isMineur(String sBirthdate) {
        SimpleDateFormat formatDate = new SimpleDateFormat("DD/MM/YYYY");

        try {
            Date birthdate = formatDate.parse(sBirthdate);
            Date today = new Date();

            long todayLong = today.getTime();
            long birthdateLong = birthdate.getTime();

            long diff = todayLong - birthdateLong;
            long anneeDiff = diff / 1000 / 60 / 60 / 24 / 365;

            if (anneeDiff >= 18) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            // Si la date de naissance n'a pas le bon format la personne seras considéré en temps qu'adulte
        }

        return false;
    }

    public static int getAge(String sBirthdate) {
        SimpleDateFormat formatDate = new SimpleDateFormat("DD/MM/YYYY");

        try {
            Date birthdate = formatDate.parse(sBirthdate);
            Date today = new Date();

            long todayLong = today.getTime();
            long birthdateLong = birthdate.getTime();

            long diff = todayLong - birthdateLong;
            long anneeDiff = diff / 1000 / 60 / 60 / 24 / 365;

            return (int)anneeDiff;

        } catch (ParseException e) {
            // Si la date de naissance n'a pas le bon format la personne seras considéré en temps qu'adulte
        }

        return 0;
    }

}
