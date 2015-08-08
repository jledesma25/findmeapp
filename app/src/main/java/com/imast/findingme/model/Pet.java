package app.imast.com.findingme.model;

/**
 * 
 */
public class Pet {

    private int id;
    private String name;
    private String sex;
    private int age;
    private String vaccinated;
    private String information;
    private String state;
    private int pet_type_id;
    private int user_id;
    private int race_id;
    private String photo_file_name;
    private String photo;

    public Pet(int id, String name, String sex, int age, String vaccinated, String information, String state, int pet_type_id, int user_id, int race_id, String photo_file_name, String photo) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.vaccinated = vaccinated;
        this.information = information;
        this.state = state;
        this.pet_type_id = pet_type_id;
        this.user_id = user_id;
        this.race_id = race_id;
        this.photo_file_name = photo_file_name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPet_type_id() {
        return pet_type_id;
    }

    public void setPet_type_id(int pet_type_id) {
        this.pet_type_id = pet_type_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRace_id() {
        return race_id;
    }

    public void setRace_id(int race_id) {
        this.race_id = race_id;
    }

    public String getPhoto_file_name() {
        return photo_file_name;
    }

    public void setPhoto_file_name(String photo_file_name) {
        this.photo_file_name = photo_file_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
