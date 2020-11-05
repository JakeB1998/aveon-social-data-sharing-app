package com.example.madcompetition.backend.messaging.system;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.madcompetition.backend.Interfaces.DataTransferCallback;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.server.ftp.FileData;
import com.example.madcompetition.backend.utils.StringUtils;
import com.example.madcompetition.activties.ActivityConversationInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PictureMessage extends Message {
    private DataTransferCallback callback;

    private transient Bitmap pictureMessage;


    private boolean downloadable;
    private boolean downloaded;
    private transient boolean loaded;
    private boolean readyToSend;


    public PictureMessage(Bitmap pictureMessage, AccountInformation sender, AccountInformation[] recepients) {
        super(sender, recepients, MediaType.Picture, "Picture");
        this.pictureMessage = pictureMessage;
        setTimeSent(LocalTime.now());
        setDateSent(LocalDate.now());
        setDownloadable(true);
        setDownloaded(false);

    }


    public Bitmap getPictureMessage() {
        return pictureMessage;
    }

    public void setPictureMessage(Bitmap pictureMessage) {
        this.pictureMessage = pictureMessage;
    }

    public static void registerCallback(DataTransferCallback callback) {
        callback.TransferData(null);
    }

    public void prepMessage(Context context, ProgressBar progressBar, ActivityConversationInterface.MessageReadyCallback callback) {
        PrepareMessageTask task = new PrepareMessageTask(progressBar, pictureMessage, context, callback);
        task.execute(progressBar);
    }

    @NonNull
    @Override
    public String toString() {
        String str = "";

        String str1 = "\nBitmap : " + getPictureMessage().toString();


        if (getPictureMessage() == null) {
            str1 = "\nBitmap : null";
        }


        str = str1;
        return str;
    }


    public DataTransferCallback getCallback() {
        return callback;
    }

    public void setCallback(DataTransferCallback callback) {
        this.callback = callback;
    }


    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }


    private class PrepareMessageTask extends AsyncTask<ProgressBar, Integer, Boolean> {
        private ProgressBar progressBar;
        private Bitmap bitmap;
        private Context context;
        private ActivityConversationInterface.MessageReadyCallback callback;

        public PrepareMessageTask(ProgressBar bar, Bitmap bitmap, Context context, ActivityConversationInterface.MessageReadyCallback callback) {
            this.progressBar = bar;
            this.bitmap = bitmap;
            this.context = context;
            this.callback = callback;

        }


        @Override
        protected Boolean doInBackground(ProgressBar... progressBars) {
            publishProgress(0);
            FileData data = convertImageToPNG(bitmap);
            setMessageFileData(data);
            publishProgress(100);

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            if (values[0] == 100) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (callback != null) {
                    Log.i(this.getClass().getName(), "Callback called");
                    callback.MessageReadyCallback(getMessageFileData());
                }
            }
            Log.i(this.getClass().getName(), "Progress updated");
            super.onProgressUpdate(values);
        }


        public FileData convertImageToPNG(Bitmap bitmap) {

            try {
                File file = new File(context.getFilesDir(), StringUtils.generateID(5) + getDateSent().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".png");
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                //pictureMessage =  BitmapFactory.decodeFile(file.getPath());
                FileData fileData = new FileData(file);


                Log.i(this.getClass().getName(), "File Path : " + file.getAbsolutePath() + "\nFile size : " + file.length());
                return fileData;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


    }

}
