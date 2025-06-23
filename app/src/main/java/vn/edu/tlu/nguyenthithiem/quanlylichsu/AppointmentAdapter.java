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
    public AppointmentAdapter(Context context, List<Appointment> appointmentList, DatabaseHelper dbHelper, String userRole) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.dbHelper = dbHelper;
        this.userRole = userRole;
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
        holder.tvStatus.setText("Trạng thái: " + appt.getStatus());

        // Phân quyền hủy lịch
        if (!"admin".equalsIgnoreCase(userRole) && "Chờ xác nhận".equalsIgnoreCase(appt.getStatus())) {
            holder.btnCancelAppointment.setVisibility(View.VISIBLE);
        } else {
            holder.btnCancelAppointment.setVisibility(View.GONE);}
        // Xử lý hủy
        holder.btnCancelAppointment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận hủy lịch");
            builder.setMessage("Bạn có chắc muốn hủy lịch hẹn này?");
            builder.setPositiveButton("Hủy lịch", (dialog, which) -> {
                appt.setStatus("Đã hủy");
                dbHelper.updateAppointmentStatus(appt.getId(), "Đã hủy");
                notifyItemChanged(holder.getAdapterPosition());
            });
            builder.setNegativeButton("Không", null);
            builder.show();
            });


        //Phân quyền xác nhận
        if ("admin".equalsIgnoreCase(userRole) && "Chờ xác nhận".equalsIgnoreCase(appt.getStatus())) {
            holder.btnConfirmAppointment.setVisibility(View.VISIBLE);
        } else {
            holder.btnConfirmAppointment.setVisibility(View.GONE);}
        holder.btnConfirmAppointment.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận lịch hẹn")
                    .setMessage("Bạn có chắc muốn xác nhận lịch hẹn này?")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        appt.setStatus("Đã xác nhận");
                        dbHelper.updateAppointmentStatus(appt.getId(), "Đã xác nhận");
                        notifyItemChanged(holder.getAdapterPosition());
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

    }
    @Override
    public int getItemCount() {
    return appointmentList.size();
}

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvDepartmentName, tvDoctorName, tvClinicName, tvTime, tvStatus;
        Button btnCancelAppointment, btnConfirmAppointment;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDepartmentName = itemView.findViewById(R.id.tvDepartmentName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvClinicName = itemView.findViewById(R.id.tvClinicName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
            btnConfirmAppointment = itemView.findViewById(R.id.btnConfirmAppointment);


        }

    }
}

