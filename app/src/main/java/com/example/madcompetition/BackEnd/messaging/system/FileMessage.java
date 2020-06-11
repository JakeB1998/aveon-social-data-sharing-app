package com.example.madcompetition.BackEnd.messaging.system;

import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.server.ftp.FileData;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class FileMessage extends Message {


    private boolean downloadable;
    private boolean downloaded;



    public FileMessage(AccountInformation sender, AccountInformation [] recepients, File sendFile)
    {

        super(sender,recepients,MediaType.File, sendFile.getAbsolutePath());
        setMessageFileData(new FileData(sendFile));
        setDownloadable(true);
        setDownloaded(false);
        setTimeSent(LocalTime.now());
        setDateSent(LocalDate.now());

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



}
