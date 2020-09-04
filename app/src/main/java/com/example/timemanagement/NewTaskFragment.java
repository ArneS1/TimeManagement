package com.example.timemanagement;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class NewTaskFragment extends DialogFragment {

    private LinearLayout setDurationScrollLayout;
    private HorizontalScrollView horizontalScrollView;
    private int textViewCount;
    private List<TextView> durations;
    private Button setTaskButton;
    private EditText title;

    private TextView currentDuration;
    private int currentDurationIndex;
    private Data data;

    private Task outTask;
    private int outTaskDuration;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_task_fragment, container, false);
        initializeViews(view);

        data = Data.getInstance();
        durations = new ArrayList<>();
        fillDurationLayout(setDurationScrollLayout);
        TextView t = (TextView) setDurationScrollLayout.getChildAt(4);
        t.setTextColor(getResources().getColor(R.color.colorPickedTimeText));
        outTaskDuration = 5;

        setEventListeners();

        return view;
    }

    private void initializeViews(View view){
        horizontalScrollView = view.findViewById(R.id.horizontalScrollview);
        title = view.findViewById(R.id.editText_taskName);
        setTaskButton = view.findViewById(R.id.setTaskButton);
        setDurationScrollLayout =  view.findViewById(R.id.linearLayout_newTaskLength);
    }

    private void setEventListeners() {

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setTextColor(getResources().getColor(R.color.colorTextBlack));
            }
        });

        setTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().equals("")){
                    outTask = new Task(data.getNextId(),title.getText().toString(),outTaskDuration);
                    data.addTask(outTask);
                    closeFragment();

                } else {
                    title.setHintTextColor(getResources().getColor(R.color.colorWarning));
                    title.setTextColor(getResources().getColor(R.color.colorWarning));
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            horizontalScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    Rect scrollBounds = new Rect();
                    horizontalScrollView.getDrawingRect(scrollBounds);
                    scrollBounds.set(scrollBounds.left+300, scrollBounds.top, scrollBounds.right-300, scrollBounds.bottom);
                    Rect childBounds = new Rect();

                    for (int i = 0; i < setDurationScrollLayout.getChildCount(); i++) {
                        setDurationScrollLayout.getChildAt(i).getHitRect(childBounds);

                        if(scrollBounds.contains(childBounds)) {
                            currentDurationIndex = i;
                            currentDuration = (TextView) setDurationScrollLayout.getChildAt(currentDurationIndex);

                            if(currentDuration.getText()!=""){
                                currentDuration.setTextColor(getResources().getColor(R.color.colorPickedTimeText));
                                outTaskDuration = Integer.decode(currentDuration.getText().toString());

                                TextView prevDuration = (TextView) setDurationScrollLayout.getChildAt(i-2);
                                prevDuration.setTextColor(getResources().getColor(R.color.colorTextBlack));

                                TextView nextDuration = (TextView) setDurationScrollLayout.getChildAt(i+2);
                                nextDuration.setTextColor(getResources().getColor(R.color.colorTextBlack));
                            }
                            return;
                        }
                    }

                }
            });
        } else {
            Toast.makeText(getContext(), "Please Update Your Android Version", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void fillDurationLayout(LinearLayout layout){
        addEmptyTextViews(layout,4);
        textViewCount = 30;
        TextView textView;
        for(int i = 1; i < textViewCount; i++){
            textView = new TextView(getContext());
            textView = createTextview(textView,String.valueOf(i*5));

            layout.addView(textView);
            durations.add(textView);

            addEmptyTextViews(layout,1);
        }
        addEmptyTextViews(layout,3);
    }

    private TextView createTextview(TextView textView, String content){
        textView = new TextView(getContext());
        textView.setTextSize(40);
        textView.setTextColor(getResources().getColor(R.color.colorTextBlack));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setZ(-1);

        if(!content.equals("")){
            textView.setText(content);
        } else {
            textView.setWidth(100);
            textView.setText("");
        }

        return textView;
    }

    private void addEmptyTextViews(LinearLayout layout, int count){
        for(int i=0;i<count;i++) {
            layout.addView(createTextview(new TextView(getContext()), ""));
        }
    }

}
