package vn.edu.tlu.nguyenthithiem.quanlylichsu;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import vn.edu.tlu.nguyenthithiem.quanlylichsu.database.DatabaseHelper;
import vn.edu.tlu.nguyenthithiem.quanlylichsu.models.Appointment;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<Appointment> appointmentList;
    private DatabaseHelper dbHelper;
    private String userRole;
    public AppointmentAdapter(Context context, List<Appointment> appointmentList, String userRole) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.dbHelper = new DatabaseHelper(context);
        this.userRole = this.userRole;
    }


    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appt = appointmentList.get(position);

        holder.tvUserName.setText("Người khám: " + appt.getUserName());
        holder.tvDepartmentName.setText("Khoa khám bệnh: " + appt.getDepartmentName());
        holder.tvDoctorName.setText("Bác sĩ: " + appt.getDoctorName());
        holder.tvClinicName.setText("Cơ sở: " + appt.getClinicName());
        holder.tvTime.setText("Thời gian: " + appt.getAppointmentTime());
        holder.tvStatus.setText("Trạng thái: " + getVietnameseStatus(appt.getStatus()));

        // Phân quyền hủy lịch
        if (!"admin".equalsIgnoreCase(userRole) && "pending".equalsIgnoreCase(appt.getStatus())) {
            holder.btnCancelAppointment.setVisibility(View.VISIBLE);
        } else {
            holder.btnCancelAppointment.setVisibility(View.GONE);}
        // Xử lý hủy
        holder.btnCancelAppointment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận hủy lịch");
            builder.setMessage("Bạn có chắc muốn hủy lịch hẹn này?");
            builder.setPositiveButton("Hủy lịch", (dialog, which) -> {
                appt.setStatus("canceled");
                dbHelper.updateAppointmentStatus(appt.getId(), "canceled");
                notifyItemChanged(holder.getAdapterPosition());
            });
            builder.setNegativeButton("Không", null);
            builder.show();
            });


        //Phân quyền sửa
        if (!"admin".equalsIgnoreCase(userRole)) {
            holder.btnEditStatus.setVisibility(View.GONE); // Ẩn nếu không phải admin
        } else {
            holder.btnEditStatus.setVisibility(View.VISIBLE);
            holder.btnEditStatus.setOnClickListener(v -> {
            //Tạo mảng trạng thái
            String[] statuses = {"Đã xác nhận", "Đang chờ", "Đã hủy"};
            String[] statusValues = {"confirmed", "pending", "canceled"};
            //Lấy lịch hẹn hiện tại
                Appointment appointment = appointmentList.get(holder.getAdapterPosition());
                //Tìm chỉ số trạng thái hiện tại
                int currentIndex = Arrays.asList(statuses).indexOf(appointment.getStatus());
                //Tạo hộp thoại
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Chọn trạng thái mới");
                builder.setSingleChoiceItems(statuses, currentIndex, (dialog, which) -> {
                    String newStatus = statusValues[which];
                    // Cập nhật nếu trạng thái mới khác trạng thái hiện tại
                    if (!newStatus.equals(appointment.getStatus())) {
                        appointment.setStatus(newStatus);
                        dbHelper.updateAppointmentStatus(appointment.getId(), newStatus);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                    dialog.dismiss();
                });
                builder.setNegativeButton("Hủy", null);
                builder.show();
            });
    }
    }

    //hamf chuyển đổi tiếng việt
    private String getVietnameseStatus(String status) {
        switch (status) {
            case "confirmed": return "Đã xác nhận";
            case "pending": return "Đang chờ";
            case "canceled": return "Đã hủy";
            default: return status;
        }
    }


    @Override
    public int getItemCount() {
    return appointmentList.size();
}

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvDepartmentName, tvDoctorName, tvClinicName, tvTime, tvStatus;
        Button btnEditStatus, btnCancelAppointment;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDepartmentName = itemView.findViewById(R.id.tvDepartmentName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvClinicName = itemView.findViewById(R.id.tvClinicName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEditStatus = itemView.findViewById(R.id.btnEditStatus);
            btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);

        }

    }
}

