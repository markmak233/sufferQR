package com.example.sufferqr.ui.main;

import java.util.Map;

/**
 * a list for saving all the context of item
 */
public class sameQrListContext {
    private String user;

    /**
     * load info into list
     * @param myUser user name
     */
    public sameQrListContext(String myUser){
        user = myUser;
    }

    /**
     * get name
     * @return qr name
     */
    public String getName(){
        if (user==null){
            return "";
        } else {
            return user;
        }
    }
}
