package com.example.plan_it;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    SwipeMenuListView lvTasks;
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

        // Instantiate swipe menu creator
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu taskMenu)
            {
                // Create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // Set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x9e,
                        0x45)));
                // Set item width
                editItem.setWidth(120);
                // Set an icon
                editItem.setIcon(R.drawable.ic_edit);

                // Add item to the menu
                taskMenu.addMenuItem(editItem);

                // Create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // Set item background color
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0x9c,
                        0x0a, 0x00)));
                // Set item width
                deleteItem.setWidth(130);
                // Set an icon
                deleteItem.setIcon(R.drawable.ic_delete);

                // Add item to the menu
                taskMenu.addMenuItem(deleteItem);
            }
        };

        // Set menu creator
        lvTasks.setMenuCreator(creator);

        lvTasks.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        // Edit
                        break;
                    case 1:
                        // Delete task
                        tasks.remove(position);
                        taskAdapter.notifyDataSetChanged();
                        return false;
                }

                // False : Close the swipe menu; true : Do not close the swipe menu
                return false;
            }
        });
    }

    public void openAdd(View v)
    {
        Intent openWindow = new Intent(this, ClickedAdd.class);
        startActivity(openWindow);
    }
}
