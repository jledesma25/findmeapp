package app.imast.com.findingme.model;

/**
 * Created by XKenokiX on 07/08/2015.
 */
public class MySearch {

    private int id;
    private int lost_pet_id;
    private int user_id;
    private LostPet lost_pet;

    public MySearch(int id, int lost_pet_id, int user_id, LostPet lost_pet) {
        this.id = id;
        this.lost_pet_id = lost_pet_id;
        this.user_id = user_id;
        this.lost_pet = lost_pet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLost_pet_id() {
        return lost_pet_id;
    }

    public void setLost_pet_id(int lost_pet_id) {
        this.lost_pet_id = lost_pet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LostPet getLost_pet() {
        return lost_pet;
    }

    public void setLost_pet(LostPet lost_pet) {
        this.lost_pet = lost_pet;
    }

}
