package asapp.cleanversionsimplesql;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom Nijhof on 3-2-2016.
 * EditText name;
 */

public class addObject extends Activity {
    EditText name;
    ImageView imageViewPhoto;
    Bundle bundle = new Bundle();
    Bitmap BMP;
    String[] theTagsTest = new String[0];
    ListView tagList;
    ArrayAdapter theAdapter;
    public String activeID;
    public String upString;
    public SQLiteDatabase contactsDB;
    List<String[]> favoriteTVShows2 = new ArrayList<String[]>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.adding_object2);

        //////SQL data base/////
        contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
        Intent activityThatCalled = getIntent();
        upString = activityThatCalled.getExtras().getString("callingActivity");
        if(upString.equals("Create")){
            contactsDB.execSQL("INSERT INTO contacts (name) VALUES ('');");
            Cursor c = (Cursor) contactsDB.rawQuery("SELECT id FROM contacts", null);
            if(c.moveToLast()) {
                activeID = c.getString(0);
            }
            c.close();
        }else{
            activeID = upString;
        }

        //////Tag Adapter//////
        tagList = (ListView) findViewById(R.id.tagListView);
        Cursor c = contactsDB.rawQuery("SELECT * FROM contacts WHERE id = 0",null);
        theTagsTest = c.getColumnNames();
        for(int i = 0; i<theTagsTest.length; i++){
            if(theTagsTest[i].indexOf("unicorntag_") == 0) {
                //Toast.makeText(getApplicationContext(), theTagsTest[i],Toast.LENGTH_SHORT).show();
                Cursor cursor = contactsDB.rawQuery("SELECT " + theTagsTest[i] + " FROM contacts WHERE id = " + activeID, null);
                String status;
                if(cursor.moveToFirst()) {
                    status = cursor.getString(0);
                }else{
                    status = "0";
                }
                favoriteTVShows2.add(new String[] {status, theTagsTest[i].substring(11)} );
            }
        }

        theAdapter = null;
        theAdapter = new TagAdapter(this, favoriteTVShows2);
        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout listFooterView = (LinearLayout)inflater.inflate(
                R.layout.add_object_footer, null);

        tagList.addFooterView(listFooterView);

        LinearLayout listHeaderView = (LinearLayout)inflater.inflate(
                R.layout.add_object_header, null);

        tagList.addHeaderView(listHeaderView);

        tagList.setAdapter(theAdapter);



        //////Adding things//////
        name = (EditText) findViewById(R.id.Object_name_ET);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        c = contactsDB.rawQuery("SELECT name FROM contacts WHERE id = " + activeID, null);
        if(c.moveToFirst()) {
            name.setText(c.getString(0));
        }
        //ToDo update Pictures
        if (savedInstanceState != null) {
            BMP = savedInstanceState.getParcelable("bitmap");
            imageViewPhoto.setImageBitmap(BMP);
        }

        //////Tag List//////
        tagList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);
                //Toast.makeText(getApplicationContext(), favoriteTVShows2.get(i-1)[0],Toast.LENGTH_SHORT).show();
                String fav1 = favoriteTVShows2.get(i - 1)[0];
                String fav2 = favoriteTVShows2.get(i - 1)[1];
                if(fav1.equals("1")){
                    favoriteTVShows2.set(i-1, new String[] {"0",  fav2});
                }else{
                    favoriteTVShows2.set(i-1, new String[] {"1",  fav2});
                }
                updateTags();
            }

        });


    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putParcelable("bitmap", BMP);
    }
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if(requestcode == REQUEST_IMAGE_CAPTURE && resultcode == RESULT_OK){
            bundle = data.getExtras();
            BMP = (Bitmap) bundle.get("data");
            imageViewPhoto.setImageBitmap(BMP);
        }
    }

    public void cancel_button(View view) {
        if(upString.equals("Create")) {
            contactsDB.execSQL("DELETE FROM contacts WHERE id =  ('" + activeID + "');");
            contactsDB.close();
            finish();
        }else {
            contactsDB.close();
            finish();
        }
    }

    public void send_button(View view) {
        // Execute an SQL statement that isn't select
        //ToDo store image too
        //name.getText().toString()
        String query = "UPDATE contacts SET name = '" +  name.getText().toString()+ "'";
        for(int i = 0; i < favoriteTVShows2.size(); i++){
            query += ", unicorntag_" + favoriteTVShows2.get(i)[1] + " = '" + favoriteTVShows2.get(i)[0] + "'";
        }
        query += " WHERE id ='"+ activeID+"';";
        //Toast.makeText(addObject.this, query, Toast.LENGTH_LONG).show();
        contactsDB.execSQL(query);
        contactsDB.close();
        finish();
    }


    public void takePic_button(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void delete_button(View view) {
        contactsDB.execSQL("DELETE FROM contacts WHERE id =  '" + activeID + "';");
        contactsDB.close();
        finish();
    }

    public void updateTags(){
        theAdapter = new TagAdapter(this, favoriteTVShows2);
        tagList.setAdapter(theAdapter);
    }

    public void addTag(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add tag");
        alert.setMessage("give the name of the tag you want to introduce. For all other object this tag will be atomaticly turned off.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                contactsDB.execSQL("ALTER TABLE contacts ADD COLUMN unicorntag_" + String.valueOf(input.getText()).replace(' ','_') +" INTEGER DEFAULT 0");
                favoriteTVShows2.add(new String[]{"0", String.valueOf(input.getText()).replace(' ', '_')});
                //Toast.makeText(addObject.this, String.valueOf(favoriteTVShows2.get(0)[1]), Toast.LENGTH_LONG).show();
                contactsDB.close();
                contactsDB = addObject.this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
                updateTags();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void deleteTag(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Tag");
        alert.setMessage("WARNING: This tag will be removed for all objects.");

        // Set an EditText view to get user input
        final Spinner input = new Spinner(this);
        alert.setView(input);
        // Create an ArrayAdapter using the string array and a default spinner layout
        Cursor c = contactsDB.rawQuery("SELECT * FROM contacts WHERE id = 0", null);
        ArrayList<String> spinnerFill = new ArrayList<String>();
        String[] theTagsTest = c.getColumnNames();
        for(int i = 0; i<theTagsTest.length; i++){
            if(theTagsTest[i].indexOf("unicorntag_") == 0) {
                spinnerFill.add(theTagsTest[i].substring(11));
            }
        }
        c.close();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, spinnerFill);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        input.setAdapter(adapter);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputStr = "unicorntag_"+input.getSelectedItem().toString();
                //create table new_table as select column1,column2,....from old_table;
                Cursor c = contactsDB.rawQuery("SELECT * FROM contacts WHERE id = 0", null);
                String[] theTagsTest = c.getColumnNames();
                c.close();
                String query = "CREATE TABLE contacts2 AS SELECT ";
                boolean firstTime = true;
                for(int i =0; i < theTagsTest.length; i++){
                    if(theTagsTest[i].equals(inputStr)){

                    }else{
                        if(firstTime){
                            query += theTagsTest[i];
                            firstTime = false;
                        }else{
                            query += " , " + theTagsTest[i];
                        }

                    }
                }
                query += " FROM contacts";
                //contactsDB.beginTransaction();
                contactsDB.execSQL(query);
                contactsDB.execSQL("DROP TABLE IF EXISTS 'contacts';");
                contactsDB.execSQL("ALTER TABLE 'contacts2' RENAME TO 'contacts';");
                //contactsDB.close();
                //contactsDB = addObject.this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

                for(int i = 0; i < favoriteTVShows2.size(); i++){
                    if(favoriteTVShows2.get(i)[1].equals(input.getSelectedItem().toString())){
                        favoriteTVShows2.remove(i);

                    }
                }
                updateTags();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }
}
