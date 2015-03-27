package io.brothers.sgm.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 20/03/2015.
 */
public class SGMUserManager {
    private List<SGMUser> users = new ArrayList<>();
    private static SGMUserManager instance = null;

    public static SGMUserManager getInstance(){
        if(instance == null)
            instance = new SGMUserManager();

        return instance;
    }

    /**
     *
     * @param id The id of a SGMUser registered to find
     * @return SGMUser find into the UserManager. <b>NULL</b> if no SGMUser is find
     */
    public SGMUser getUser(String id){
        SGMUser userFind = null;

        for (SGMUser user : users){
            if(user.id == id){
                userFind = user;
            }
        }

        return userFind;
    }

    /**
     *
     * @param userToAdd A SGMUser to add on the user manager
     * @return boolean to know if the user already exist into the list
     */
    public boolean registerUser(SGMUser userToAdd){
        boolean isAlreadyRegister = false;

        for (SGMUser user : users){
            if(user.id == userToAdd.id){
                isAlreadyRegister = true;
            }
        }

        if (!isAlreadyRegister)
            users.add(userToAdd);

        return isAlreadyRegister;
    }
}
