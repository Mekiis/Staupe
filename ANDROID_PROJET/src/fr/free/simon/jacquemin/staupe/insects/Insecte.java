package fr.free.simon.jacquemin.staupe.insects;

import fr.free.simon.jacquemin.staupe.SGM.utils.SGMMath;
import fr.free.simon.jacquemin.staupe.SGMGameManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class Insecte extends AsyncTask<Object, Integer, Void> {

	private Activity parent = null;
	private Context ctx = null;
	private AnimationDrawable life = null;
	private AnimationDrawable death = null;
	private ImageView image = null;

	private long currentTime = 0;

	// Controllers
	private float yAnimation = 0;
	private float durationInSecond = 25f;
	private float durationInPixel = 1500;
	private float startInPixel = -500;
	private float alphaFadeOutDurationInSecond = 5f;
	private float waitTransitionInSecond = 7f;
	// Calculate automatically
	private float frameRate = 1000 / 60;
	private float pixelPerFrame = (durationInPixel / durationInSecond)
			/ frameRate;
	private float alphaPerFrame = (1f / alphaFadeOutDurationInSecond)
			/ frameRate;

	private AnimatorDone animatorLifeDone;
	private AnimatorDone animatorDeathDone;

	private int idAnim = -1;
	
	private int eventX;
	private int eventY;
    private boolean isClicked = false;

	@Override
	protected Void doInBackground(Object... params) {
		image = (ImageView) params[0];
		parent = (Activity) params[1];
		ctx = (Context) params[2];
		int screenHeight = (Integer) params[3];
		startInPixel = (Float) params[4];
		durationInPixel = (Float) params[5];

		idAnim = SGMMath.randInt(0, 3);

		life = SGMGameManager.listAnimation
				.get(SGMGameManager.listNom.length - 1 + (idAnim * 2) + 1);
		animatorLifeDone = new AnimatorDone(life, true);
		death = SGMGameManager.listAnimation
				.get(SGMGameManager.listNom.length - 1 + (idAnim * 2) + 2);
		animatorDeathDone = new AnimatorDone(death);
		
		yAnimation = (float) SGMMath.randInt(0, screenHeight-life.getFrame(0).getBounds().height());

		Log.d("Staupe", "Insecte : "+life.getNumberOfFrames());
		
		parent.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				life.stop();
				life.selectDrawable(0);
				image.setBackground(life);
				life.start();

				animatorLifeDone.checkIfAnimationDone();

				image.setX(startInPixel);
				image.setY(yAnimation);
				image.setAlpha(1.0f);
			}
		});

		image.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN && !isClicked) {
                    isClicked = true;

					animatorLifeDone.stop();
					
					eventX = (int) event.getX();
			        eventY = (int) event.getY();
					parent.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							image.setX(image.getX()+eventX-(image.getWidth()/2f));
							image.setY(image.getY()+eventY-(image.getHeight()/2f));
						
							death.stop();
							death.selectDrawable(0);
							image.setBackground(death);
							death.start();

							animatorDeathDone.checkIfAnimationDone();
						}
					});

                    return true;
				}

				return false;
			}
		});

		currentTime = System.currentTimeMillis();
		while (image.getX() < durationInPixel
				&& !animatorLifeDone.getCallback()) {
			if (System.currentTimeMillis() - currentTime > frameRate) {
				currentTime = System.currentTimeMillis();
				parent.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						image.setX(image.getX() + pixelPerFrame);
					}
				});
			}
		}

		if (image.getX() >= durationInPixel){
			parent.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					image.setBackground(null);
				}
			});
			return null;
		}

		while (!animatorDeathDone.getCallback()) {
		}

		currentTime = System.currentTimeMillis();
		while (waitTransitionInSecond > 0.0f) {
			waitTransitionInSecond -= ((System.currentTimeMillis() - currentTime) / 60f);
			currentTime = System.currentTimeMillis();
		}

		currentTime = System.currentTimeMillis();
		while (image.getAlpha() > 0.0f) {
			if (System.currentTimeMillis() - currentTime > frameRate) {
				currentTime = System.currentTimeMillis();
				parent.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						image.setAlpha(SGMMath.clamp(image.getAlpha()
								- alphaPerFrame, 0.0f, 1.0f));
					}
				});
			}
		}
		
		parent.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				image.setBackground(null);
			}
		});

		return null;
	}

	public AnimationDrawable copyAnimation(AnimationDrawable anim) {
		AnimationDrawable animation = new AnimationDrawable();

		for (int i = 0; i < anim.getNumberOfFrames(); i++) {
			animation.addFrame(anim.getFrame(i), 40);
		}

		return animation;
	}

	private class AnimatorDone {
		private AnimationDrawable a = null;
		private boolean l = false;
		private boolean s = false;
		private boolean c = false;

		public AnimatorDone(AnimationDrawable anim) {
			a = anim;
		}

		public AnimatorDone(AnimationDrawable anim, boolean loop) {
			a = anim;
			l = loop;
		}

		public void checkIfAnimationDone() {
			if (a == null) {
				Log.d("Staupe", "Error - No animation");
				c = true;
				s = false;
				return;
			}
			int timeBetweenChecks = 300;
			Handler h = new Handler();
			h.postDelayed(new Runnable() {
				public void run() {
					if (s) {
						c = true;
						return;
					}
					if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
						checkIfAnimationDone();
					} else {
						a.stop();
						if (l) {
							a.selectDrawable(0);
							a.start();
							checkIfAnimationDone();
						} else {
							c = true;
						}

					}
				}
			}, timeBetweenChecks);
		}

		public boolean getCallback() {
			if(s)
				return true;
			return c;
		}

		public void stop() {
			s = true;
		}
	}

}
