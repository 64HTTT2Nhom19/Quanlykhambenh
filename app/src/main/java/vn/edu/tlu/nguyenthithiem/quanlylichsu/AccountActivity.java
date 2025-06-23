package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.database.DatabaseHelper;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.User;

public class AccountActivity extends AppCompatActivity {

    TextView textUserName, textUserRole;
    Button btnViewProfile, btnSupport, btnLogout;
    private DatabaseHelper dbHelper;
    int userId;
    String userName, userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        textUserName = findViewById(R.id.textUserName);
        textUserRole = findViewById(R.id.textUserRole);
        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnSupport = findViewById(R.id.btnSupport);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);

        // Nhận user_id từ HomeActivity
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        userName = intent.getStringExtra("user_name");
        userRole = intent.getStringExtra("user_role");

        // Gán dữ liệu lên UI
        if (userName != null) {
            textUserName.setText(userName);
            textUserRole.setText("Vai trò: " + userRole);
        }

        btnViewProfile.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Chức năng Hồ sơ cá nhân đang phát triển.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        // Trung tâm hỗ trợ
        btnSupport.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Trung tâm hỗ trợ")
                    .setMessage("Vui lòng liên hệ email: support@clinicapp.vn hoặc hotline: 0123 456 789")
                    .setPositiveButton("Đóng", null)
                    .show();
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất không?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        Intent i = new Intent(AccountActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }
}
