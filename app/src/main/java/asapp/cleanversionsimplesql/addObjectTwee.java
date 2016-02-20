package asapp.cleanversionsimplesql;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom Nijhof on 3-2-2016.
 * EditText name;
 */

public class addObjectTwee extends Activity {
    EditText name;
    EditText theme;
    ImageView imageViewPhoto;
    Bundle bundle = new Bundle();
    Bitmap BMP;
    ArrayAdapter tagAdapter;
    ListView tagSpinner;
    List<String[]> theTags = new ArrayList<String[]>();
    String[] theTagsTest = new String[0];
    public String activeID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.adding_object2);
        /*
        SQLiteDatabase contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
        tagAdapter = new TagAdapter(this, theTags);
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, theTagsTest);
        tagSpinner = (ListView) findViewById(R.id.tag_scroll);
        tagSpinner.setAdapter(tagAdapter);
        Intent activityThatCalled = getIntent();
        activeID = activityThatCalled.getExtras().getString("callingActivity");
        if(activeID == "Create"){
            contactsDB.execSQL("INSERT INTO contacts (name) VALUES ('-_-_-_-');");
            Cursor c = contactsDB.rawQuery("SELECT FROM contacts id WHERE name = '-_-_-_-';", null);
            activeID = c.getString(0);
            Toast.makeText(this, activeID + " is Opened", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, activeID + " is Opened", Toast.LENGTH_LONG).show();
        }
        */
        name = (EditText) findViewById(R.id.Object_name_ET);
        //theme = (EditText) findViewById(R.id.Object_thema_ET);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        //ToDo update editText and Pictures
        if (savedInstanceState != null) {
            BMP = savedInstanceState.getParcelable("bitmap");
            imageViewPhoto.setImageBitmap(BMP);
        }

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
        if(activeID == "Create") {
            //ToDo delete last added element
            finish();
        }else {
            finish();
        }
    }

    public void send_button(View view) {
        SQLiteDatabase contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
        // Execute an SQL statement that isn't select
        //ToDo make alternivive for Update
        //ToDo store image too
        contactsDB.execSQL("INSERT INTO contacts (name, email) VALUES ('" + name.getText().toString() + "', '" + theme.getText().toString() + "');");
        contactsDB.close();
        finish();
    }


    public void takePic_button(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
