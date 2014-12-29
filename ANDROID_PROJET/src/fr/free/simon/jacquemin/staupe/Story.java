package fr.free.simon.jacquemin.staupe;

import android.os.Bundle;
import android.view.KeyEvent;
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
		case R.id.story_btn_back:
			endActivity("Back");
		}
	}

    @Override
    public void onBackPressed() {
        endActivity("Back");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            endActivity("Back");
            return true;
        }
        return false;
    }

    @Override
    public String getNameActivity() {
        return "Story";
    }

    @Override
    public void init() {
		super.init();
		
		((TextView) findViewById(R.id.story_tv_display_story)).setTypeface(font);
	}
}
