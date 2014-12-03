package fr.free.simon.jacquemin.staupe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.container.Maul;

public class Tutorial extends SGMActivity {
	Maul taupe;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		init();
	}


    @Override
    public void actionClick(View v) {
        switch (v.getId()) {
            case R.id.game_btn_back:
                endActivity("Return");
                break;
            case R.id.game_btn_check:
                break;
        }
    }

    @Override
    public String getNameActivity() {
        return "Tutorial";
    }

    @Override
    protected void init() {
        super.init();

        ((TextView) findViewById(R.id.game_tv_level_name)).setTypeface(font);
        ((TextView) findViewById(R.id.game_tv_maul_shape_title)).setTypeface(font);
        ((Button) findViewById(R.id.game_btn_back)).setTypeface(font);
        ((Button) findViewById(R.id.game_btn_check)).setTypeface(font);
    }

}
