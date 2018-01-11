package cn.easyar.samples.helloarvideo;

/**
 * Created by OoiYongWah on 6/1/2018.
 */

public class Appointment {
    private String residentName,address, contact, appointmentDate, appointmentTime, product;

    public Appointment() {
    }

    public Appointment(String residentName, String address, String contact, String appointmentDate, String appointmentTime, String product) {
        this.residentName = residentName;
        this.address = address;
        this.contact = contact;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.product = product;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
