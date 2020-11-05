package com.example.madcompetition.activties.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.account.friend.Friend;
import com.example.madcompetition.backend.messaging.system.Conversation;
import com.example.madcompetition.backend.messaging.system.MessageHandler;
import com.example.madcompetition.backend.security.KeyContract;
import com.example.madcompetition.backend.utils.KeyboardUtils;
import com.example.madcompetition.backend.utils.StringUtils;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.ActivityConversationInterface;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ComposeMessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private FrameLayout fragmentView;
    private EditText input;
    private LinearLayout friendListPreview;
    private ImageButton backBtn;
    private Account currentAccount;
    private boolean friendChoosen;
    public ComposeMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeyboardUtils.hideKeyboard(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       fragmentView = (FrameLayout)inflater.inflate(R.layout.fragment_compose_message, container, false);
       input = fragmentView.findViewById(R.id.InputEditText);
       friendListPreview = fragmentView.findViewById(R.id.UserPreviewContainer);
        backBtn = fragmentView.findViewById(R.id.BackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .remove(ComposeMessageFragment.this
                        ).commit();
            }
        });

        currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();
        init();
        return fragmentView;
    }


    public void init()
    {
        final StringUtils myUtils = new StringUtils("NULL");
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String data = myUtils.getData();
                data = s.toString();
                myUtils.setData(data);
                if (count > 0) {
                    updateFriendsListPreview(s.toString());
                }
                else
                {
                    friendListPreview.removeAllViews();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void updateFriendsListPreview(String name)
    {
        Log.i(this.getClass().getName(), "Query friend list for result");
        if (currentAccount != null)
        {

            Friend[] results = currentAccount.getFRIEND_LIST().queryFriendsList(name);

            if (results != null) {
                friendListPreview.removeAllViews();

                Log.i(this.getClass().getName(), "Query to friend list count : " + results.length);
                for (int i = 0; i < results.length; i++) {

                    handleFriendListGui(results[i]);
                }
            }
        }
        else
        {
            currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();
            updateFriendsListPreview(name);
        }

    }

    public void handleFriendListGui(final Friend friend)
    {
        if (friend != null)
        {


            View view = getLayoutInflater().inflate(R.layout.my_layout_friendlist_gui,fragmentView, false);
            TextView name = view.findViewById(R.id.UsernameText);
            //name.setLayoutParams(new ViewGroup.LayoutParams(200,200));
            Log.i(this.getClass().getName(), "Name " + friend.getFriendAccount().getUserName());
            name.setText(friend.getFriendAccount().getUserName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    createConversation(friend);
                }
            });
            friendListPreview.addView(view);
            Log.i(this.getClass().getName(), "Friend guid added from results");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        currentAccount = null;
    }


    public void createConversation(Friend friend)
    {
        AccountInformation[] xx = {friend.getFriendAccount(), AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()};
        Conversation convo = MessageHandler.createConversation(xx);
        MessageHandler.addConversationToAccount(convo, AppManager.getInstance().getCurrentAccountLoggedIn());
        Intent intent = new Intent(getActivity(), ActivityConversationInterface.class);
        intent.putExtra(KeyContract.CONVERSATION_KEY, convo);

        getFragmentManager().beginTransaction().remove(this).commit();
        AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(getActivity());
        startActivity(intent);
    }

}
