package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ToDoViewHolder> {

    private List<Todo> todoList = new ArrayList<>();
    private OnChecked mListener;

    public void setList(List<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToDoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setOnCheckedListener(OnChecked mListener) {
        this.mListener = mListener;
    }

    public interface OnChecked {
        void onChecked(Todo todo);

        void onItemClicked(String id, String title, String description, String date);
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {

        // Declare your views
        private CheckBox checkBox;
        private TextView li_todo_title;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            // inflate the view
            checkBox = itemView.findViewById(R.id.li_todo_check);
            li_todo_title = itemView.findViewById(R.id.li_todo_title);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onChecked(todoList.get(getAdapterPosition()));
                }
            });

            li_todo_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(
                            todoList.get(getAdapterPosition()).getId(),
                            todoList.get(getAdapterPosition()).getTitle(),
                            todoList.get(getAdapterPosition()).getDescription(),
                            todoList.get(getAdapterPosition()).getDate()
                    );
                }
            });

        }

        private void bind(int position) {
            // Bind data
            checkBox.setChecked(todoList.get(position).isChecked());
            li_todo_title.setText(todoList.get(position).getTitle());

        }
    }
}
