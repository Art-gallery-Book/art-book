package com.artbook401.artbook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Event;
import com.artbook401.artbook.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{
    //    private final List<TaskItem> taskItems;
    private OnTaskItemClickListener listener;
    private  List<Event> eventList ;

    public EventsAdapter(List<Event> eventList, OnTaskItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    public interface OnTaskItemClickListener {
        void onTaskClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_adapter, parent, false);

        return new ViewHolder(view,listener);

    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {
        Event item = eventList.get(position);
        holder.eventName.setText(item.getName());
        holder.eventDate.setText(item.getDate());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        TextView eventDate;



        public ViewHolder(@NonNull View itemView,OnTaskItemClickListener listener) {
            super(itemView);

            eventName=itemView.findViewById(R.id.eventsAdaptersName);
            eventDate=itemView.findViewById(R.id.eventsAdapterDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTaskClicked(getAdapterPosition());
                }
            });
        }

    }
}

