package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.database.DatabaseHelper;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewSignIn;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Ánh xạ các View
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        // Xử lý sự kiện click cho nút Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.checkEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Email đã tồn tại. Vui lòng sử dụng email khác.", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo đối tượng User mới với vai trò mặc định là 'user'
                    User newUser = new User(0, fullName, email, "", "", password, "user");
                    boolean isInserted = databaseHelper.addUser(newUser);

                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển về LoginActivity sau khi đăng ký thành công
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Đóng RegisterActivity
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Xử lý sự kiện click cho TextView "Sign In"
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng RegisterActivity
            }
        });
    }
}
