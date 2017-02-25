package ram.android.myhabittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ram.android.myhabittracker.data.HabitContract;
import ram.android.myhabittracker.data.HabitTrackerDBHelper;

import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_DURATION;
import static ram.android.myhabittracker.data.HabitContract.HabitEntry.COLUMN_HABIT_NAME;

public class MainActivity extends AppCompatActivity {

    HabitTrackerDBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new HabitTrackerDBHelper(this);
        Button button = (Button) findViewById(R.id.insertbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           EditText samplePersonEditTextView = (EditText) findViewById(R.id.habitEditText);
                String habit = samplePersonEditTextView.getText().toString();
                EditText samplePersonEditTextView1 = (EditText) findViewById(R.id.durationEditText);

               Integer duration= Integer.parseInt(samplePersonEditTextView1.getText().toString());
                insertHabit(habit,duration);
              //  readHabits(habit);

            }
        });

        Button button1 = (Button) findViewById(R.id.getbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText samplePersonEditTextView = (EditText) findViewById(R.id.habitEditText);
                String habit = samplePersonEditTextView.getText().toString();
                readHabits(habit);

            }
        });
        Button button2 = (Button) findViewById(R.id.deletebutton);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               deleteEntries();

            }
        });

    }

    //Insert method for making an entry to the habit tracker database
    public void insertHabit(String name, int duration) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_NAME, name);
        values.put(COLUMN_DURATION, duration);

        db.insert(HabitContract.HabitEntry.TABLE_NAME, "null", values);
    }

    //read method that reads habit_name and duration of entries with habit name as gaming
    public void readHabits(String habitName) {
        db = dbHelper.getReadableDatabase();
        String whereClause = COLUMN_HABIT_NAME + " = ?";
        String[] selectionArgs = {habitName};
        String result = "";
        StringBuilder sb = new StringBuilder();
        String[] projection = {
                COLUMN_HABIT_NAME,
                COLUMN_DURATION};
        Cursor c = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                whereClause,
                selectionArgs,
                null,
                null,
                null);
        c.moveToFirst();
        if (c != null) {
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    result = sb.append(" " + c.getString(i)).toString();
                }
            } while (c.moveToNext());

            Log.v("Result of query ", result);
            TextView scoreView = (TextView) findViewById(R.id.result1);
            scoreView.setText(String.valueOf(result));

        }
        c.close();
    }

    //deletes all the entries from the table
    public void deleteEntries() {
        db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);
    }
}
