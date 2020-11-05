package com.example.madcompetition.backend.server.ftp;

import android.net.Uri;

import com.example.madcompetition.backend.utils.StringUtils;

import java.io.File;
import java.io.Serializable;

public class FileData implements Serializable {
    private String oneTimeDownloadKey;


    private  File file;
    private String uri;
    private String filePath;
    private long fileLength;

    public FileData(File sendFile)
    {

        this.setFile(sendFile);
        this.setFilePath(sendFile.toPath().toString());
        this.setUri(sendFile.toURI().toString());
        setFileLength((int)sendFile.length());
        if (oneTimeDownloadKey != null)
        {
            if (oneTimeDownloadKey.length() < 8)
            {
                setOneTimeDownloadKey(StringUtils.generateID(8));
            }
        }



    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        setFileLength(this.file.length());
        setFilePath(this.file.toPath().toString());
        setUri(Uri.parse(this.file.toURI().toString()).toString());
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
    /**
     * @return the oneTimeDownloadKey
     */
    public String getOneTimeDownloadKey()
    {
        return oneTimeDownloadKey;
    }

    /**
     * @param oneTimeDownloadKey the oneTimeDownloadKey to set
     */
    public void setOneTimeDownloadKey(String oneTimeDownloadKey)
    {
        this.oneTimeDownloadKey = oneTimeDownloadKey;
    }

    public boolean equals(Object obj)
    {
        return false;
    }
}
