package com.imast.findingme;

import java.util.ArrayList;
import java.util.List;

import com.imast.findingme.model.District;
import com.imast.findingme.model.LostPet;
import com.imast.findingme.model.Pet;
import com.imast.findingme.model.PetType;
import com.imast.findingme.model.Profile;
import com.imast.findingme.model.Race;
import com.imast.findingme.model.User;

/**
 * 
 */
public class Config {

    // Is this an internal dogfood build?
    public static final boolean IS_DOGFOOD_BUILD = false;

    // Warning messages for dogfood build
    public static final String DOGFOOD_BUILD_WARNING_TITLE = "Test build";
    public static final String DOGFOOD_BUILD_WARNING_TEXT = "This is a test build.";

    public static List<District> lstDistrict = new ArrayList<District>();
    public static List<PetType> lstPetType = new ArrayList<PetType>();
    public static List<Race> lstRace = new ArrayList<Race>();

    public static User user;
    public static LostPet lostPet;
    public static Pet pet;
    public static Profile profile;

}
