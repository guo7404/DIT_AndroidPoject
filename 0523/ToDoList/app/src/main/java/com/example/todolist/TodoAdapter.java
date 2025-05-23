package com.example.todolist;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<TodoItem> todoList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TodoItem item);
        void onDeleteClick(TodoItem item);
        void onEditClick(TodoItem item);
    }

    public TodoAdapter(List<TodoItem> todoList, OnItemClickListener listener) {
        this.todoList = todoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem item = todoList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxTask;
        private TextView textViewTask;
        private TextView textViewAlarmTime;
        private Button buttonEdit;
        private Button buttonDelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            textViewAlarmTime = itemView.findViewById(R.id.textViewAlarmTime);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(TodoItem item) {
            textViewTask.setText(item.getTask());
            checkBoxTask.setChecked(item.isCompleted());

            // 완료된 항목 스타일 변경
            if (item.isCompleted()) {
                textViewTask.setPaintFlags(textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewTask.setAlpha(0.5f);
            } else {
                textViewTask.setPaintFlags(textViewTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                textViewTask.setAlpha(1.0f);
            }

            // 알람 시간 표시
            if (item.getAlarmTime() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
                String alarmTimeStr = "알람: " + sdf.format(new Date(item.getAlarmTime()));
                textViewAlarmTime.setText(alarmTimeStr);
                textViewAlarmTime.setVisibility(View.VISIBLE);
            } else {
                textViewAlarmTime.setVisibility(View.GONE);
            }

            // 클릭 리스너 설정
            checkBoxTask.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });

            buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(item);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(item);
                }
            });
        }
    }
}