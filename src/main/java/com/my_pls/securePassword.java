package com.my_pls;

import org.apache.commons.codec.digest.DigestUtils;

public class securePassword {
    private static String hashstring = "myPLSRIT";

    public String hashPassword(String password){
        String myHash = DigestUtils.md5Hex(password+hashstring);
        return myHash;
    }
    public boolean comparePassword(String savedPassword,String inputPassword){
        String inputHashed = hashPassword(inputPassword);
        boolean flag = false;
        if (inputHashed.equals(savedPassword))
            flag = true;
        return flag;
    }
}