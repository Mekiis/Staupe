package fr.free.simon.jacquemin.staupe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Story extends SGMScreenInterface {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story);

		init();
	}
	
	@Override
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_rules:
			endActivity("Back");
		}
	}

    @Override
    public String getNameActivity() {
        return "Story";
    }

    public void init() {
		super.init();
		
		((TextView) findViewById(R.id.tv_rules)).setTypeface(font);
		((Button) findViewById(R.id.btn_back_rules)).setTypeface(font);
	}
}
