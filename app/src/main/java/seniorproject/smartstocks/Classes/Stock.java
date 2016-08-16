package seniorproject.smartstocks.Classes;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class Stock {

    String Symbol;

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public Stock(String stockSymbol) {
        Symbol = stockSymbol;
    }
}
