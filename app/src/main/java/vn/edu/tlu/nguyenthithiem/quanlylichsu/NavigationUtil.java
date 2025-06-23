package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

public class NavigationUtil {

    public static void setupBottomNavigation(final Activity activity) {
        LinearLayout btnHome = activity.findViewById(R.id.btn_trang_chu);
        LinearLayout btnHoidap = activity.findViewById(R.id.btn_hoi_dap);
        LinearLayout btnLichhen = activity.findViewById(R.id.btn_lich_hen);
        LinearLayout btnThongbao = activity.findViewById(R.id.btn_thong_bao);
        LinearLayout btnTaikhoan = activity.findViewById(R.id.btn_tai_khoan);

        SharedPreferences prefs = activity.getSharedPreferences("user_session", Activity.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        String userName = prefs.getString("user_name", "");
        String userRole = prefs.getString("user_role", "");
        //Trang chủ
        btnHome.setOnClickListener(v -> {
            if (!(activity instanceof HomeActivity)) {
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.putExtra("user_id", userId);
                intent.putExtra("user_name", userName);
                intent.putExtra("user_role", userRole);
                activity.startActivity(intent);
            }
        });

        //Lịch hẹn
        btnLichhen.setOnClickListener(v -> {
            if (!(activity instanceof AppointmentActivity)) {
                Intent intent = new Intent(activity, AppointmentActivity.class);
                intent.putExtra("user_id", userId);
                intent.putExtra("user_name", userName);
                intent.putExtra("user_role", userRole);
                activity.startActivity(intent);
            }
        });
        //Hỏi đáp
        btnHoidap.setOnClickListener(v -> {
            // TODO:
        });

        btnThongbao.setOnClickListener(v -> {
            // TODO:
        });

        btnTaikhoan.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AccountActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("user_name", userName);
            intent.putExtra("user_role", userRole);
            activity.startActivity(intent);
        });
    }
}