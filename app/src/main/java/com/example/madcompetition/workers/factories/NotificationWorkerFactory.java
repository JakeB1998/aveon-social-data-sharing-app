package com.example.madcompetition.workers.factories;

import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.madcompetition.workers.NotificationWorker;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class NotificationWorkerFactory
{
    public static void queNotificationJob(Context context)
    {
        final String Tag = "NotificationDestroy";
        try {

            Log.i("WorkManager", "Notification Builder Method Called");

            OneTimeWorkRequest saveRequest = NotificationWorkerFactory.buildNotificationRequest();

            ListenableFuture<List<WorkInfo>> future = WorkManager.getInstance(context).getWorkInfosByTag(Tag);
            List<WorkInfo> list = future.get();
            boolean invalid = false;
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).getState() == WorkInfo.State.ENQUEUED)
                {
                    invalid = true;
                    break;
                }
            }
            if (invalid == false)
            {
                Log.i("WorkManager", "Work enqued");
                WorkManager.getInstance(context).enqueue(saveRequest);
            }
            else
            {
                Log.i("WorkManager", "Work Not enqued");

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("WorkManager", e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("WorkManager", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("WorkManager", e.getMessage());
        }

        Log.i("WorkManager", "Method finished");

    }


    public static OneTimeWorkRequest buildNotificationRequest()
    {
        final String Tag = "NotificationDestroy";

        Constraints constraints = null;
        Data inputData = null;
        inputData = new Data.Builder()
                .putString(NotificationWorker.TITLE_KEY, "Message")
                .putString(NotificationWorker.CONTENT_TEXT_KEY, "Botka Content")
                .build();

        constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest saveRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .addTag(Tag)
                        .setInitialDelay(10, TimeUnit.SECONDS)
                        .setConstraints(constraints)
                        .setInputData(inputData)
                        .build();

        return saveRequest;

    }
}
