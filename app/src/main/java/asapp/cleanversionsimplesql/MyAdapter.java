package asapp.cleanversionsimplesql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
// We can create custom adapters
class MyAdapter extends ArrayAdapter<String[]> {

    public MyAdapter(Context context, List<String[]> values){

        super(context, R.layout.dubbel_string_layout, values);

    }
    public String id = "";

    // Override getView which is responsible for creating the rows for our list
    // position represents the index we are in for the array.

    // convertView is a reference to the previous view that is available for reuse. As
    // the user scrolls the information is populated as needed to conserve memory.

    // A ViewGroup are invisible containers that hold a bunch of views and
    // define their layout properties.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The LayoutInflator puts a layout into the right View
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        // inflate takes the resource to load, the parent that the resource may be
        // loaded into and true or false if we are loading into a parent view.
        View theView = theInflater.inflate(R.layout.dubbel_string_layout, parent, false);

        // We retrieve the text from the array
        String[] tvShow = getItem(position);

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.textView1);
        id = tvShow[0];
        // Put the next TV Show into the TextView
        theTextView.setText(tvShow[0]);
        // Get the TextView we want to edit
        TextView theTextView2 = (TextView) theView.findViewById(R.id.textView2);

        // Put the next TV Show into the TextView
        theTextView2.setText(tvShow[1]);

        return theView;

    }

    public String getId(){
        return id;
    }

}