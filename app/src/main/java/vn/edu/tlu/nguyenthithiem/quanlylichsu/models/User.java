package vn.edu.tlu.nguyenthithiem.quanlylichsu.models;

public class User {
    private int userId;
    private String name;
    private String email;
    private String gender;
    private String phone;
    private String password;
    private String role;

    public User(int userId, String name, String email, String gender, String phone, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Setters (if needed, but for a model class, often only getters are used after construction)
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}