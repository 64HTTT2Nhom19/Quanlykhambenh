package vn.edu.tlu.nguyenthithiem.quanlylichsu.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.Clinic;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.Doctor;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "clinic_system.db";
    private static final int DB_VERSION = 3;
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "user_id INTEGER PRIMARY KEY, " +
                "name TEXT, email TEXT, gender TEXT, phone TEXT, password TEXT, role TEXT)");

        db.execSQL("CREATE TABLE departments (" +
                "dept_id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE doctors (" +
                "doctor_id INTEGER PRIMARY KEY, " +
                "name TEXT,dept_id INTEGER, bio TEXT, photo_url TEXT,  " +
                "FOREIGN KEY(dept_id) REFERENCES departments(dept_id))");

        db.execSQL("CREATE TABLE clinics (" +
                "clinic_id INTEGER PRIMARY KEY, " +
                "name TEXT, address TEXT, phone TEXT)");

        db.execSQL("CREATE TABLE appointments (" +
                "appointment_id INTEGER PRIMARY KEY, " +
                "user_id INTEGER, doctor_id INTEGER, clinic_id INTEGER, " +
                "created_at TEXT, appointment_time TEXT," +
                "status TEXT NOT NULL DEFAULT 'Chờ xác nhận' CHECK(status IN ('Chờ xác nhận', 'Đã xác nhận', 'Đã hủy'))," +
                "FOREIGN KEY(user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY(doctor_id) REFERENCES doctors(doctor_id), " +
                "FOREIGN KEY(clinic_id) REFERENCES clinics(clinic_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS appointments");
        db.execSQL("DROP TABLE IF EXISTS clinics");
        db.execSQL("DROP TABLE IF EXISTS doctors");
        db.execSQL("DROP TABLE IF EXISTS departments");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void importCSVIfFirstRun() {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Log.d("DB", "Importing CSV data");

            importCSV("users.csv", "users", new String[]{
                    "user_id", "name", "email", "gender", "phone", "password", "role"
            });

            importCSV("departments.csv", "departments", new String[]{
                    "dept_id", "name"
            });

            importCSV("doctors.csv", "doctors", new String[]{
                    "doctor_id", "name", "dept_id", "bio", "photo_url"
            });

            importCSV("clinics.csv", "clinics", new String[]{
                    "clinic_id", "name", "address", "phone"
            });

            importCSV("appointments.csv", "appointments", new String[]{
                    "appointment_id", "user_id", "doctor_id", "clinic_id", "created_at", "appointment_time", "status"
            });

            prefs.edit().putBoolean("isFirstRun", false).apply();
        }
    }

    private void importCSV(String fileName, String tableName, String[] columns) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            db.beginTransaction();
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1);
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
            e.printStackTrace();
        }
    }
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_GENDER = "gender";
    public static final String COL_PHONE = "phone";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    // Thêm người dùng mới
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, user.getName());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_GENDER, user.getGender());
        values.put(COL_PHONE, user.getPhone());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_ROLE, user.getRole());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }


    // Kiểm tra email đã tồn tại
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_EMAIL},
                COL_EMAIL + " = ?", new String[]{email}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    // Kiểm tra đăng nhập
    public User checkUser(String email, String password) {
        Log.d("LOGIN_DEBUG", "Input Email: " + email);           // In email người dùng nhập
        Log.d("LOGIN_DEBUG", "Input Password: " + password);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;
        try {
            String[] columns = {COL_USER_ID, COL_NAME, COL_EMAIL, COL_GENDER, COL_PHONE, COL_PASSWORD, COL_ROLE};
            String selection = COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
            String[] selectionArgs = {email, password};

            cursor = db.query(TABLE_USERS, // Tên bảng
                    columns,                  // Các cột cần trả về
                    selection,                // Các điều kiện WHERE
                    selectionArgs,            // Giá trị cho điều kiện WHERE
                    null,                     // Nhóm hàng
                    null,                     // Lọc nhóm hàng
                    null);                    // Thứ tự sắp xếp
            if (cursor != null && cursor.moveToFirst()) {
                // Tìm thấy người dùng, tạo đối tượng User
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE));
                String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));
                user = new User(userId, name, userEmail, gender, phone, userPassword, role);

            }else {    Log.d("LOGIN_DEBUG", "Không tìm thấy user phù hợp");}
            }finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return user;
    }

    public Cursor getAppointmentsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.appointment_time, a.status, " +
                "u.name AS user_name, d.name AS doctor_name, c.name AS clinic_name, dept.name AS department_name " +
                "FROM appointments a " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN departments dept ON d.dept_id = dept.dept_id " +
                "JOIN clinics c ON a.clinic_id = c.clinic_id " +
                "WHERE u.user_id = ?";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public Cursor getAllAppointmentsWithDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.appointment_time, a.status, " +
                "u.name AS user_name, d.name AS doctor_name, c.name AS clinic_name, dept.name AS department_name " +
                "FROM appointments a " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN departments dept ON d.dept_id = dept.dept_id " +
                "JOIN clinics c ON a.clinic_id = c.clinic_id";
        return db.rawQuery(query, null);
    }

    //cập nhật trạng thái lịch hẹn sau khi sửa
    public void updateAppointmentStatus(int appointmentId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        db.update("appointments", values, "appointment_id = ?", new String[]{String.valueOf(appointmentId)});
        db.close();
    }

    //Lấy thông tin người dùng để hiển thị trong tài khoản
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            user = new User(id, name, email, gender, phone, password, role);
            cursor.close();
        }
        db.close();
        return user;
    }

    //Lấy thông tin bác sĩ
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT doctor_id, name, dept_id, bio, photo_url FROM doctors", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int deptId = cursor.getInt(cursor.getColumnIndexOrThrow("dept_id"));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow("bio"));
                String photoUrl = cursor.getString(cursor.getColumnIndexOrThrow("photo_url"));

                Doctor doctor = new Doctor(id, name, deptId, bio, photoUrl);
                doctors.add(doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return doctors;
    }

    //Lấy danh sách cơ sở
    public List<Clinic> getAllClinics() {
        List<Clinic> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT clinic_id, name FROM clinics", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                list.add(new Clinic(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    //Kiểm tra lịch trống
    public boolean isDoctorAvailable(int doctorId, int clinicId, String appointmentTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM appointments " +
                        "WHERE doctor_id = ? AND clinic_id = ? AND appointment_time = ?",
                new String[]{String.valueOf(doctorId), String.valueOf(clinicId), appointmentTime}
        );

        boolean available = !cursor.moveToFirst(); // true nếu KHÔNG có lịch trùng
        cursor.close();
        return available;
    }


    // Lưu thông tin đặt lịch mới vào bảng appointment
    public boolean insertAppointment(int userId, int doctorId, int clinicId, String appointmentTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Định dạng thời gian
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        values.put("user_id", userId);
        values.put("doctor_id", doctorId);
        values.put("clinic_id", clinicId);
        values.put("created_at", createdAt); //Định dạng yyyy-MM-dd HH:mm
        values.put("appointment_time", appointmentTime);
        values.put("status", "Chờ xác nhận");
        long result = db.insert("appointments", null, values);
        return result != -1;
    }





}