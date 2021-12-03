package com.my_pls.application;

import spark.Request;
import spark.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager {

    public static Response downloadFile(Request request, Response response, String fileName){
        File file = new File("uploadFolder/"+ fileName);
        response.raw().setContentType("application/octet-stream");
        response.raw().setHeader("Content-Disposition","attachment; filename="+file.getName()+".zip");
        try {
            try(ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.raw().getOutputStream()));
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file)))
            {
                ZipEntry zipEntry = new ZipEntry(file.getName());

                zipOutputStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer,0,len);
                }
                zipOutputStream.flush();
                zipOutputStream.close();
            }
        } catch (Exception e) {
            System.out.println("Exception in downloading file: "+e.getMessage());
        }
        return response;
    }




}
