package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    List<Appointment> appointmentList;
    AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        RecyclerView recyclerView = findViewById(R.id.recyclerAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        appointmentList = new ArrayList<>();

        // Lấy dữ liệu từ SQLite
        Cursor cursor = dbHelper.getAllAppointmentsWithDetails();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("user_name"));
                @SuppressLint("Range") String doctorName = cursor.getString(cursor.getColumnIndex("doctor_name"));
                @SuppressLint("Range") String clinicName = cursor.getString(cursor.getColumnIndex("clinic_name"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("appointment_time"));
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));

                appointmentList.add(new Appointment(userName, doctorName, clinicName, time, status));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new AppointmentAdapter(this, appointmentList);
        recyclerView.setAdapter(adapter);
    }
}