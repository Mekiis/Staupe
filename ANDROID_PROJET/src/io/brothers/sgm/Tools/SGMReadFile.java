package io.brothers.sgm.Tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Simon on 17/11/2014.
 */
public abstract class SGMReadFile {

    /**
     * Decode and read a file in the assets folder of the project.
     * Override <b>protected void FileNotFoundException(FileNotFoundException e)</b> and
     * <b>protected void IOException(IOException e)</b> function to catch error while the operation.
     * @param context The context of the activity to open and read the file
     * @param fileName The name of the file (fileName.extension).<br/><u>Example :</u> <i>file.txt</i>
     * @return An <b>ArrayList<String></b> that is each line in one case of the array
     */
    protected ArrayList<String> readFromFile(Context context, String fileName) {

        ArrayList<String> strFinal = new ArrayList<String>();

        try {
            AssetManager am = context.getAssets();
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
            FileNotFoundException(e);
        } catch (IOException e) {
            IOException(e);
        }

        return strFinal;
    }

    protected void FileNotFoundException(FileNotFoundException e){
        Log.e(this.getClass().getName(), "File not found: " + e.toString());
    }

    protected void IOException(IOException e){
        Log.e(this.getClass().getName(), "Can not read file: " + e.toString());
    }
}
