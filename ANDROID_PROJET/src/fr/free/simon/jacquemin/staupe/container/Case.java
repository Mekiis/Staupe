package fr.free.simon.jacquemin.staupe.container;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import io.brothers.sgm.Tools.SGMMath;
import fr.free.simon.jacquemin.staupe.SGMGameManager;
import fr.free.simon.jacquemin.staupe.R;

public class Case {
	private int state;
	private ImageButton imgBtn;
	private Grid grille;
	/*
	 * State : Pour le game : 0 = Inexistant 1 = Libre 2 = Bloqu� par joueur 5 =
	 * Avec taupe Pour l'algo : 1 = Libre mais avec taupe 3 = Libre mais sans
	 * taupe 4 = Bloqu� par l'algo
	 */

	private AnimationDrawable anim = new AnimationDrawable();
	private Activity parent;
	private Context ctx;
	private ImageView animContainer = null;
	private int idAnim = -1;

	public Case(Activity parent, Context ctx) {
		this.state = 0;
		this.parent = parent;
		this.ctx = ctx;
		this.animContainer = new ImageView(ctx);
		this.animContainer.setImageResource(R.drawable.case_none);
		this.animContainer.setScaleType(ImageView.ScaleType.FIT_START);
		this.animContainer.setVisibility(View.INVISIBLE);

		refreshImg();
	}

	public Case(int state, Activity parent, Context ctx) {
		this.state = state;
		this.parent = parent;
		this.ctx = ctx;
		this.animContainer = new ImageView(ctx);
		this.animContainer.setImageResource(R.drawable.case_none);
		this.animContainer.setScaleType(ImageView.ScaleType.FIT_START);
		this.animContainer.setVisibility(View.INVISIBLE);

		refreshImg();
	}

	public int randomizeAnim(int idWorld) {
		int compteur = 0;
		for (int i = 0; i < SGMGameManager.listWorld.length; i++) {
			if (SGMGameManager.listWorld[i] == idWorld) {
				compteur++;
			}
		}

		if (compteur == 0)
			return -1;

		int idAnim = SGMMath.randInt(0, compteur - 1);

		compteur = 0;
		for (int i = 0; i < SGMGameManager.listWorld.length; i++) {
			if (SGMGameManager.listWorld[i] == idWorld) {
				if (compteur == idAnim) {
					anim = copyAnimation(SGMGameManager.listAnimation.get(i));
					break;
				} else {
					compteur++;
				}

			}
		}

		return idAnim;
	}

	public AnimationDrawable copyAnimation(AnimationDrawable anim) {
		AnimationDrawable animation = new AnimationDrawable();

		for (int i = 0; i < anim.getNumberOfFrames(); i++) {
			animation.addFrame(anim.getFrame(i), 40);
		}

		return animation;
	}

	public void setState(int state) {
		this.state = state;
		refreshImg();
	}

	public int getState() {
		return this.state;
	}

	public void setImgBtn(ImageButton i, Grid parent,
			RelativeLayout layoutAnim) {
		this.imgBtn = i;
		this.grille = parent;
		this.imgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				grille.clearTaupe();
				if (state == 1 || state == 5) {
					state = 2;
				} else if (state == 2) {
					state = 1;
				}
				refreshImg();
			}
		});

		if (imgBtn == null) {
			return;
		}

		layoutAnim.addView(animContainer, new LayoutParams(0, 0));

		refreshImg();
	}

	public ImageButton getImgBtn() {
		return this.imgBtn;
	}

	public void refreshImg() {
		if (imgBtn == null) {
			return;
		}

		if (state == 1) {
			idAnim = -1;
			imgBtn.setBackgroundResource(R.drawable.case_normal);
			this.animContainer.setVisibility(View.INVISIBLE);
		} else if (state == 2) {
			initAnim();
			anim.stop();
			anim.selectDrawable(0);
			this.animContainer.setBackground(anim);
			this.animContainer.setVisibility(View.VISIBLE);
			anim.start();
		} else if (state == 5) {
			idAnim = -1;
			imgBtn.setBackgroundResource(parent.getResources().getIdentifier(
					"case_taupe" + SGMMath.randInt(1, 5), "drawable",
					ctx.getPackageName()));
		} else {
			idAnim = -1;
			imgBtn.setBackgroundResource(R.drawable.case_none);
		}
	}

	public void initAnim() {
		if (idAnim == -1) {
			idAnim = randomizeAnim(1);
		}

		animContainer.getLayoutParams().height = (int) (imgBtn.getWidth() * SGMGameManager.listRatio[idAnim]);
		animContainer.getLayoutParams().width = imgBtn.getWidth();

		this.animContainer.setX(((GridLayout) imgBtn.getParent()).getX()
				+ imgBtn.getX());
		this.animContainer
				.setY(((GridLayout) imgBtn.getParent()).getY()
						+ imgBtn.getY()
						- ((int) (imgBtn.getWidth() * SGMGameManager.listRatio[idAnim]) - imgBtn
								.getHeight()));
	}
}