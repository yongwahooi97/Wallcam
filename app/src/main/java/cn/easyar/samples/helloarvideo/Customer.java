package cn.easyar.samples.helloarvideo;

/**
 * Created by OoiYongWah on 27/12/2017.
 */

public class Customer {
    private String name;
    private String email;
    private String passsword;
    private String DOB;
    private String mobile;

    public Customer(){

    }

    public Customer(String name, String email, String passsword, String DOB, String mobile) {
        this.name = name;
        this.email = email;
        this.passsword = passsword;
        this.DOB = DOB;
        this.mobile = mobile;
    }

    public Customer(String email, String passsword) {
        this.email = email;
        this.passsword = passsword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
