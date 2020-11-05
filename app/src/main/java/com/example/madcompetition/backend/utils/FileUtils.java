package com.example.madcompetition.backend.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.webkit.MimeTypeMap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils
{

    public static boolean isVirtualFile(Uri uri, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!DocumentsContract.isDocumentUri(activity, uri)) {
                return false;
            }

            Cursor cursor = activity.getContentResolver().query(
                    uri,
                    new String[]{DocumentsContract.Document.COLUMN_FLAGS},
                    null, null, null);
            int flags = 0;
            if (cursor.moveToFirst()) {
                flags = cursor.getInt(0);
            }
            cursor.close();
            return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
        } else {
            return false;
        }
    }


    public static InputStream getInputStreamForVirtualFile(Uri uri, String mimeTypeFilter, Activity activity)
            throws IOException {

        ContentResolver resolver = activity.getContentResolver();

        String[] openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter);

        if (openableMimeTypes == null ||
                openableMimeTypes.length < 1) {
            throw new FileNotFoundException();
        }

        return resolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
                .createInputStream();
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


}
