package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.database.DatabaseHelper;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.Appointment;

public class AppointmentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    List<Appointment> appointmentList;
    AppointmentAdapter adapter;
    private int userId;
    private String userName, userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        //Khởi tạo
        recyclerView = findViewById(R.id.recyclerAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        appointmentList = new ArrayList<>();

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        userRole = intent.getStringExtra("user_role");
        userName = intent.getStringExtra("user_name");
        //Tải dữ liệu ban đầu
        loadAppointments();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments(); // Tải lại khi quay lại màn hình
    }
    private void loadAppointments(){
        appointmentList.clear();

        //Phân quyền truy vấn dữ liệu
        Cursor cursor;
        if ("admin".equalsIgnoreCase(userRole)) {
            cursor = dbHelper.getAllAppointmentsWithDetails();
        } else {
            cursor = dbHelper.getAppointmentsByUserId(userId);
        }

        //Đọc dữ liệu từ cursor
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("appointment_id"));
                @SuppressLint("Range") String uName = cursor.getString(cursor.getColumnIndex("user_name"));
                @SuppressLint("Range") String deptName = cursor.getString(cursor.getColumnIndex("department_name"));
                @SuppressLint("Range") String docName = cursor.getString(cursor.getColumnIndex("doctor_name"));
                @SuppressLint("Range") String clinicName = cursor.getString(cursor.getColumnIndex("clinic_name"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("appointment_time"));
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));

                appointmentList.add(new Appointment(id, uName, deptName, docName, clinicName, time, status));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new AppointmentAdapter(this, appointmentList,dbHelper, userRole);
        recyclerView.setAdapter(adapter);

        // Gọi hàm điều hướng
        NavigationUtil.setupBottomNavigation(this);
    }
}