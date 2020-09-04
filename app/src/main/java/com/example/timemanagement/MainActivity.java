package com.example.timemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //Activity Variables
    private RecyclerView taskRecyclerView;
    private RecyclerView.Adapter taskListAdapter;
    private RecyclerView.LayoutManager taskListLayoutManager;
    private Data data;
    private FragmentManager fragmentManager;

    //View Variables
    private ImageButton addTaskButton;
    private TextView startOfDay;
    private TextView endOfDay;

    //Shared Preferences Variables
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SHARED_PREFS_TASKS = "tasks";
    public static final String SHARED_PREFS_STARTOFDAY = "startofday";
    public static final String SHARED_PREFS_ENDOFDAY = "endofday";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        startOfDay = findViewById(R.id.textView_startOfDay);
        endOfDay = findViewById(R.id.textView_endOfDay);
        addTaskButton = findViewById(R.id.imageButtonAddTask);
        taskRecyclerView = findViewById(R.id.taskList);
        fragmentManager = getSupportFragmentManager();

        data = Data.getInstance();
        loadData();
        initializeRecyclerView();
        data.setTaskListAdapter(taskListAdapter);
        initializeAddTaskButton();
        initializeDatepickers();

    }

    private Bundle getTimePickerBundle(TextView time, String STARTorEND) {
        Bundle bundle = new Bundle();
        bundle.putInt("hours", Integer.parseInt(time.getText().subSequence(0, 2).toString()));
        bundle.putInt("minutes", Integer.parseInt(time.getText().subSequence(3, 5).toString()));
        bundle.putString("STARTorEND", STARTorEND);
        return bundle;
    }

    private void initializeRecyclerView() {
        taskRecyclerView.setHasFixedSize(true);
        taskListLayoutManager = new LinearLayoutManager(this);
        taskListAdapter = new TaskListAdapter(data.getTasks(),fragmentManager);
        taskRecyclerView.setLayoutManager(taskListLayoutManager);
        taskRecyclerView.setAdapter(taskListAdapter);
    }

    private void initializeAddTaskButton() {
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTaskFragment newTaskFragment = new NewTaskFragment();
                newTaskFragment.show(fragmentManager, "newTaskFragment");
            }
        });
    }

    private void initializeDatepickers() {
        startOfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setArguments(getTimePickerBundle(startOfDay, "START"));
                timePickerFragment.show(fragmentManager, "timePickerFragment");
            }
        });
        endOfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setArguments(getTimePickerBundle(endOfDay, "END"));
                timePickerFragment.show(fragmentManager, "timePickerFragment");
            }
        });
    }

    /**
     * this Method saves all the data from Data.class to the Shared Preferences,
     * so they are permanently saved on the users phone.
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(data.getTasks());
        editor.putString(SHARED_PREFS_TASKS, json);

        editor.putString(SHARED_PREFS_STARTOFDAY, startOfDay.getText().toString());
        editor.putString(SHARED_PREFS_ENDOFDAY, endOfDay.getText().toString());

        //TODO save current Time

        editor.apply();
    }

    /**
     * this Method loads all the data from Shared Preferences to the Data.class
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String tasksJson = sharedPreferences.getString(SHARED_PREFS_TASKS, null);

        Type tasksType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        HashMap<Integer, Task> taskList = gson.fromJson(tasksJson, tasksType);
        data.setTasks(taskList);
        //TODO: get timestamp
        //TODO: check for running tasks how much time passed since timestamp

        startOfDay.setText(sharedPreferences.getString(SHARED_PREFS_STARTOFDAY, "08:00 AM"));
        endOfDay.setText(sharedPreferences.getString(SHARED_PREFS_ENDOFDAY, "08:00 PM"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }
}
