package com.example.plan_it;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Button addWidget;
    Button clearWidget;
    EditText taskEdited;

    private String phoneDate;
    private String editedInput;

    static ArrayAdapter<String> taskAdapter;
    static ArrayList<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create calendar instance to obtain date
        Calendar cal = Calendar.getInstance();
        phoneDate = DateFormat.getDateInstance().format(cal.getTime());
        // Reference date into textView
        TextView date = findViewById(R.id.dateView);
        date.setText(phoneDate);

        tasks = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);

        // OnClickListener for plus button
        addWidget = findViewById(R.id.buttonAddTask);
        addWidget.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAdd(v);
            }
        });

        // OnClickListener for clear all button
        clearWidget = findViewById(R.id.buttonClear);
        clearWidget.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(tasks.size() == 0)
                {
                    Toast.makeText(MainActivity.this, "Your list is empty.", Toast.LENGTH_LONG).show();
                }
                else {
                    // Create dialog pop up for clear all button
                    final AlertDialog.Builder clearAllWarning = new AlertDialog.Builder(MainActivity.this);
                    clearAllWarning.setTitle("Warning: This will clear ALL tasks");

                    clearAllWarning.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Clear the list of tasks
                            clearList();
                            taskAdapter.notifyDataSetChanged();
                        }
                    });

                    // Set cancel button
                    clearAllWarning.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    clearAllWarning.show();
                }
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
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        // Edit
                        // Create new Alert Dialog on this instance
                        final AlertDialog.Builder editDialog = new AlertDialog.Builder(MainActivity.this);
                        editDialog.setTitle("Edit your task below");

                        // Initialize editText
                        taskEdited = new EditText(MainActivity.this);
                        taskEdited.setMaxLines(1);

                        // Set the View
                        editDialog.setView(taskEdited);

                        // Set confirm button
                        editDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // Store input
                                editedInput = taskEdited.getText().toString();

                                // Modify arraylist
                                tasks.set(position, editedInput);
                                taskAdapter.notifyDataSetChanged();

                                // Alert that change has been made
                                Toast.makeText(MainActivity.this, "Task has been updated.", Toast.LENGTH_LONG).show();
                            }
                        });

                        // Set cancel button
                        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                        // Set the dialog window to be visible
                        editDialog.show();
                        break;
                    case 1:
                        // Delete task
                        // Create new Alert Dialog on this instance
                        final AlertDialog.Builder deleteWarning = new AlertDialog.Builder(MainActivity.this);
                        deleteWarning.setTitle("Would like to delete this task?");

                        deleteWarning.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                tasks.remove(position);
                                taskAdapter.notifyDataSetChanged();

                                // Alert that change has been made
                                Toast.makeText(MainActivity.this, "Task has been deleted.", Toast.LENGTH_LONG).show();
                            }
                        });

                        deleteWarning.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                        // Set the dialog window to be visible
                        deleteWarning.show();

                        break;
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

    public void clearList()
    {
        int indexToRemove;

        // Completion Case
        if(tasks.size() == 0)
        {
            // Alert that change has been made
            Toast.makeText(MainActivity.this, "List has been cleared.", Toast.LENGTH_LONG).show();
        }
        else // What is done if the list is not yet empty
        {
            indexToRemove = (tasks.size() - 1);
            tasks.remove(indexToRemove);

            // Recursive call
            clearList();
        }
    }
}
