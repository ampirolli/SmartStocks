package seniorproject.smartstocks.Classes;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class Session {

    final Integer user_id;
    private static Session uniqueInstance;
    private Session(Integer user_id) {
        this.user_id = user_id;
    }
    public static Session getInstance(Integer user_id){
        if(uniqueInstance == null){
            uniqueInstance = new Session(user_id);
        }
        return uniqueInstance;
    }

    public Integer getUser_id() {
        return user_id;
    }


}
