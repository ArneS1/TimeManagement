package com.example.timemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewholder> {
    private HashMap<Integer,Task> taskHashMap;
    private FragmentManager fragmentManager;

    public static class TaskListViewholder extends RecyclerView.ViewHolder {

        public ImageButton imageButton;
        public TextView title;
        public TextView time;



        public TaskListViewholder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.list_item_imageButton);
            title = itemView.findViewById(R.id.list_item_nameText);
            time = itemView.findViewById(R.id.list_item_timeText);
        }
    }

    public TaskListAdapter(HashMap<Integer,Task> taskHashMap, FragmentManager fragmentManager) {
        this.taskHashMap = taskHashMap;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TaskListViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        TaskListViewholder taskListViewholder = new TaskListViewholder(view);
        return taskListViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskListViewholder holder, int position) {

        final Task currentTask = taskHashMap.get(position);
        holder.title.setText(currentTask.getName());
        holder.time.setText(String.valueOf(currentTask.getMinutesLeft()));

        switch (currentTask.getTaskStatus()) {
            case TODO:
            case PAUSED:
                holder.imageButton.setBackgroundResource(R.drawable.start);
                break;

            case RUNNING:
                holder.imageButton.setBackgroundResource(R.drawable.pause);
                break;
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentTask.getTaskStatus()) {
                    case TODO:
                        currentTask.start();
                        holder.imageButton.setBackgroundResource(R.drawable.pause);
                        break;

                    case RUNNING:
                        currentTask.pause();
                        holder.imageButton.setBackgroundResource(R.drawable.start);
                        break;

                    case PAUSED:
                        currentTask.resume();
                        holder.imageButton.setBackgroundResource(R.drawable.pause);
                        break;

                    case DONE:
                        break;
                }
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaskFragment editTaskFragment = new EditTaskFragment();
                Bundle currentTaskData = new Bundle();
                currentTaskData.putInt("taskID",currentTask.getId());
                editTaskFragment.setArguments(currentTaskData);
                editTaskFragment.show(fragmentManager,"edit task fragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskHashMap.size();
    }
}
