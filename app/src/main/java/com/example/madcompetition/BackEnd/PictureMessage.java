package com.example.madcompetition.BackEnd;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.madcompetition.MediaType;

public class PictureMessage extends Message

{


    private Bitmap pictureMessage;


    public PictureMessage()
    {
        super();
    }

    public PictureMessage(Bitmap pictureMessage)
    {

        super();
    }

    public PictureMessage(Bitmap pictureMessage, Account sender, Account [] recepients)
    {
        super(sender,recepients, MediaType.Picture, "Picture");
        this.pictureMessage = pictureMessage;
    }


    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
