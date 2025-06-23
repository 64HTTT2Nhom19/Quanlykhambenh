package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class HomeActivity extends AppCompatActivity {
    private int userId;
    private String userName;
    private String userRole;


    private TextView textViewWelcomeHome;
    private LinearLayout btnHoiDap, btnLichHen, btnTrangChu, btnThongBao, btnTaiKhoan;
    private ViewPager2 imageSlider;
    private Button btnDichVu, btnTraCuu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Lấy dữ liệu user từ Intent
        userId = getIntent().getIntExtra("user_id", -1);
        userName = getIntent().getStringExtra("user_name");
        userRole = getIntent().getStringExtra("user_role");

        imageSlider = findViewById(R.id.imageSlider);
        btnDichVu = findViewById(R.id.btnDichVu);
        btnTraCuu = findViewById(R.id.btnTraCuu);
        btnHoiDap = findViewById(R.id.btn_hoi_dap);
        btnLichHen = findViewById(R.id.btn_lich_hen);
        btnTrangChu = findViewById(R.id.btn_trang_chu);
        btnThongBao = findViewById(R.id.btn_thong_bao);
        btnTaiKhoan = findViewById(R.id.btn_tai_khoan);


        int[] imageList = {R.drawable.image1, R.drawable.image2};
        ImageAdapter adapter = new ImageAdapter(this, imageList);
        imageSlider.setAdapter(adapter);
        // xử lý sự kiện click ở màn hình
        btnDichVu.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("user_name", userName);
            intent.putExtra("user_role", userRole);
            startActivity(intent);
        });

        btnTraCuu.setOnClickListener(v -> {});

        // Gọi hàm điều hướng
        NavigationUtil.setupBottomNavigation(this);
    }
}