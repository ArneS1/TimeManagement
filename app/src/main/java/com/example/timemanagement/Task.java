package com.example.timemanagement;

import android.os.CountDownTimer;

public class Task {

    //vars
    private int id;
    private String name;
    private long startTimeInMillis;
    private long timeLeftInMillis;
    private TaskStatus taskStatus;
    private CountDownTimer countDownTimer;
    private int MILLIS_PER_MINUTE = 60000;

    //constructor
    public Task(int id, String name, int timeInMinutes) {
        this.id = id;
        this.name = name;
        this.startTimeInMillis = timeInMinutes * MILLIS_PER_MINUTE;
        this.timeLeftInMillis = startTimeInMillis;
        this.taskStatus = TaskStatus.TODO;
    }

    //methods

    /**
     * starts a new Timer with starting Time
     * Ticks every Minute
     * automatically updates Main Activities Task List
     */
    public void start() {
        countDownTimer = createTimer(startTimeInMillis, MILLIS_PER_MINUTE);
        countDownTimer.start();
        taskStatus = TaskStatus.RUNNING;
    }

    /**
     * cancels the Timer, time left is saved
     */
    public void pause() {
        this.taskStatus = TaskStatus.PAUSED;
        countDownTimer.cancel();
    }

    /**
     * creates new Timer with time left
     */
    public void resume() {
        countDownTimer = createTimer(timeLeftInMillis, MILLIS_PER_MINUTE);
        countDownTimer.start();
        taskStatus = TaskStatus.RUNNING;
    }

    public void finish() {
        taskStatus = TaskStatus.DONE;
    }

    /**
     * Creates a Timer
     *
     * @param startingTime time in Milliseconds the timer runs
     * @param tickRate     time between each tick -> list data update
     * @return returns a CountDownTimer Object
     */
    private CountDownTimer createTimer(long startingTime, long tickRate) {
        return new CountDownTimer(startingTime, tickRate) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateListData();
            }

            @Override
            public void onFinish() {
                //TODO alert + ask if its actually DONE
                finish();
            }
        };
    }

    private void updateListData() {
        Data.getInstance().updateListData();
    }


    public void addMinutes(int minutes) {
        pause();
        timeLeftInMillis += minutes * MILLIS_PER_MINUTE;
        resume();
    }

    //getter / setter

    public String getName() {
        return name;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public int getId() {
        return id;
    }

    public int getMinutesLeft() {
        return (int) timeLeftInMillis / MILLIS_PER_MINUTE;
    }

    public int getStartTimeInMinutes() {
        return (int) startTimeInMillis / MILLIS_PER_MINUTE;
    }

    public void setTimeLeftInMinutes(int minutes) {
        timeLeftInMillis = minutes * MILLIS_PER_MINUTE;
    }

}
