package com.udl.bss.barbershopschedule.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udl.bss.barbershopschedule.R;
import com.udl.bss.barbershopschedule.database.BLL;
import com.udl.bss.barbershopschedule.domain.Appointment;
import com.udl.bss.barbershopschedule.domain.Barber;
import com.udl.bss.barbershopschedule.domain.BarberService;
import com.udl.bss.barbershopschedule.listeners.OnItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<Appointment> mDataset;
    private OnItemClickListener listener;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView date;
        TextView service;
        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.name_cv);
            date = itemView.findViewById(R.id.date_cv);
            service = itemView.findViewById(R.id.service_cv);
        }
    }

    public AppointmentAdapter(List<Appointment> myDataset, OnItemClickListener listener, Context context) {
        mDataset = myDataset;
        this.listener = listener;
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointments_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BLL instace = new BLL(this.context);
        BarberService service = instace.Get_BarberShopService(mDataset.get(position).getService_id());
        Barber barber = instace.Get_BarberShop(mDataset.get(position).getBarber_shop_id());

        holder.service.setText(service.Get_Name());
        holder.name.setText(barber.getName());

        Date date_obj = null;

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");
        try {
            date_obj = format.parse(mDataset.get(position).getDate());
        } catch (ParseException e) {
            date_obj = null;
        }

        String date = new SimpleDateFormat("HH:mm dd-MM-yyyy", new Locale("es", "ES")).format(date_obj);
        holder.date.setText(date);

        ViewCompat.setTransitionName(holder.service, String.valueOf(position)+"_serv");
        ViewCompat.setTransitionName(holder.name, String.valueOf(position)+"_name");
        ViewCompat.setTransitionName(holder.date, String.valueOf(position)+"_date");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Appointment getItem (int position) {
        return mDataset.get(position);
    }

    public void removeAll(){
        Iterator<Appointment> iter = mDataset.iterator();
        while(iter.hasNext()){
            Appointment app = iter.next();
            int position = mDataset.indexOf(app);
            iter.remove();
            notifyItemRemoved(position);
        }
    }

    public int add(Appointment app){
        mDataset.add(app);
        notifyItemInserted(mDataset.size()-1);
        return mDataset.size()-1;
    }
}
