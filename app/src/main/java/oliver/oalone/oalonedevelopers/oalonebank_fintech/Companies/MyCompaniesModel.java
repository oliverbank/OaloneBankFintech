package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

public class MyCompaniesModel {

    String company_image, company_name,company_ruc,company_verification,credit_score;

    public MyCompaniesModel() {
    }

    public MyCompaniesModel(String company_image, String company_name, String company_ruc, String company_verification, String credit_score) {
        this.company_image = company_image;
        this.company_name = company_name;
        this.company_ruc = company_ruc;
        this.company_verification = company_verification;
        this.credit_score = credit_score;
    }

    public String getCompany_image() {
        return company_image;
    }

    public void setCompany_image(String company_image) {
        this.company_image = company_image;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_ruc() {
        return company_ruc;
    }

    public void setCompany_ruc(String company_ruc) {
        this.company_ruc = company_ruc;
    }

    public String getCompany_verification() {
        return company_verification;
    }

    public void setCompany_verification(String company_verification) {
        this.company_verification = company_verification;
    }

    public String getCredit_score() {
        return credit_score;
    }

    public void setCredit_score(String credit_score) {
        this.credit_score = credit_score;
    }
}
