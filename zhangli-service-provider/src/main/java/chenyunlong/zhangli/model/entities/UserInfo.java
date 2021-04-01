package chenyunlong.zhangli.model.entities;


public class UserInfo {

    private Long id;
    private String name;
    private byte gender;
    private String ImgName;
    private String ImgUrl;
    private User user;

    public UserInfo() {
    }

    public UserInfo(Long id, String name, byte gender, String imgName, String imgUrl, User user) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        ImgName = imgName;
        ImgUrl = imgUrl;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getImgName() {
        return ImgName;
    }

    public void setImgName(String imgName) {
        ImgName = imgName;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", ImgName='" + ImgName + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                ", user=" + user +
                '}';
    }
}
