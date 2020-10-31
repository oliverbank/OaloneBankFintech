package oliver.oalone.oalonedevelopers.oalonebank_fintech;

public class UsersModel {

    public String profileimage,username;

    public UsersModel() {
    }

    public UsersModel(String profileimage, String username) {
        this.profileimage = profileimage;
        this.username = username;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
