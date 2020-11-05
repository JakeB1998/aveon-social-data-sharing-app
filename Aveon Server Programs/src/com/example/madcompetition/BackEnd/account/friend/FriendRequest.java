package com.example.madcompetition.backend.account.friend;

import com.example.madcompetition.backend.account.AccountInformation;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class FriendRequest implements Serializable {

    private FriendRequestResult result;

    private AccountInformation source;
    private AccountInformation target;


    private boolean onResultReturn;
    private final LocalDate dateRequested;
    private final LocalTime timeRequested;

    private boolean valid;


    public FriendRequest(boolean request, AccountInformation source, AccountInformation target)
    {
        setOnResultReturn(false);

        if (request)
        {
            valid = true;
            this.source = source;
            this.target = target;
        }

        dateRequested = LocalDate.now();
        timeRequested = LocalTime.now();
        setResult(FriendRequestResult.Pending);
    }


    public FriendRequestResult getResult() {
        return result;
    }

    public void setResult(FriendRequestResult result) {
        this.result = result;
    }

    public AccountInformation getSource() {
        return source;
    }



    public AccountInformation getTarget() {
        return target;
    }



    public LocalDate getDateRequested() {
        return dateRequested;
    }

    public LocalTime getTimeRequested() {
        return timeRequested;
    }

    public boolean isValid() {
        return valid;
    }


    public boolean isOnResultReturn() {
        return onResultReturn;
    }

    public void setOnResultReturn(boolean onResultReturn) {
        this.onResultReturn = onResultReturn;
    }
}
