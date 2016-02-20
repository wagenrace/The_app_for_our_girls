package asapp.cleanversionsimplesql;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScrollList extends AppCompatActivity {

    SQLiteDatabase contactsDB = null;

    Button addContactButton;

    ListAdapter theAdapter;

    ListView theListView;

    List<String[]> favoriteTVShows2 = new ArrayList<String[]>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption

            //Toast toast = Toast.makeText(getApplicationContext(), "Updating Database", Toast.LENGTH_SHORT);
            //toast.show();
            contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

            // Execute an SQL statement that isn't select
            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " +
                    "(id integer primary key, image BLOB, name VARCHAR, unicorntag_hot INTEGER DEFAULT 0, unicorntag_cool INTEGER DEFAULT 0, unicorntag_chill INTEGER DEFAULT 0);");

            // The database on the file system
            File database = getApplicationContext().getDatabasePath("MyContacts.db");

        }

        catch(Exception e){
            Log.e("CONTACTS ERROR", "Error Creating Database");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);

        theAdapter = new MyAdapter(this,
                favoriteTVShows2);
        theListView = (ListView) findViewById(R.id.ListView_total);

        // Tells the ListView what data to use
        theListView.setAdapter(theAdapter);

        updateListview();
        final Intent addObjectIntent = new Intent(this,addObject.class);

        // ListViews display data in a scrollable list

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addObjectIntent.putExtra("callingActivity", String.valueOf(((Object[]) adapterView.getItemAtPosition(i))[0]));
                startActivity(addObjectIntent);
            }
        });


        addContactButton = (Button) findViewById(R.id.addContactButton);

    }

    public void addContact(View view) {
        Intent addObjectIntent = new Intent(this,addObject.class);
        addObjectIntent.putExtra("callingActivity", "Create");
        startActivity(addObjectIntent);

    }


    @Override
    protected void onResume() {
        updateListview();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        contactsDB.close();

        super.onDestroy();
    }



    public void updateListview(){
        contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);

        // Get the index for the column name provided
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");


        // Move to the first row of results
        cursor.moveToFirst();

        // Verify that we have results
        if(cursor != null && (cursor.getCount() > 0)){
            favoriteTVShows2 = new ArrayList<String[]>();
            do{
                // Get the results and store them in a String
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                favoriteTVShows2.add(new String[] {id, name} );

                // Keep getting results as long as they exist
            }while(cursor.moveToNext());
            //contactListEditText.setText(contactList);

        } else {
            favoriteTVShows2 = new ArrayList<String[]>();
            Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
            //contactListEditText.setText("");

        }
        //*/

        theAdapter = new MyAdapter(this,
                favoriteTVShows2);

        // Tells the ListView what data to use
        theListView.setAdapter(theAdapter);
        contactsDB.close();

    }


}