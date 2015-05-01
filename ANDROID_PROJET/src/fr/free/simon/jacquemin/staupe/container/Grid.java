package fr.free.simon.jacquemin.staupe.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;

public class Grid implements Tile.TileEventListener{
    @Override
    public void onTileClick() {
        if (listener != null)
            listener.onTileClick();
    }

    public interface GridEventListener{
        public void onTileClick();
    }

	private int width;
	private int height;
	private Tile[][] grid;
	private Tile[][] gridOrigin;
	private Activity parent;
	private Context ctx;
    private GridEventListener listener = null;

	public Grid(int height, int width, Activity parent, Context ctx, GridEventListener listener) {
		this.width = width;
		this.height = height;
		this.parent = parent;
		this.ctx = ctx;
        this.listener = listener;
		
		setGrid(new Tile[this.height][this.width]);
		gridOrigin = new Tile[this.height][this.width];
		
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				gridOrigin[i][j] =  new Tile(1, this.parent, this.ctx, this);
				getGrid()[i][j] = new Tile(1, this.parent, this.ctx, this);
			}
		}
	}

	public Tile getTile(int height, int width) {
		return getGrid()[height][width];
	}
	
	public Tile getTileOrigin(int height, int width) {
		return gridOrigin[height][width];
	}

    public int getNbTileBlocked(){
        int nbTileBlocked = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(getTile(i, j).getState() == 2)
                    nbTileBlocked++;
            }
        }

        return nbTileBlocked;
    }

	public int displayXTaupe(Maul t, int nbMaulToDisplay) {
		int nbMaulCanFit = 0;
		ArrayList<Integer> listMaul = new ArrayList<Integer>();
		
		t.setOriginalMaul(true);
		
		// Compter le nombre de taupe encore possible
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Pour chaque case de la grid
				for (int r = 0; r < 4; r++) {
					if (maulCanFit(t, i, j, getGrid(), height, width) == true) {
						nbMaulCanFit++;
					}
					// Rotation de la taupe
					t.setShape(t.rot90Hor(), false);
				}
			}
		}
		
		if(nbMaulCanFit == 0){
			return 0;
		}
		
		// Choisir les taupes � afficher
		if(nbMaulToDisplay >= nbMaulCanFit){
			for(int i = 0; i < nbMaulCanFit; i++){
				listMaul.add(i);
			}
		} else {
			List<Integer> l = new ArrayList<Integer>();
			for(int i = 0; i < nbMaulCanFit; i++){
				l.add(i);
			}
			l = pickNRandomElementsInList(l, nbMaulToDisplay);
			for(int i = 0; i < l.size(); i++){
				listMaul.add(l.get(i));
			}
		}
		
		// Afficher les taupes
		int idTaupe = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Pour chaque case de la grid
				for (int r = 0; r < 4; r++) {
					if (maulCanFit(t, i, j, getGrid(), height, width) == true) {
						
						if(listMaul.contains(idTaupe)){
							for (int iTaupe = 0; iTaupe < t.getHeight(); iTaupe++) {
								for (int jTaupe = 0; jTaupe < t.getWidth(); jTaupe++) {
									if(t.getCase(iTaupe, jTaupe) == 1){
										getGrid()[i + iTaupe][j + jTaupe].setState(5);
									}
									
								}
							}
						}
						idTaupe++;
					}
					// Rotation de la taupe
					t.setShape(t.rot90Hor(), false);
				}
			}
		}
		
		t.setOriginalMaul(false);
		
		return Math.min(nbMaulCanFit, nbMaulToDisplay);
	}
	
	public void clearMaul(){
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(getGrid()[i][j].getState() == 5){
					getGrid()[i][j].setState(1);
				}
			}
		}
	}
	
	public <T> List<T> pickNRandomElementsInList(List<T> array, int numberOfElements) {
	    Collections.shuffle(array);
	    array = array.subList(0, numberOfElements);

        List<T> list = new ArrayList<>();
        for (T ele : array)
	        list.add(ele);

	    return list;
	}

	public boolean verifyGrid(Maul t) {
		// Si taupe.possibilte == true => return false
		
		t.setOriginalMaul(true);
		
		// Algo de v�rification de la grid
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Pour chaque case de la grid
				boolean hasFindSolution = false;
				for (int r = 0; r < 4; r++) {
					if (maulCanFit(t, i, j, getGrid(), height, width) == true) {
						hasFindSolution = true;
					}
					// Rotation de la taupe
					t.setShape(t.rot90Hor(), false);
				}

				if (hasFindSolution == true) {
					return false;
				}

			}
		}
		
		t.setOriginalMaul(false);
		
		return true;
	}

	private boolean maulCanFit(Maul t, int x, int y, Tile[][] g, int h, int l) {
		boolean canFit = true;
				
		for (int iTaupe = 0; iTaupe < t.getHeight(); iTaupe++) {
			for (int jTaupe = 0; jTaupe < t.getWidth(); jTaupe++) {
				if (x + iTaupe >= h || y + jTaupe >= l) {
					// La case actuelle de la taupe est en dehors de la grid
					canFit = false;
				} else {
					// La case actuelle de la taupe est dans la grid
					if (t.getCase(iTaupe, jTaupe) == 0) {
						// La case actuelle de la taupe est vide
					} else {
						if (g[x + iTaupe][y + jTaupe].getState() != 1 && g[x + iTaupe][y + jTaupe].getState() != 3 && g[x + iTaupe][y + jTaupe].getState() != 5) {
							canFit = false;
						}
					}
				}
			}
		}
		
		return canFit;
	}

	public int findBestSolution(Maul t) {
		Tile[][] field = new Tile[height][width];
		
		t.setOriginalMaul(true);
		
		// On recopie la grid initiale
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				field[i][j] = new Tile(gridOrigin[i][j].getState(), this.parent, this.ctx, this);
			}
		}

		// Algo de v�rification de la grid
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Pour chaque case de la grid
				boolean hasFindSolution = false;
				for (int r = 0; r < 4; r++) {
					if (maulCanFit(t, i, j, field, height, width) == true) {
						hasFindSolution = true;
					}
					// Rotation de la taupe
					t.setShape(t.rot90Hor(), false);
				}

				if (hasFindSolution == true) {
					field[i][j].setState(1);
				} else {
					field[i][j].setState(3);
				}
			}
		}
		
		for (int j = width - 1; j >= 0; j--) {
			for (int i = height - 1; i >= 0; i--) {
				// Pour les cases contenant un 1 en partant de la case en bas �
				// droite et en remontant
				if(field[i][j].getState() == 1){
					boolean hasFindSolution = false;
					for (int r = 0; r < 4; r++) {
						if (maulCanFit(t, i, j, field, height, width) == true) {
							hasFindSolution = true;
						}
						// Rotation de la taupe
						t.setShape(t.rot90Hor(), false);
					}

					if (hasFindSolution == true) {
						field[i][j].setState(4);
					} else {
						field[i][j].setState(3);
					}
				}
			}
		}
		
		t.setOriginalMaul(false);
		
		// On compte le nombre de mine pos� (case = 4)
		return countNbMine(field, 4);
	}
	
	public int countNbMine(Tile[][] field, int idMine){
		int nbMine = 0;
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if(field[i][j].getState() == idMine){
					nbMine++;
				}
			}
		}
		
		return nbMine;
	}

	public Tile[][] getGrid() {
		return grid;
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
	}
}