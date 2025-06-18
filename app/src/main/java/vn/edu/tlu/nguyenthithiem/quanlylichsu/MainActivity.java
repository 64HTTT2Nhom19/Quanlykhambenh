package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.importCSVIfFirstRun();
        List<Appointment> appointmentList = new ArrayList<>();

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

        AppointmentAdapter adapter = new AppointmentAdapter(this, appointmentList);
        recyclerView.setAdapter(adapter);
    }
}
