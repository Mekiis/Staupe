package fr.free.simon.jacquemin.staupe.gui.achievements;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.free.simon.jacquemin.staupe.R;
import fr.free.simon.jacquemin.staupe.gui.stats.StatsItem;

// here's our beautiful adapter
public class AchievementsAdapter extends ArrayAdapter<AchievementsItem> {

    Context mContext;
    int layoutResourceId;
    AchievementsItem data[] = null;
    int sizeScreen;

    public AchievementsAdapter(Context mContext, int layoutResourceId, AchievementsItem[] data, int sizeScreen) {

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
        AchievementsItem objectItem = data[position];
        
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/Barthowheel Regular.ttf");
        TextView statsViewItemPercent = (TextView) convertView.findViewById(R.id.achievement_item_percent);
        if(!objectItem.isComplete)
            statsViewItemPercent.setText(objectItem.percentCompletion + "%");
        else
            statsViewItemPercent.setText("Ok");
        statsViewItemPercent.setTypeface(tf);
        TextView statsViewItemName = (TextView) convertView.findViewById(R.id.achievement_item_title);
        statsViewItemName.setText(objectItem.name);
        statsViewItemName.setTypeface(tf);
        TextView statsViewItemDesc = (TextView) convertView.findViewById(R.id.achievement_item_desc);
        statsViewItemDesc.setText(objectItem.desc);
        statsViewItemDesc.setTypeface(tf);
        
        return convertView;
    }
}