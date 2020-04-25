package com.example.plan_it;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    ListView lvTasks;
    ImageView addWidget;

    static ArrayAdapter<String> taskAdapter;
    static ArrayList<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String phoneDate;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create calendar instance to obtain date
        Calendar cal = Calendar.getInstance();
        phoneDate = DateFormat.getDateInstance().format(cal.getTime());
        // Reference date into textView
        TextView date = findViewById(R.id.dateView);
        date.setText(phoneDate);

        tasks = new ArrayList<String>();
        taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);

        // OnClickListener for plus button
        addWidget = findViewById(R.id.imageViewAdd);
        addWidget.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAdd(v);
            }
        });

        // Reference to listView
        lvTasks = findViewById(R.id.taskList);
        lvTasks.setAdapter(taskAdapter);
    }

    public void openAdd(View v)
    {
        Intent openWindow = new Intent(this, ClickedAdd.class);
        startActivity(openWindow);
    }
}
