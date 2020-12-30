package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {

    private List<TasksList> tasksList = new ArrayList<>();
    private OnItemClickListener mListener;

    public void setList(List<TasksList> tasksListList) {
        this.tasksList = tasksListList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String id, String title, int size);
    }

    class TaskListViewHolder extends RecyclerView.ViewHolder {

        // Declare your views
        private TextView title;
        private TextView size;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);

            // inflate the view
            title = itemView.findViewById(R.id.li_task_title);
            size = itemView.findViewById(R.id.li_task_size);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(
                            tasksList.get(getAdapterPosition()).getId(),
                            tasksList.get(getAdapterPosition()).getTitle(),
                            tasksList.get(getAdapterPosition()).getSize()
                    );
                }
            });
        }

        private void bind(int position) {
            // Bind data
            title.setText(tasksList.get(position).getTitle());
            size.setText("" + tasksList.get(position).getSize() + " tasks");
        }
    }
}
