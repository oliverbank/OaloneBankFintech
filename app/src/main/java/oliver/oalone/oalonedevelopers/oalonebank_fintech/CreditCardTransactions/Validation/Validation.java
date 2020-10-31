package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Validation;

import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Entities.Card;

/**
 * Created by culqi on 1/19/17.
 */

public class Validation {

    Card oCard = new Card();

    public static Card typeCard(String textCard) {
        ArrayList<Card> oListCard = new ArrayList<>();
        oListCard.add(new Card("VISA", "^4\\d{12}(\\d{3})?$"));
        oListCard.add(new Card("MasterCard", "^(5[1-5]\\d{4}|677189)\\d{10}$"));
        oListCard.add(new Card("Discover", "^6(?:011\\d\\d|5\\d{4}|4[4-9]\\d{3}|22(?:1(?:2[6-9]|[3-9]\\d)|[2-8]\\d\\d|9(?:[01]\\d|2[0-5])))\\d{10}$"));
        oListCard.add(new Card("Amex", "^3[47]\\d{13}$"));
        oListCard.add(new Card("Diners", "^3(0[0-5]|[68]\\d)\\d{11}$"));
        oListCard.add(new Card("JCB", "^35(28|29|[3-8]\\d)\\d{12}$"));
        oListCard.add(new Card("Switch", "^6759\\d{12}(\\d{2,3})?$"));
        oListCard.add(new Card("Solo", "^6767\\d{12}(\\d{2,3})?$"));
        oListCard.add(new Card("Dankort", "^5019\\d{12}$"));
        oListCard.add(new Card("Maestro", "^(5[06-8]|6\\d)\\d{10,17}$"));
        oListCard.add(new Card("Forbrugsforeningen", "^600722\\d{10}$"));
        oListCard.add(new Card("Laser", "^(6304|6706|6771|6709)\\d{8}(\\d{4}|\\d{6,7})?$"));
        oListCard.add(new Card("", ".*"));

        Card result = new Card();
        for(Card obj : oListCard) {
            Pattern r = Pattern.compile(obj.pattern);
            Matcher m = r.matcher(textCard);
            if (m.find()) {
                return obj;
            } else {
                result = null;
            }
        }
        return result;
    }

    public static boolean luhn(String number){
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for(int i = 0 ;i < reverse.length();i++){
            int digit = Character.digit(reverse.charAt(i), 10);
            if(i % 2 == 0){//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            }else{//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if(digit >= 5){
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

    public int bin(String bin, final TextView kind_card) {

        if (bin.length() > 1) {
            Card result = typeCard(bin);
            if (result == null) {
                kind_card.setText("");
            } else {
                if (result.name == "") {
                    kind_card.setText("");
                } else {
                    kind_card.setText(result.name);
                    return 3;
                }
            }
        }
        /* if(bin.length() > 1) {
            if(Integer.valueOf(bin.substring(0,2)) == 41) {
                kind_card.setText("VISA");
                return 3;
            } else if (Integer.valueOf(bin.substring(0,2)) == 51) {
                kind_card.setText("MasterCard");
                return 3;
            } else {
            }
        } else {
            kind_card.setText("");
        }

        if(bin.length() > 1) {
            if(Integer.valueOf(bin.substring(0,2)) == 36){
                kind_card.setText("Diners Club");
                return 3;
            } else if(Integer.valueOf(bin.substring(0,2)) == 38){
                kind_card.setText("Diners Club");
                return 3;
            } else if(Integer.valueOf(bin.substring(0,2)) == 37){
                kind_card.setText("AMEX");
                return 3;
            } else {
            }
        }

        if(bin.length() > 2) {
            if(Integer.valueOf(bin.substring(0,3)) == 300){
                kind_card.setText("Diners Club");
                return 3;
            } else if(Integer.valueOf(bin.substring(0,3)) == 305){
                kind_card.setText("Diners Club");
                return 3;
            } else {
            }
        } */
        return 0;
    }

    public boolean month(String month) {
        if(!month.equals("")){
            if(Integer.valueOf(""+month) > 12) {
                return true;
            }
        }
        return false;
    }

    public boolean year(String year){
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if(!year.equals("")){
            if(Integer.valueOf("20"+year) < calendar.get(Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

}
