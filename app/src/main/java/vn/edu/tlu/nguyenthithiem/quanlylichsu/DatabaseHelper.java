package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "clinic_system.db";
    private static final int DB_VERSION = 1;
    private final Context context;

    //Khởi tạo database
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // USERS
        db.execSQL("CREATE TABLE users (" +
                "user_id INTEGER PRIMARY KEY, " +
                "name TEXT, email TEXT, gender TEXT, phone TEXT, password TEXT, role TEXT)");

        // DEPARTMENTS
        db.execSQL("CREATE TABLE departments (" +
                "dept_id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL)");
        // DOCTORS
        db.execSQL("CREATE TABLE doctors (" +
                "doctor_id INTEGER PRIMARY KEY, " +
                "name TEXT, specialty TEXT, bio TEXT, photo_url TEXT, dept_id INTEGER NOT NULL, FOREIGN KEY(dept_id) REFERENCES departments(dept_id))");

        // CLINICS
        db.execSQL("CREATE TABLE clinics (" +
                "clinic_id INTEGER PRIMARY KEY, " +
                "name TEXT, address TEXT, phone TEXT)");

        // APPOINTMENTS
        db.execSQL("CREATE TABLE appointments (" +
                "appointment_id INTEGER PRIMARY KEY, " +
                "user_id INTEGER, doctor_id INTEGER, clinic_id INTEGER, " +
                "created_at TEXT, appointment_time TEXT, status TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS doctors");
        db.execSQL("DROP TABLE IF EXISTS clinics");
        db.execSQL("DROP TABLE IF EXISTS appointments");
        onCreate(db);
    } //kiểm tra bảng đã tồn tại chưa mỗi lần chạy app, dùng khi nâng version DB

    // Chỉ chạy khi lần đầu mở app
    void importCSVIfFirstRun() {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE); //SharedPreferences để ghi nhỡ đã import csdl chưa
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            importCSV("users.csv", "users", new String[]{
                    "user_id", "name", "email", "gender", "phone", "password", "role"
            });

            importCSV("doctors.csv", "doctors", new String[]{
                    "doctor_id", "name", "specialty", "bio", "photo_url"
            });

            importCSV("clinics.csv", "clinics", new String[]{
                    "clinic_id", "name", "address", "phone"
            });

            importCSV("appointments.csv", "appointments", new String[]{
                    "appointment_id", "user_id", "doctor_id", "clinic_id", "created_at", "appointment_time", "status"
            });

            prefs.edit().putBoolean("isFirstRun", false).apply(); // sau khi đọc file thig set isFirstRun = false để không chạy lại các lần sau
        }
    }

    // Import file CSV theo bảng & cột
    private void importCSV(String fileName, String tableName, String[] columns) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            InputStream is = context.getAssets().open(fileName); // mở file trong assets
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            db.beginTransaction();
            reader.readLine(); // bỏ qua dòng tiêu đề

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1); // mỗi don được split thành mảng tokens[], rồi chèn vào bảng dưới dạng ContentValues
                if (tokens.length >= columns.length) {
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < columns.length; i++) {
                        values.put(columns[i], tokens[i].trim());
                    }
                    db.insert(tableName, null, values);
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); // chỉ cần bắt một lần duy nhất
        }
    }


    // Hàm truy vấn lịch hẹn, trả về con trỏ cursor
    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM appointments", null);
    }
    public Cursor getAllAppointmentsWithDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.appointment_time, a.status, " +
                "u.name AS user_name, d.name AS doctor_name, c.name AS clinic_name " +
                "FROM appointments a " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN clinics c ON a.clinic_id = c.clinic_id";
        return db.rawQuery(query, null);
    }

}
