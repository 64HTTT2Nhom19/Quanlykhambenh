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
    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.dbHelper = new DatabaseHelper(context);
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

        holder.btnEditStatus.setOnClickListener(v -> {
            String[] statuses = {"confirmed", "pending", "canceled"};
            Appointment appointment = appointmentList.get(holder.getAdapterPosition());
            int currentIndex = Arrays.asList(statuses).indexOf(appointment.getStatus());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Chọn trạng thái mới");
            builder.setSingleChoiceItems(statuses, currentIndex, (dialog, which) -> {
                String newStatus = statuses[which];
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


    @Override
    public int getItemCount() {
    return appointmentList.size();
}

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvDepartmentName, tvDoctorName, tvClinicName, tvTime, tvStatus;
        Button btnEditStatus;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDepartmentName = itemView.findViewById(R.id.tvDepartmentName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvClinicName = itemView.findViewById(R.id.tvClinicName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEditStatus = itemView.findViewById(R.id.btnEditStatus);

        }
    }
}

