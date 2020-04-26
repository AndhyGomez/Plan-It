/**
 * Author: Andhy Gomez
 *
 * Date Last Modified: 4/26/2020
 *
 * Description: Source code for my first android app, Plan-It. Plan-It is a simple to-do-list
 * app with most basic functionalities one has come to expect from an app of this kind.
 */

package com.example.plan_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

                // Save the data
                saveData();

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

    /**
     * Description: This method closes the pop up window
     *
     * @param v View to be closed
     */
    public void closeWindow(View v)
    {
        finish();
    }

    /**
     * Description: This method saves data to the device's shared preferences folder
     */
    private void saveData()
    {
        SharedPreferences appList = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = appList.edit();
        Gson gson = new Gson();
        String item = gson.toJson(tasks);
        editor.putString("tasks", item);
        editor.apply();
    }
}
