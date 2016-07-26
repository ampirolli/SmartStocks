package seniorproject.smartstocks;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class LICS {
    public static String LoginConnectionString()
    {
        String connection = "jdbc:jtds:sqlserver://sql.neit.edu/" + "SE414_Group3;user=SE414_group3;password=ahanan";
        return connection;
    }
}
