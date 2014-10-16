package fr.free.simon.jacquemin.staupe;

import fr.free.simon.jacquemin.staupe.carousel.Carousel;
import fr.free.simon.jacquemin.staupe.carousel.CarouselAdapter;
import fr.free.simon.jacquemin.staupe.carousel.CarouselAdapter.OnItemClickListener;
import fr.free.simon.jacquemin.staupe.carousel.CarouselItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

public class SelectWorld extends Activity {
	Carousel carousel;
	int positionItem = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_monde);

		carousel = (Carousel) findViewById(R.id.carousel);
		carousel.setAnimationDuration(200);
		carousel.setAnimationCacheEnabled(true);
		carousel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(CarouselAdapter<?> parent, View view,
					int position, long id) {
				if (positionItem == position) {
					int numWorld = Integer.parseInt(((CarouselItem) parent.getChildAt(position)).getName().split("_")[1]);
					actionGameManager("Choose", numWorld);
				} else {
					carousel.setSelection(position, true);
					positionItem = position;
				}

			}

		});

		carousel.setAnimationDuration(200);
	}

	// 2.0 and above
	@Override
	public void onBackPressed() {
		actionGameManager("Return", -1);
	}

	// Before 2.0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			actionGameManager("Return", -1);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void actionGameManager(String msg, int world) {
		// Création de l'intent
		Intent intent = new Intent();

		// On rajoute le nom saisie dans l'intent
		intent.putExtra(SGMGameManager.RESPOND_NAME, "World" + msg);
		
		intent.putExtra(SGMGameManager.WORLD, Integer.toString(world));

		// On retourne le résultat avec l'intent
		setResult(SGMGameManager.RESULT_OK, intent);

		// On termine cette activité
		finish();
	}

}
