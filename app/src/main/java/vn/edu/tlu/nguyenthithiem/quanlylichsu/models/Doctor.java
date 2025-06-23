package vn.edu.tlu.nguyenthithiem.quanlylichsu.models;

public class Doctor {
    private int id;
    private String name;
    private int deptId;
    private String bio;
    private String photoUrl;

    public Doctor(int id, String name, int deptId, String bio, String photoUrl) {
        this.id = id;
        this.name = name;
        this.deptId = deptId;
        this.bio = bio;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDeptId() {
        return deptId;
    }

    public String getBio() {
        return bio;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String toString() {
        return name; // dùng để hiển thị trong Spinner
    }
}
