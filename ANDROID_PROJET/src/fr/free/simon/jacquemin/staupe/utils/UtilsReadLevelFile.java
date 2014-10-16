package fr.free.simon.jacquemin.staupe.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class UtilsReadLevelFile {
	public ArrayList<UtilsLevel> allLevel = new ArrayList<UtilsLevel>();
	
	public ArrayList<String> readFromFile(Context c, String fileName) {

	    ArrayList<String> strFinal = new ArrayList<String>();

	    try {
	    	AssetManager am = c.getAssets();
	    	InputStream inputStream = am.open(fileName);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	            	strFinal.add(receiveString);
	            }

	            inputStream.close();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return strFinal;
	}
	
	public ArrayList<UtilsLevel> buildLevel(Context c, String id){
		allLevel = new ArrayList<UtilsLevel>();
		ArrayList<String> l = readFromFile(c, "lvl.txt");

		for(int d = 0; d < l.size(); d++) {
			String[] tokens = l.get(d).split(":");
			if(tokens[0].equalsIgnoreCase("id")){
				allLevel.add(new UtilsLevel());
				allLevel.get(allLevel.size()-1).id = Integer.parseInt(tokens[1]);
			} else if(tokens[0].equalsIgnoreCase("name")){
				allLevel.get(allLevel.size()-1).name = tokens[1];
			} else if(tokens[0].equalsIgnoreCase("world")){
				allLevel.get(allLevel.size()-1).idWorld = Integer.parseInt(tokens[1]);
			} else if(tokens[0].equalsIgnoreCase("lock")){
				allLevel.get(allLevel.size()-1).lock = Integer.parseInt(tokens[1]);
			}  else if(tokens[0].equalsIgnoreCase("level")){
				boolean hasFindEndMap = false;
				do{
					d++;
					if(l.get(d).equalsIgnoreCase(":level") == true){
						hasFindEndMap = true;
					} else {
						String[] mot = l.get(d).split(" ");
						allLevel.get(allLevel.size()-1).rep.add(new ArrayList<Integer>());
						for(int j = 0; j < mot.length; j++){
							allLevel.get(allLevel.size()-1).rep.get(allLevel.get(allLevel.size()-1).rep.size()-1).add(Integer.parseInt(mot[j]));
							allLevel.get(allLevel.size()-1).width = allLevel.get(allLevel.size()-1).rep.get(allLevel.get(allLevel.size()-1).rep.size()-1).size();
						}
						allLevel.get(allLevel.size()-1).height = allLevel.get(allLevel.size()-1).rep.size();
					}
					
				}while(d < l.size() && !hasFindEndMap);
			} else if(tokens[0].equalsIgnoreCase("taupe")){
				boolean hasFindEndMap = false;
				do{
					d++;
					if(l.get(d).equalsIgnoreCase(":taupe") == true){
						hasFindEndMap = true;
					} else {
						String[] mot = l.get(d).split(" ");
						allLevel.get(allLevel.size()-1).taupe.add(new ArrayList<Integer>());
						for(int j = 0; j < mot.length; j++){
							allLevel.get(allLevel.size()-1).taupe.get(allLevel.get(allLevel.size()-1).taupe.size()-1).add(Integer.parseInt(mot[j]));
							allLevel.get(allLevel.size()-1).widthTaupe = allLevel.get(allLevel.size()-1).taupe.get(allLevel.get(allLevel.size()-1).taupe.size()-1).size();
						}
						allLevel.get(allLevel.size()-1).heightTaupe = allLevel.get(allLevel.size()-1).taupe.size();
					}
					
				}while(d < l.size() && !hasFindEndMap);
			}
		}
		
		return allLevel;
	}
}