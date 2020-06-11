package com.example.madcompetition.BackEnd.messaging.system;

import android.util.Log;


import com.example.madcompetition.BackEnd.messaging.system.Message;
import com.example.madcompetition.BackEnd.messaging.system.MessageAction;
import com.example.madcompetition.BackEnd.messaging.system.MessageFrom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MessageContraints implements Serializable
{

    private int unit;
    private ChronoUnit timeUnit;
    private MessageAction action;

    private MessageFrom from;


    private boolean actionCompleted;

    public MessageContraints(MessageAction action, MessageFrom from, ChronoUnit timeUnit, int unit)
    {
        this.action = action;
        this.timeUnit = timeUnit;
        this.unit = unit;
        this.from = from;
    }



    public boolean hasConstaintBeenMet(Message message)
    {
        boolean useDate = false;
        boolean error = false;
        LocalDate date = null;
        LocalTime time = null;

        if (from == MessageFrom.DateRecieved)
        {
            useDate = true;
            date = message.getDateRecieved();

        }
        else if (from == MessageFrom.DateSent)
        {
            useDate = true;
            date = message.getDateSent();

        }
        else if (from == MessageFrom.TimeRecieved)
        {
            time = message.getTimeRecieved();

        }
        else if (from == MessageFrom.TimeSent)
        {
            time = message.getTimeSent();

        }
        else
        {
            Log.i("MessageConstraint", "From enum is not set");
            // null
            return false;
        }


        LocalDate nowDate = LocalDate.now();
        LocalTime timeNow = LocalTime.now();



        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
        if (useDate)
        {
            Log.i("MessageConstraint", "old date : "  + date.format(DateTimeFormatter.ISO_LOCAL_DATE));
           date = date.plus(unit, timeUnit);

           Log.i("MessageConstraint", "Constrain date : "  + date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                   + "\nDate Now: " +  nowDate.format(DateTimeFormatter.BASIC_ISO_DATE));
           Log.i("MessageConstraint", Boolean.toString(nowDate.isAfter(date)));
           if (nowDate.isAfter(date))
           {
             return true;
           }

        }
        else
        {
            time = time.plus(unit, timeUnit);
            Log.i("MessageConstraint", "Constrain time : "  + time.format(DateTimeFormatter.ISO_LOCAL_TIME)
                    + "\nDate Now: " +  timeNow.format(DateTimeFormatter.ISO_LOCAL_TIME));
            Log.i("MessageConstraint", Boolean.toString(timeNow.isAfter(time)));

            if (timeNow.isAfter(time))
            {
                return true;
            }

        }

        return false;

    }


}
