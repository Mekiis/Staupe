package fr.free.simon.jacquemin.staupe.container;

import java.util.ArrayList;

public class Level {
	public int id = -1;
	public String name = "";
	public int lock = 0;
	public int width = 0;
	public int height = 0;
	public ArrayList<ArrayList<Integer>> rep = new ArrayList<ArrayList<Integer>>();
	public int idWorld = -1;
	public int widthTaupe = 0;
	public int heightTaupe = 0;
	public ArrayList<ArrayList<Integer>> taupe = new ArrayList<ArrayList<Integer>>();
}