package seniorproject.smartstocks;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class LICS {
    public static String LoginConnectionString()
    {
        String connection = "jdbc:sqlserver://localhost:1433;" + "databaseName=SE414_Group3;user=SE414_group3;password=ahanan";
        // "Server=sql.neit.edu;Database=SE417_AP;User Id=001329606;Password=001329606;";
        return connection;
    }
}
