package seniorproject.smartstocks.Classes;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class User {
    private String FirstName;
    private String LastName;
    private String Email;
    private String Phone;
    private String BirthDate;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public User(String firstName, String lastName, String email, String phone, String birthDate) {

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setBirthDate(birthDate);
    }
    //execute sp_user_registration 'Owner', 'Anthony', 'Pirolli', 'ampirolli831@gmail.com', '4015555555', '08-31-1995', 'password', NULL


}
