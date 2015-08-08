package app.imast.com.findingme;

import java.util.ArrayList;
import java.util.List;

import app.imast.com.findingme.model.District;
import app.imast.com.findingme.model.LostPet;
import app.imast.com.findingme.model.Pet;
import app.imast.com.findingme.model.PetType;
import app.imast.com.findingme.model.Profile;
import app.imast.com.findingme.model.Race;
import app.imast.com.findingme.model.User;

/**
 * Created by aoki on 03/08/2015.
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
