package com.example.madcompetition.BackEnd.Interfaces;

public interface ProgressDataTransferCallback
{
    public void uploadProgressUpdate(int progress);
    public void downloadProgressUpdate(int progress);
}
