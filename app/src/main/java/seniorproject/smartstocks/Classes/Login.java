package seniorproject.smartstocks.Classes;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class Login {

    private String Email;
    private String HashedPassword;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHashedPassword() {
        return HashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        HashedPassword = hashedPassword;
    }

    public Login(String email, String Password) {
        setEmail(email);
        setHashedPassword(Password); //WE ARE GOING TO ENCORPERATE AN ENCRYPTION METHOD FOR THE PASSWORD
    }




}
