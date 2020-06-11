package com.example.madcompetition.activties.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.account.friend.Friend;
import com.example.madcompetition.BackEnd.Interfaces.DataTransferCallback;
import com.example.madcompetition.BackEnd.account.friend.FriendList;
import com.example.madcompetition.BackEnd.security.Credentials;
import com.example.madcompetition.R;


import java.util.ArrayList;

public class FriendListFragment extends Fragment {
    private final static String LOG = FriendListFragment.class.getName();


    private SwipeRefreshLayout mSwipeRefresh;
    private FrameLayout myLayout;
    private LinearLayout friendListContainer;


    private Account currentAccount;

    private DataTransferCallback transferCallback;


    private LayoutInflater inflater;

    private ImageButton backBtn;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendListFragment(Account currentAccount) {
        this.currentAccount = currentAccount;
        transferCallback = null;
    }

    public FriendListFragment(Account currentAccount, DataTransferCallback callback) {
        this.currentAccount = currentAccount;
        this.transferCallback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
       mSwipeRefresh= (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_friendlistfragment_list, container, false);
       myLayout = mSwipeRefresh.findViewById(R.id.Main);
       backBtn = myLayout.findViewById(R.id.BackBtn);
       backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getFragmentManager().beginTransaction()
                       .remove(FriendListFragment.this).commit();
           }
       });
       friendListContainer = myLayout.findViewById(R.id.FriendListContainer);

       mSwipeRefresh.setOnRefreshListener(new SwipeRefreshFeature());
        return mSwipeRefresh;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadFriendsList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void loadFriendsList()
    {
        ArrayList<Friend> friends;
       // ArrayList<Friend> friends = currentAccount.getFRIEND_LIST().getFriends();
      friends = AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().getFriends();

        if (friends != null)
        {
            if(friends.size() > 0)
            {
                FriendList.sortAlphabetically(friends, friends.get(0));

                for (Friend friend: friends)
                {
                    createGUI(friend);
                }

            }
            else
            {
                Log.e(LOG, "Friend list is empty");
            }
        }
        else
        {
            Log.e(LOG, "Friend list is null");
        }


    }

    public ArrayList<Friend>addTestData()
    {
        ArrayList<Friend> friends = new ArrayList<>(0);
       Friend test =  new Friend(new AccountInformation(null,0,new Credentials("Alex","1234"),null));
        Friend test2 =  new Friend(new AccountInformation(null,0,new Credentials("Alex","1234"),null));
        Friend test3 =  new Friend(new AccountInformation(null,0,new Credentials("Alex","1234"),null));


        friends.add(test);
        test2.getFriendAccount().setUserName("ILove");
        friends.add(test2);
        test3.setUseNickname(true);
        test3.setNickName("aalex");
        friends.add(test3);
        return friends;
    }


    public void createGUI(Friend friend)
    {
        View start = inflater.inflate(R.layout.my_layout_friendlist_gui, null, false);
        ImageView img = start.findViewById(R.id.ProfilePicture);
        TextView name = start.findViewById(R.id.UsernameText);

        Log.i(this.getClass().getName(), "Name " + friend.getFriendAccount().getUserName());
        name.setText(friend.getFriendAccount().getUserName());
        if (friend.getFriendAccount().getProfilePicture() != null) {
//            img.setImageBitmap(friend.getFriendAccount().getProfilePicture());
        }
        else
        {
            Log.i(LOG, "Users doesnt have profile picture set");
        }

       // name.setText(friend.getFriendAccount().getAccountCred().getUsername());
        Bitmap x = BitmapFactory.decodeResource(getResources(), R.mipmap.default_profile_picture);

        if (start != null) {
            friendListContainer.addView(start);
            Log.i(FriendListFragment.class.getName(), "Friend gui added");
        }
    }



    /**
     * Inner class
     */
    private class SwipeRefreshFeature implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {

            Log.i(this.getClass().getName(), "Swipe called back overridden here");
            mSwipeRefresh.setRefreshing(false);
        }
    }

}
