package com.example.mechanic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.R;
import com.example.mechanic.model.Complaint;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {


    Context c;
    List<Complaint> x;
    private final int[] mColors = {R.color.list_color_1,R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

    public HistoryAdapter(Context c, List<Complaint> x) {
        this.c = c;
        this.x = x;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_fragment_item, null);
        return new HistoryAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder1, int position) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
        myholder1.cardview.setCardBackgroundColor(bgColor);
        myholder1.pendingComplaintDate.setText(x.get(position).getGeneratedDate());
        myholder1.pendingComplaintDescription.setText(x.get(position).getDescription());
        myholder1.pendingComplaintId.setText(String.valueOf(x.get(position).getComplaintId()));
        myholder1.pendingComplaintMachineId.setText(x.get(position).getMachine().getMachineId());

    }

    @Override
    public int getItemCount() {
        return x.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView pendingComplaintDate, pendingComplaintId, pendingComplaintDescription, pendingComplaintMachineId;
        CardView cardview;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            pendingComplaintDate = itemView.findViewById(R.id.s_history_date);
            pendingComplaintId = itemView.findViewById(R.id.s_history_complaint_id);
            pendingComplaintDescription = itemView.findViewById(R.id.s_history_desc);
            pendingComplaintMachineId = itemView.findViewById(R.id.s_history_machine_id);
            cardview = itemView.findViewById(R.id.cardview_history);

        }

    }
}
