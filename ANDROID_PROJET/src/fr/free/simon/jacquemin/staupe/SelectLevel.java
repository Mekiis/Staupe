package fr.free.simon.jacquemin.staupe;

import java.util.ArrayList;

import fr.free.simon.jacquemin.staupe.SGM.SGMActivity;
import fr.free.simon.jacquemin.staupe.container.Level;
import fr.free.simon.jacquemin.staupe.utils.ReadLevelFile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SelectLevel extends SGMActivity {
	private int index;
	private int idLevel = -1;
	private int idWorld = -1;
	private int scrollLevel = -1;
	public ScrollView lay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_level);

		init();
		choixLevel();

		lay = (ScrollView) findViewById(R.id.tutorial_scroll);

		lay.post(new Runnable() {
			public void run() {
				if (scrollLevel != -1) {
					lay.scrollTo(0,
							(int) ((LinearLayout) findViewById(R.id.select_level_sv_list))
									.getChildAt(scrollLevel).getY());
					Animation anim = new AlphaAnimation((float) 0.5, 1);
					anim.setDuration(2500);
					((Button) ((LinearLayout) findViewById(R.id.select_level_sv_list))
							.getChildAt(scrollLevel)).startAnimation(anim);
				}
			}
		});
	}

	public void choixLevel() {
		ReadLevelFile f = new ReadLevelFile();
		ArrayList<Level> allLevels = f.buildLevel(getApplicationContext(),
				"lvl.txt");
		allLevels = filtreIdMonde(idWorld, allLevels);
		LinearLayout linearLay = (LinearLayout) findViewById(R.id.select_level_sv_list);
		Button btn = new Button(getApplicationContext());
		// Cr�ation de la font

		for (int i = 0; i < allLevels.size(); i++) {
			btn = new Button(getApplicationContext());
			// Ajout de la font
			btn.setTypeface(font);
			btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimension(R.dimen.btn_level));
			// Ajout de l'image du background
			btn.setBackgroundResource(R.drawable.selector);
			// Ajout de la couleur noir
			btn.setTextColor(Color.parseColor("#000000"));

			btn.setPadding(0, 15, 15, 15);

			if (Integer.parseInt(getPref(
					SGMGameManager.FILE_STATS, SGMGameManager.STATS_ALL_STARS,
					"0")) >= allLevels.get(i).lock) {
				Bitmap bmOn = BitmapFactory.decodeResource(getResources(),
						R.drawable.star_on);
				Bitmap bmOff = BitmapFactory.decodeResource(getResources(),
						R.drawable.star_off);
				ArrayList<Bitmap> a = new ArrayList<Bitmap>();
				for (int j = 0; j < getStars(allLevels.get(i)); j++) {
					a.add(bmOn);
				}
				for (int j = getStars(allLevels.get(i)); j < 3; j++) {
					a.add(bmOff);
				}
				Drawable image = new BitmapDrawable(getResources(),
						createImageWithMultipleSources(a));
				btn.setCompoundDrawablesWithIntrinsicBounds(null, null, image,
						null);
				// Ajout du texte
				btn.setText(allLevels.get(i).name);
				index = allLevels.get(i).id;
				btn.setOnClickListener(new OnClickListener() {
					int level = index;

					@Override
					public void onClick(View v) {
						Button btn = (Button) v;
						endActivity("Ok", level);
					}
				});
			} else {
				ArrayList<Bitmap> a = new ArrayList<Bitmap>();
				a.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.star_on));
				a.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.lock));
				int numberMiss = allLevels.get(i).lock - Integer.parseInt(getPref(
						SGMGameManager.FILE_STATS, SGMGameManager.STATS_ALL_STARS,
						"0"));
				Drawable image = new BitmapDrawable(getResources(),
						createLockItem(a, "x"+numberMiss));
				btn.setCompoundDrawablesWithIntrinsicBounds(null, null, image,
						null);
				// Ajout du texte
				btn.setText(allLevels.get(i).name);
				btn.setSelected(true);
			}

			// Ajout du bouton � la liste
			linearLay.addView(btn);
		}

		// Fonction de scrolling jusqu'au pr�c�dent level (Si possible)
		Intent intent = getIntent();
		if (intent != null) {
			// R�cup�ration du nom du level
			idLevel = intent.getIntExtra(SGMGameManager.LEVEL, -1);

			if (idLevel != -1) {
				int i;
				// Recherche de l'instance pour calculer sa position
				for (i = 0; i < allLevels.size(); i++) {
					if (allLevels.get(i).id == idLevel) {
						// i*taille d'un bouton level+espacement
						scrollLevel = i;
						break;
					}
				}
			}
		}
	}

	// Combine Multi Image Into One
	private Bitmap createImageWithMultipleSources(ArrayList<Bitmap> bitmap) {
		int w = 0, h = 0;
		// Images
		for (int i = 0; i < bitmap.size(); i++) {
			if (i < bitmap.size() - 1) {
				h = bitmap.get(i).getHeight() > bitmap.get(i + 1).getHeight() ? bitmap
						.get(i).getHeight() : bitmap.get(i + 1).getHeight();
			}
			w += bitmap.get(i).getWidth();
		}

		// Drawing
		Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(temp);
		int left = 0;
		for (int i = 0; i < bitmap.size(); i++) {
			left = (i == 0 ? 0 : left + bitmap.get(i).getWidth());
			canvas.drawBitmap(bitmap.get(i), left, 0f, null);
		}
		return temp;
	}

	// Combine Multi Image Into One
	private Bitmap createLockItem(ArrayList<Bitmap> bitmap, String text) {
		int w = 0, h = 0;
		// Images
		for (int i = 0; i < bitmap.size(); i++) {
			if (i < bitmap.size() - 1) {
				h = bitmap.get(i).getHeight() > bitmap.get(i + 1).getHeight() ? bitmap
						.get(i).getHeight() : bitmap.get(i + 1).getHeight();
			}
			w += bitmap.get(i).getWidth();
		}

		// Text
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(getResources().getColor(R.color.black));
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/Barthowheel Regular.ttf");
		paint.setTypeface(tf);
		paint.setTextSize(getResources().getDimension(R.dimen.text_Normal));

		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.width();
		int height = bounds.height();
		w += width;
		h = h > height ? h : height;
		
		// Marges
		Paint paintMarges = new Paint();
		paintMarges.setStyle(Style.FILL);
		paintMarges.setColor(getResources().getColor(R.color.transparent));
		w += 20;

		// Drawing
		Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(temp);

		int cursorX = 0;
		canvas.drawBitmap(bitmap.get(0), cursorX, 0f, null);
		cursorX += bitmap.get(0).getWidth();
		canvas.drawRect(new Rect(cursorX, 0, cursorX + 10, h), paintMarges);
		cursorX += 10;
		canvas.drawText(text, cursorX, h-(h-height)/2f, paint);
		cursorX += width;
		canvas.drawRect(new Rect(cursorX, 0, cursorX + 10, h), paintMarges);
		cursorX += 10;
		canvas.drawBitmap(bitmap.get(1), cursorX, 0f, null);
		
		return temp;
	}

	private ArrayList<Level> filtreIdMonde(int id,
			ArrayList<Level> allLevels) {
		ArrayList<Level> levels = new ArrayList<Level>();

		if (id == -1) {
			return allLevels;
		}

		for (int i = 0; i < allLevels.size(); i++) {
			if (allLevels.get(i).idWorld == id) {
				levels.add(allLevels.get(i));
			}
		}

		return levels;
	}
	
	private int getStars(Level l) {
		SharedPreferences preferences = getSharedPreferences(
				SGMGameManager.FILE_LEVELS, 0);
		return Integer.parseInt(preferences.getString(SGMGameManager.LVL_STARS
				+ l.id, "0"));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			endActivity("Back", -1);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	public void actionClick(View v) {
		switch (v.getId()) {
		case R.id.select_level_btn_back:
			endActivity("Back", -1);
		}
	}

    @Override
    public String getNameActivity() {
        return "SelectLevel";
    }

    @Override
	protected void init() {
		super.init();
		
		((Button) findViewById(R.id.select_level_btn_back)).setTypeface(font);

		Intent intent = this.getIntent();
		idWorld = intent.getIntExtra(SGMGameManager.WORLD, -1);
	}

	protected void endActivity(String msg, int idLevel) {
		Intent intent = getIntent();
		
		intent.putExtra(SGMGameManager.LEVEL, idLevel);
		
		super.endActivity(msg);
	}
}