package com.tcs.Library.utils;

public class BookUtils {
    public static String getParentfromCopyId(String cpyId) {
        return cpyId.split("-")[0];
    }
    public static Long  getBookIdfromPublicId(String publicId){
        return Long.parseLong(new String(java.util.Base64.getDecoder().decode(publicId)));
    }
}
