package com.example.madcompetition.backend.Interfaces;

public interface ProgressDataTransferCallback
{
    void uploadProgressUpdate(int progress);
    void downloadProgressUpdate(int progress);
}
