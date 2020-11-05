package com.example.madcompetition.backend.account.friend;


import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FriendList implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1568350442004727677L;
	private static final int MAX_NUMBER_OF_FRIENDS = 150;
    private ArrayList<Friend> friends;
    private ArrayList<FriendRequest> friendRequests;

    public FriendList(int initialSize)
    {
        setFriends(new ArrayList<Friend>(initialSize));
        setFriendRequests(new ArrayList<FriendRequest>(initialSize));

    }


    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }


    public void addFriend(Friend friend)
    {
        friends.clear();
        if (friend != null) {
            if (friends.contains(friend) == false) {
                friends.add(friend);
                //Log.i(this.getClass().getName(), "Friend succesffuly added, The current amount of friends on list is : " + friends.size());
                //Log.i(this.getClass().getName(), "The name of added friend is : " + AppManager.getInstance().getCurrentAccountLoggedIn().getPersonalInformation().toString());

            }
            else
            {
                
            }
        }
        else
        {
            //Log.i(this.getClass().getName(), "Friend NOT added because object is null");
        }

    }

    public void removeFriend(Friend friend)
    {

    }

    public void updateFriend(Friend friend)
    {


    }

    public Friend[] queryFriendsList(String data)
    {
        int count = 0;

        Friend[] newFriendsList = new Friend[MAX_NUMBER_OF_FRIENDS];
        Friend[] FriendsList = null;

        //Log.i(this.getClass().getName(), "The number of friends during query : " + friends.size());

        for (Friend f : friends)
        {
            if (f.getFriendAccount().getUserName().contains(data)) {
                newFriendsList[count] = f;
                count++;
            }
        }

        if (count == 0)
        {
            FriendsList = new Friend[count];
        }
        else
        {
            FriendsList = new Friend[count + 1];
            for (int i = 0; i < FriendsList.length; i++ )
            {
                FriendsList[i] = newFriendsList[i];
            }

        }


        newFriendsList = null;

            return FriendsList;

    }

  


    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(ArrayList<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public static ArrayList<Friend> sortAlphabetically(ArrayList<Friend> list, Comparator<Friend> sortParams)
    {
        list.sort(sortParams);
        //Log.i(Friend.class.getName(), "List Sorted \n"
        

        for (Friend f : list)
        {
           // Log.i(Friend.class.getName(), f.getTextRepresentation());
        }
        return list;
    }


    public Friend getFriend(AccountInformation information)
    {
        int index = this.getFriends().lastIndexOf(new Friend(information));
        if (index != -1)
        {
            return this.getFriends().get(index);
        }
        return null;
    }




}
