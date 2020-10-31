package oliver.oalone.oalonedevelopers.oalonebank_fintech;

public class OliverBankAccountsModel {
    String cc_code, cci_code,currrency,image,name;

    public OliverBankAccountsModel() {
    }

    public OliverBankAccountsModel(String cc_code, String cci_code, String currrency, String image, String name) {
        this.cc_code = cc_code;
        this.cci_code = cci_code;
        this.currrency = currrency;
        this.image = image;
        this.name = name;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getCci_code() {
        return cci_code;
    }

    public void setCci_code(String cci_code) {
        this.cci_code = cci_code;
    }

    public String getCurrrency() {
        return currrency;
    }

    public void setCurrrency(String currrency) {
        this.currrency = currrency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
