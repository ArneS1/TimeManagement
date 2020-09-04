package com.example.timemanagement;

import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;


public class Data {

    //Data Variables
    private static Data instance;
    private RecyclerView.Adapter taskListAdapter;

    //Variables to handle
    private HashMap<Integer, Task> tasks;

    private Data() {
        tasks = new HashMap<>();
    }

    public static Data getInstance() {
        if (Data.instance == null) {
            Data.instance = new Data();
        }
        return Data.instance;
    }

    public void updateListData() {
        taskListAdapter.notifyDataSetChanged();
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        updateListData();
    }

    public void updateTask(Task newTask) {
        tasks.remove(newTask.getId());
        addTask(newTask);
    }

    public void deleteTask(int id){
        tasks.remove(id);
        updateListData();
    }

    //getter / setter
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public Task getTask(int taskID) {
        return tasks.get(taskID);
    }

    public int getNextId() {
        int outid = 0;
        while(tasks.containsKey(outid)){
            outid++;
        }
        return outid;
    }

    public void setTaskListAdapter(RecyclerView.Adapter taskListAdapter) {
        this.taskListAdapter = taskListAdapter;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        if (tasks != null) {
            this.tasks = tasks;
            for (Task task : this.tasks.values()) {
                if(task.getTaskStatus() == TaskStatus.RUNNING) task.resume();
            }
        } else {
            System.out.println("DATA: Shared Prefs Tasks are null.");
        }
    }

}
