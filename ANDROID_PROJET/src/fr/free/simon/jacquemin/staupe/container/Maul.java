package fr.free.simon.jacquemin.staupe.container;

import fr.free.simon.jacquemin.staupe.SGM.utils.SGMMath;

public class Maul {
	private int[][] shape;
	private int[][] shapePure;
	private int width;
	private int height;

	public Maul() {
		width = 0;
		height = 0;
	}

	public void randomizeShape(int l, int h, boolean needCompleteShape) {
		width = l;
		height = h;

		setNullShape();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				shape[j][i] = SGMMath.randInt(0, 1);
			}
		}
		
		shapePure = shape;
		
		if(needCompleteShape){
			shape = completeForme(shape);
			getSize();
		}
	}

	public int getWeight() {
		int nb = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (shape[j][i] == 1) {
					nb++;
				}
			}
		}

		return nb;
	}

	public void setShape(int[][] f, boolean completeForme) {
		shape = f;
		shapePure = f;
		getSize();
		
		if(completeForme){
			shape = completeForme(shape);
			getSize();
		}
	}
	
	public void setOriginaleTaupe(boolean a_value){
		if(a_value){
			shape = shapePure;
			getSize();
		} else {
			shape = completeForme(shape);
			getSize();
		}
	}
	
	
	public int[][] completeForme(int[][] f){
		if(f.length == 0){
			return null;
		}

        return shapePure;
        /*
		int maxSize = Math.max(f.length, f[0].length);
		
		int[][] formeCarre = new int[maxSize][maxSize];
		for(int i = 0; i < f.length; i++){
			for(int j = 0; j < f[i].length; j++){
				if(i > height || j > width){
					formeCarre[i][j] = 0;
				} else {
					formeCarre[i][j] = f[i][j];
				}
				
			}
		}
		
		return formeCarre;
        */

	}

	public int[][] getShape() {
		return shape;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void setNullShape() {
		shape = new int[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				shape[j][i] = 0;
			}
		}
	}

	private void getSize() {
		height = shape.length;
		width = 0;
		if (height != 0) {
			width = shape[0].length;
		}
	}

	public int getCase(int height, int width) {
		return shape[height][width];
	}

	public int[][] rot90Hor() {
		int[][] rotate = new int[shapePure[0].length][shapePure.length];

		for (int i = 0; i < shapePure[0].length; i++) {
			for (int j = 0; j < shapePure.length; j++) {
				rotate[i][shapePure.length - 1 - j] = shapePure[j][i];
			}
		}

		return rotate;
	}
}