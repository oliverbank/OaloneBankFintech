package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Entities;

public class Card {

    public String name;
    public String pattern;

    public Card(String name, String pattern){
        this.name = name;
        this.pattern = pattern;
    }

    public Card() {
        super();
    }
}
