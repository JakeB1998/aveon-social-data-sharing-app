package com.example.madcompetition.activties.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.account.friend.Friend;
import com.example.madcompetition.backend.server.AccountInformationTemporaryDataContainer;
import com.example.madcompetition.backend.server.ClientServerMessage;
import com.example.madcompetition.backend.server.ClientServerObjectRequest;
import com.example.madcompetition.backend.server.MessageType;
import com.example.madcompetition.backend.server.ObjectRequest;
import com.example.madcompetition.backend.server.ServerConnectInterface;
import com.example.madcompetition.backend.utils.KeyboardUtils;
import com.example.madcompetition.backend.utils.SerializationOperations;
import com.example.madcompetition.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendFragment extends Fragment {

private final String LOG = this.getClass().getName();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final int PROGRESS_BAR_MAX = 1;

    private FrameLayout mLayout;
    private LinearLayout userPreviewContainer;
    private ImageButton mBackBtn;
    private Button addFriendBtn;
    private ImageButton mSearchBtn;
    private EditText input;
    private ProgressBar mPorgressBar;

    private boolean taskRunning;
    private Handler mHandler;
   private Runnable mStatusChecker;
   private Runnable mTimeout;

   private AccountInformation resultsInfo;

   private Timer timer;

    public AddFriendFragment() {
        // Required empty public constructor
    }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSwipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_add_friend, container, false);
        Log.i(this.getClass().getName(), "OOGOSFOOFSD");
        mLayout = mSwipeRefreshLayout.findViewById(R.id.Main);
        userPreviewContainer = mSwipeRefreshLayout.findViewById(R.id.UserPreviewContainer);


        input = mLayout.findViewById(R.id.InputEditText);
        mBackBtn = mLayout.findViewById(R.id.BackBtn);
        mSearchBtn = mLayout.findViewById(R.id.SearchBtn);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        mTimeout = new Runnable() {
            @Override
            public void run() {
                stopRepeatingTask();
            }
        };
        mPorgressBar = mLayout.findViewById(R.id.ProgressBar);
        mPorgressBar.setVisibility(ProgressBar.INVISIBLE);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AddFriendFragment.this).commit();
            }
        });

        return  mSwipeRefreshLayout;
    }


    public void refresh()
    {
        Log.i(LOG, "App swiped to refresh");

    }

    public void search()
    {
        mPorgressBar.setVisibility(View.VISIBLE);
        mPorgressBar.setMax(PROGRESS_BAR_MAX);
        mPorgressBar.setProgress(0);
        resultsInfo = null;
        KeyboardUtils.hideKeyboard(getActivity());
        Log.i(LOG, "Searching for results");
        if (input != null)
        {
            if (input.getText().length() > 0)
            {
                Toast.makeText(getActivity(),"Seraching for user", Toast.LENGTH_LONG).show();
                String username = input.getText().toString().trim();

            ClientServerObjectRequest request = new ClientServerObjectRequest(ObjectRequest.AccountInformation, username);
            ClientServerMessage message = new ClientServerMessage( AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),null,
                    MessageType.IncomingObjectRequest, SerializationOperations.serializeObjectToBtyeArray(request));
                    ServerConnectInterface.getInstance().addClientServerMessageToQue(message);
                    startRepeatingTask(username);

            }
        }

    }

    public void startRepeatingTask(final String username)
    {
                if (mHandler == null)
                {
                    mHandler = new Handler();
                }

        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                retrieveInfo(username);
                mHandler.postDelayed(this,250);
            }
        };

        // the runnable to run on ui thread
        final Runnable rx = new Runnable() {
            @Override
            public void run() {
                retrieveInfo(username);
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               AddFriendFragment.this.getActivity().runOnUiThread(rx); // must run on ui thread from backround thread
            }
        }, 0, 250);//put here time 1000 milliseconds=1 second

            mHandler.postDelayed(mTimeout, 4000);
            Log.i(this.getClass().getName(), "Repeating task started");
    }


    /**
     * stops server polling
     */
    public void stopRepeatingTask()
    {
        if (input != null)
        {
            input.setText(null);

        }
        taskRunning = false;
       timer.cancel();

        if (mTimeout != null)
        {
            mHandler.removeCallbacks(mTimeout);
            Log.i(this.getClass().getName(), "timeout for task stopped");
        }


       // Toast.makeText(getActivity(), "No user with that username", Toast.LENGTH_LONG).show();
    }



    public void retrieveInfo(String userName)
    {
        Log.i(this.getClass().getName(), "Fetched avvount info from user");
        AccountInformationTemporaryDataContainer query = AccountInformationTemporaryDataContainer.getInstance();
        if (query.getDataContainer().size() > 0)
        {
            Log.i(this.getClass().getName(), "Name : " + query.getDataContainer().get(0).getUserName());
        }
        AccountInformation info = query.getData(userName);
        resultsInfo = info;
        if (info != null) {
            View view = getLayoutInflater().inflate(R.layout.my_layout_user_search_results, null);
            TextView text = view.findViewById(R.id.UsernameText);
            text.setText(info.getUserName());
            Log.i(this.getClass().getName(), "Info injected into query list");


            Friend[] friends = AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().queryFriendsList(userName);
            Log.i(this.getClass().getName(), "firned list result : " + friends.length);

            addFriendBtn = view.findViewById(R.id.AddBtn);
            if (friends.length > 0) {

                addFriendBtn.setText(R.string.Friends);
            }
            else {

                addFriendBtn.setText(R.string.Add);
                addFriendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (resultsInfo != null) {
                            mPorgressBar.setProgress(1);

                            if (AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().getFriend(resultsInfo) == null) {
                                AppManager.getInstance().getCurrentAccountLoggedIn().getFRIEND_LIST().addFriend(new Friend(resultsInfo));
                                AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(getContext());
                                addFriendBtn.setClickable(false);
                                addFriendBtn.setText(R.string.Added);
                            } else {
                                addFriendBtn.setClickable(false);
                                addFriendBtn.setText(R.string.Friends);
                            }
                        }
                    }
                });
            }
            stopRepeatingTask();
            mPorgressBar.setVisibility(View.INVISIBLE);

            userPreviewContainer.addView(view);
        }
    }


    public void sortFriendsList()
    {

    }



    /**
     * Inner class
     */
    private class SwipeRefreshFeature implements SwipeRefreshLayout.OnRefreshListener
    {

        @Override
        public void onRefresh ()
        {

            Log.i(this.getClass().getName(), "Swipe called back overridden here");

        }
    }


}
