package vn.edu.tlu.nguyenthithiem.quanlylichsu;

public class Appointment {
    private String userName;
    private String doctorName;
    private String clinicName;
    private String appointmentTime;
    private String status;

    public Appointment(String userName, String doctorName, String clinicName, String appointmentTime, String status) {
        this.userName = userName;
        this.doctorName = doctorName;
        this.clinicName = clinicName;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public String getUserName() { return userName; }
    public String getDoctorName() { return doctorName; }
    public String getClinicName() { return clinicName; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
}

