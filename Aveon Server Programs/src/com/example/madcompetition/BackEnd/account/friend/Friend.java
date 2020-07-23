package com.example.madcompetition.BackEnd.account.friend;



import com.example.madcompetition.BackEnd.Enums.FriendRequestStatus;
import com.example.madcompetition.BackEnd.account.AccountInformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Friend implements Serializable, Comparable<Friend>, Comparator<Friend> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountInformation friendAccount;
    private String nickName;

    private boolean muted;
    private boolean useNickname;
    private FriendRequestStatus friendRequestStatus;

    public Friend(AccountInformation information)
    {
        this.setFriendAccount(information);

    }


    public AccountInformation getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(AccountInformation friendAccount) {
        this.friendAccount = friendAccount;
    }

    public boolean equals(Friend friend)
    {
        if (friendAccount.getAccountId() == friend.getFriendAccount().getAccountId())
        {
            return true;
        }
        return false;
    }


    public String getTextRepresentation()
    {
        if (isUseNickname())
        {
            if (getNickName().length() <= 0)
            {
                //Log.i(this.getClass().getName(), "Attemted to use null nickname, was auto corrected by program.");
                return getFriendAccount().getUserName();
            }
            return getNickName();
        }
        else
        {
            return getFriendAccount().getUserName();
        }

    }





    @Override
    public int compareTo(Friend o) {
        String c1 = this.getTextRepresentation();
        String c2 = o.getTextRepresentation();

        return c1.compareToIgnoreCase(c2);
    }

    @Override
    public int compare(Friend o1, Friend o2) {

        String c1 = o1.getTextRepresentation();
        String c2 = o2.getTextRepresentation();
        return c1.compareToIgnoreCase(c2);

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isUseNickname() {
        return useNickname;
    }

    public void setUseNickname(boolean useNickname) {
        this.useNickname = useNickname;
    }
}
