/**
 * Author: Andhy Gomez
 *
 * Date Last Modified: 4/26/2020
 *
 * Description: Source code for my first android app, Plan-It. Plan-It is a simple to-do-list
 * app with most basic functionalities one has come to expect from an app of this kind.
 */

package com.example.plan_it;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    /**
     * Class objects
     */
    SwipeMenuListView lvTasks;
    Button addWidget;
    Button clearWidget;
    EditText taskEdited;

    /**
     * Class variables
     */
    private String phoneDate;
    private String editedInput;

    private final String PREFS = "shared preferences";
    private final String TASKS = "tasks";

    /**
     * Global class array list and adapter
     */
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

        try
        {
            loadData();
        }
        catch (Resources.NotFoundException nfe)
        {
            tasks = new ArrayList<>();
        }

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

                            // Save the data
                            saveData();
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

                                if(editedInput.isEmpty())
                                {
                                    Toast.makeText(MainActivity.this, "Cannot have blank task.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    // Modify arraylist
                                    tasks.set(position, editedInput);
                                    taskAdapter.notifyDataSetChanged();

                                    // Save the data
                                    saveData();

                                    // Alert that change has been made
                                    Toast.makeText(MainActivity.this, "Task has been updated.", Toast.LENGTH_LONG).show();
                                }
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

                                // Save the data
                                saveData();


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

    /**
     * Description: This method starts the activity of the ClickedAdd class
     *
     * @param v View to be passed
     */
    private void openAdd(View v)
    {
        Intent openWindow = new Intent(this, ClickedAdd.class);
        startActivity(openWindow);
    }

    /**
     * Description: This method serves to clear an array list recursively
     */
    private void clearList()
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

            // Save the data
            saveData();

            // Recursive call
            clearList();
        }
    }

    /**
     * Description: This method saves data to the device's shared preferences folder
     */
    private void saveData()
    {
        SharedPreferences appList = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = appList.edit();
        Gson gson = new Gson();
        String item = gson.toJson(tasks);
        editor.putString(TASKS, item);
        editor.apply();
    }

    /**
     * Description: This method loads date from the device's shared preferences folder
     */
    private void loadData()
    {
        SharedPreferences appList = getSharedPreferences(PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String item = appList.getString(TASKS, null);
        Type type = new TypeToken<ArrayList>() {}.getType();
        tasks = gson.fromJson(item, type);

        if(tasks == null)
        {
            throw new Resources.NotFoundException();
        }
    }


}
