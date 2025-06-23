package vn.edu.tlu.nguyenthithiem.quanlylichsu.models;

public class Clinic {
    public int id;
    public String name;

    public Clinic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // Dùng để hiển thị trong Spinner
    }
    public int getId() {
        return id;
    }
}
