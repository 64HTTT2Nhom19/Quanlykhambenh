package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.database.DatabaseHelper;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.Clinic;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.Doctor;

public class BookingActivity extends AppCompatActivity {
        private TimePicker timePicker;
        private DatePicker datePicker;
        private Button button;
        private DatabaseHelper dbHelper;
        private Spinner spinnerClinic, spinnerDoctor;
        private String selectedDoctor;
        private String selectedClinic;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_booking);

            // Ánh xạ view
            timePicker = findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            datePicker = findViewById(R.id.datePicker);
            button = findViewById(R.id.btnBook);
            spinnerDoctor = findViewById(R.id.spinner);
            spinnerClinic = findViewById(R.id.spinnerClinic);

            dbHelper = new DatabaseHelper(this);

            // Lấy danh sách bác sĩ và cơ sở từ CSDL
            List<Doctor> doctorList = dbHelper.getAllDoctors();
            List<Clinic> clinicList = dbHelper.getAllClinics();
            // Tạo adapter
            ArrayAdapter<Doctor> spinnerAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    doctorList);

            // Giao diện dropdown
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Gắn adapter vào Spinner
            spinnerDoctor.setAdapter(spinnerAdapter);

            //Adapter cho spinner cơ sở
            ArrayAdapter<Clinic> clinicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clinicList);
            clinicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClinic.setAdapter(clinicAdapter);

            // Xử lý sự kiện chọn bác sĩ
            spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDoctor = parent.getItemAtPosition(position).toString();
                    Toast.makeText(BookingActivity.this, "Bác sĩ đã chọn: " + selectedDoctor, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không làm gì
                }
            });
            // xử lý sự kiện chọn cơ sở
            spinnerClinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedClinic = parent.getItemAtPosition(position).toString();
                    Toast.makeText(BookingActivity.this, "Cơ sở đã chọn: " + selectedClinic, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Button đặt lịch
            button.setOnClickListener(v -> {
                // Kiểm tra điều kiện trống lịch
                if (selectedDoctor.isEmpty() || selectedClinic.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Vui lòng chọn bác sĩ và cơ sở.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Lấy thời gian
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();//Tháng bắt đầu từ 0
                int year = datePicker.getYear();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Định dạng thời gian hẹn: yyyy-MM-dd HH:mm
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day, hour, minute, 0); //cập nhật thời gian bằng giá trị ngời dùng chọn
                selectedDate.set(Calendar.MILLISECOND, 0); // đặt về miligiaay để so sánh chính xác
                String appointmentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(selectedDate.getTime());

                //tạo đối tượng now cho thời gian hiện tại
                Calendar now = Calendar.getInstance();
                now.set(Calendar.SECOND, 0);
                now.set(Calendar.MILLISECOND, 0);

                if (selectedDate.before(Calendar.getInstance())) {
                    Toast.makeText(this, "Thời gian không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Lấy đối tượng đã chọn từ Spinner
                Doctor selectedDoctor = (Doctor) spinnerDoctor.getSelectedItem();
                Clinic selectedClinic = (Clinic) spinnerClinic.getSelectedItem();

                // Lấy user_id từ SharedPreferences
                SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                int userId = preferences.getInt("user_id", -1); // -1 nếu chưa đăng nhập

                // Gọi hàm lưu lịch hẹn
                boolean result = dbHelper.insertAppointment(
                        userId,
                        selectedDoctor.getId(),
                        selectedClinic.getId(),
                        appointmentTime
                );
                if (result) {
                    Toast.makeText(this, "Bạn đã đặt lịch khám thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Lỗi khi đặt lịch. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                }
            });
            // Gọi hàm điều hướng
            NavigationUtil.setupBottomNavigation(this);
        }
}
