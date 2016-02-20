package asapp.cleanversionsimplesql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

class TagAdapter extends ArrayAdapter<String[]> {


    public TagAdapter(Context context,
                      List<String[]> values) {
        super(context, R.layout.tag_adapter_layout, values);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.tag_adapter_layout, parent, false);
        CheckBox cb = (CheckBox) theView.findViewById(R.id.checkBox1);



        /*for(int i = 0; i < mItemsChecked.size(); i++) {
            if(mItemsChecked.get(i) == position) {

                //CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkBox1); // Instead of checkBox1, write your name of CheckBox
                //cb.setChecked(true);
            }
        }*/

        //return super.getView(position, convertView, parent);
        // Get the TextView we want to edit
        TextView theTextView3 = (TextView) theView.findViewById(R.id.textView3);

        // Put the next TV Show into the TextView
        String[] tvShow = getItem(position);
        theTextView3.setText(tvShow[1]);
        if(tvShow[0].equals("1")){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }

        return theView;
    }

}