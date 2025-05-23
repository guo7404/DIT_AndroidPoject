package com.example.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd, buttonSelectDateTime;
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private List<TodoItem> todoList;
    private DatabaseHelper databaseHelper;

    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadTodos();

        selectedDateTime = Calendar.getInstance();
    }

    private void initViews() {
        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSelectDateTime = findViewById(R.id.buttonSelectDateTime);
        recyclerView = findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(this);
        todoList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new TodoAdapter(todoList, new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TodoItem item) {
                toggleTaskComplete(item);
            }

            @Override
            public void onDeleteClick(TodoItem item) {
                deleteTask(item);
            }

            @Override
            public void onEditClick(TodoItem item) {
                editTask(item);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        buttonAdd.setOnClickListener(v -> addTask());
        buttonSelectDateTime.setOnClickListener(v -> showDateTimePicker());
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDateTime.set(Calendar.MINUTE, minute);
                                selectedDateTime.set(Calendar.SECOND, 0);

                                String dateTimeText = String.format("알람: %d/%d/%d %02d:%02d",
                                        year, month + 1, dayOfMonth, hourOfDay, minute);
                                buttonSelectDateTime.setText(dateTimeText);
                            },
                            selectedDateTime.get(Calendar.HOUR_OF_DAY),
                            selectedDateTime.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void addTask() {
        String taskText = editTextTask.getText().toString().trim();
        if (taskText.isEmpty()) {
            Toast.makeText(this, "할 일을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoItem newItem = new TodoItem();
        newItem.setTask(taskText);
        newItem.setAlarmTime(selectedDateTime.getTimeInMillis());
        newItem.setCompleted(false);

        long id = databaseHelper.insertTodo(newItem);
        newItem.setId((int) id);

        // 알람 설정
        setAlarm(newItem);

        todoList.add(newItem);
        adapter.notifyItemInserted(todoList.size() - 1);

        editTextTask.setText("");
        buttonSelectDateTime.setText("알람 시간 선택");
        selectedDateTime = Calendar.getInstance();

        Toast.makeText(this, "할 일이 추가되었습니다", Toast.LENGTH_SHORT).show();
    }

    private void toggleTaskComplete(TodoItem item) {
        item.setCompleted(!item.isCompleted());
        databaseHelper.updateTodo(item);
        adapter.notifyDataSetChanged();
    }

    private void deleteTask(TodoItem item) {
        new AlertDialog.Builder(this)
                .setTitle("삭제 확인")
                .setMessage("이 할 일을 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> {
                    databaseHelper.deleteTodo(item.getId());
                    cancelAlarm(item);
                    todoList.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void editTask(TodoItem item) {
        EditText editText = new EditText(this);
        editText.setText(item.getTask());

        new AlertDialog.Builder(this)
                .setTitle("할 일 수정")
                .setView(editText)
                .setPositiveButton("수정", (dialog, which) -> {
                    String newTask = editText.getText().toString().trim();
                    if (!newTask.isEmpty()) {
                        item.setTask(newTask);
                        databaseHelper.updateTodo(item);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void setAlarm(TodoItem item) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("task", item.getTask());
        intent.putExtra("id", item.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, item.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, item.getAlarmTime(), pendingIntent);
    }

    private void cancelAlarm(TodoItem item) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, item.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }

    private void loadTodos() {
        todoList.clear();
        todoList.addAll(databaseHelper.getAllTodos());
        adapter.notifyDataSetChanged();
    }
}