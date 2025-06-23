package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.database.DatabaseHelper;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewForgetPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.importCSVIfFirstRun();

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.importCSVIfFirstRun();


        // Ánh xạ các View
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewForgetPassword = findViewById(R.id.textViewForgetPassword);

        // Xử lý sự kiện click cho nút Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra đăng nhập
                    User user = databaseHelper.checkUser(email, password);
                    if (user != null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công! Vai trò: " + user.getRole(), Toast.LENGTH_SHORT).show();

                        //Lưu user_id vào SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("user_id", user.getUserId());
                        editor.putString("user_name", user.getName());
                        editor.putString("user_role", user.getRole());
                        editor.apply();

                        // Chuyển sang HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("user_id", user.getUserId());
                        intent.putExtra("user_name", user.getName());
                        intent.putExtra("user_role", user.getRole());
                        startActivity(intent);  //chuyển sang home
                        finish(); // Đóng Login

                    } else {
                        Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        // Xử lý sự kiện click cho TextView "Sign Up"
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện click cho TextView "Forget password" (có thể thêm logic sau)
        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show();
            }
        });
    }
}