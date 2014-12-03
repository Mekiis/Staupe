package fr.free.simon.jacquemin.staupe.stats;

import fr.free.simon.jacquemin.staupe.R;
import fr.free.simon.jacquemin.staupe.R.id;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

// here's our beautiful adapter
public class StatsAdapter extends ArrayAdapter<StatsItem> {

    Context mContext;
    int layoutResourceId;
    StatsItem data[] = null;
    int sizeScreen;

    public StatsAdapter(Context mContext, int layoutResourceId, StatsItem[] data, int sizeScreen) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        this.sizeScreen = sizeScreen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout. 
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        StatsItem objectItem = data[position];
        
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/Barthowheel Regular.ttf");

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView statsViewItemValue = (TextView) convertView.findViewById(R.id.statsViewItemValue);
        statsViewItemValue.setText(objectItem.itemId + " : ");
        statsViewItemValue.setTypeface(tf);
        TextView statsViewItemName = (TextView) convertView.findViewById(R.id.statsViewItemName);
        statsViewItemName.setText(objectItem.itemName); 
        statsViewItemName.setTypeface(tf);
        
        return convertView;
    }
}