package com.tn74travellers.aroDriver.Common;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class FilePathVariables
{
    public File aro_container_folder;
    public File aro_captured_images_folder;
    public File aro_pdf_documents_folder;
    public File aro_myload_captured_images_folder;
    public File aro_myload_pdf_documents_folder;
    public File captured_camera_profile_image;
    public File captured_camera_or_gallery_images;
    public File captured_camera_temp_image;


    public FilePathVariables(Context context)
    {
        aro_container_folder         = context.getExternalFilesDir("aro_Private_Container");  //+"/Edugate_Container";

        assert aro_container_folder != null;
        aro_captured_images_folder                   = new File(aro_container_folder.getAbsolutePath()+"/aro_Captured_Images_Folder");
        aro_myload_captured_images_folder            = new File(aro_container_folder.getAbsolutePath()+"/aro_MyLoad_Captured_Images_Folder");
        aro_pdf_documents_folder                     = new File(aro_container_folder.getAbsolutePath()+"/aro_Uploaded_Pdf_Documents");
        aro_myload_pdf_documents_folder              = new File(aro_container_folder.getAbsolutePath()+"/aro_Myload_Uploaded_Pdf_Documents");
        captured_camera_profile_image                       = new File(aro_container_folder.getAbsolutePath()+"/captured_camera_profile_image.jpg");
        //captured_camera_or_gallery_images                       = new File(aro_container_folder.getAbsolutePath()+"/captured_camera_or_gallery_selected_images");
        captured_camera_temp_image                          = new File(aro_container_folder.getAbsolutePath()+"/captured_camera_temp_image.jpg");

        Log.i("->"," aro_container_folder : "+aro_container_folder.getAbsolutePath());
        Log.i("->"," aro_documents_folder : "+aro_captured_images_folder.getAbsolutePath());
       Log.i("->"," captured_camera_profile_image : "+captured_camera_profile_image.getAbsolutePath());

    }

}
