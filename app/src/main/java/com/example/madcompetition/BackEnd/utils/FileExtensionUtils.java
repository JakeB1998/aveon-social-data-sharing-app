package com.example.madcompetition.BackEnd.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.example.madcompetition.BackEnd.messaging.system.FileMessage;
import com.example.madcompetition.R;

import java.io.File;

public class FileExtensionUtils
{



    public static Drawable retrieveFileDrawable(String filePath, Context context)
    {
        Log.i(FileExtensionUtils.class.getName(), "File path : " + filePath);
        Drawable drawable = context.getResources().getDrawable(R.drawable.default_add_file_foreground, context.getTheme());
        String fileExtention =filePath.substring(filePath.lastIndexOf("."));
        Log.i("FileExstentionUtils", fileExtention);
        switch (fileExtention)
        {
            case ".docx" :
                drawable = context.getResources().getDrawable(R.drawable.default_add_file_foreground, context.getTheme());
                break;
            case ".pdf":
                drawable = context.getResources().getDrawable(R.drawable.default_add_file_foreground, context.getTheme());
                break;
            case ".png":
                drawable = new BitmapDrawable(context.getResources(),BitmapFactory.decodeFile(filePath));
                break;
            case ".jpeg":
                drawable = new BitmapDrawable(context.getResources(),BitmapFactory.decodeFile(filePath));
                break;
            case ".txt":
                break;


                default:
        }
        return drawable;
    }

}
