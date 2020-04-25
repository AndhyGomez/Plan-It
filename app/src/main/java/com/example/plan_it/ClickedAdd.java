package com.example.plan_it;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static com.example.plan_it.MainActivity.taskAdapter;
import static com.example.plan_it.MainActivity.tasks;

public class ClickedAdd extends AppCompatActivity
{
    ImageView closePopUp;
    Button add;
    EditText taskInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_pop_up);

        taskInput = findViewById(R.id.taskInput);

        // OnClickListener to add task
        add = findViewById(R.id.submitTask);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String userInput;

                // Store input as string
                userInput = taskInput.getText().toString();

                // Add the users input into tasks array list
                tasks.add(userInput);
                taskAdapter.notifyDataSetChanged();

                // Close window when done
                closeWindow(v);
            }
        });

        // OnClickListener for x button
        closePopUp = findViewById(R.id.dismissPop);
        closePopUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                closeWindow(v);
            }
        });


    }

    public void closeWindow(View v)
    {
        finish();
    }
}
