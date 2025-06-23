package vn.edu.tlu.nguyenthithiem.quanlylichsu.models;

public class Appointment {
    private String userName;
    private String departmentName;
    private String doctorName;
    private String clinicName;
    private String appointmentTime;
    private String status;
    private int id;

    public Appointment(String userName,String departmentName, String doctorName, String clinicName, String appointmentTime, String status) {
        this.userName = userName;
        this.departmentName = departmentName;
        this.doctorName = doctorName;
        this.clinicName = clinicName;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public String getUserName() { return userName; }
    public String getDepartmentName() { return departmentName; }
    public String getDoctorName() { return doctorName; }
    public String getClinicName() { return clinicName; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }

    public int getId() {
        return id;
    }
    // Setter cho status (để sửa trạng thái)
    public void setStatus(String status) {
        this.status = status;
    }
}

