package fr.free.simon.jacquemin.staupe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;

public class Story extends SGMActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story);

		init();
	}
	
	@Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.rules_btn_back:
			endActivity("Back");
		}
	}

    @Override
    public String getNameActivity() {
        return "Story";
    }

    @Override
    public void init() {
		super.init();
		
		((TextView) findViewById(R.id.story_tv_display_story)).setTypeface(font);
		((Button) findViewById(R.id.rules_btn_back)).setTypeface(font);
	}
}
