package com.example.madcompetition.activties;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madcompetition.backend.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.utils.Data;
import com.example.madcompetition.backend.account.friend.Friend;
import com.example.madcompetition.backend.security.KeyContract;
import com.example.madcompetition.backend.Interfaces.DataTransferCallback;
import com.example.madcompetition.backend.utils.MessageUtils;
import com.example.madcompetition.activties.fragments.AddFriendFragment;
import com.example.madcompetition.activties.fragments.ComposeMessageFragment;
import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.messaging.system.Conversation;
import com.example.madcompetition.backend.messaging.system.Message;
import com.example.madcompetition.backend.messaging.system.MessageHandler;
import com.example.madcompetition.backend.Interfaces.MyTimeUpdate;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.fragments.FriendListFragment;
import com.example.madcompetition.activties.fragments.ProfileLoaderFragment;

import java.util.ArrayList;

public class ActivityMessagingInterface  extends AppCompatActivity implements MyTimeUpdate, DataTransferCallback, FragmentDestroyedCallback {
    private ArrayList<Conversation> conversations = new ArrayList<>(0);

    private final String LOG = "MessagingInterfaceActivity";


    private Account loggedInAccount;
    private ViewGroup conversationDisplay;
    private LinearLayout myLayout;

    private ImageButton composeMessageBtn;
    private ImageButton friendListBtn;
    private ImageButton profileBtn;
    private ImageButton mAddFriendBtn;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(LOG, "On creacte called");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_messaging_interface);

        conversations = new ArrayList<>(0);
        myLayout = findViewById(R.id.ConversationContrainer);
        composeMessageBtn = findViewById(R.id.ComposeMessage);
        friendListBtn = findViewById(R.id.friendListBtn);
        profileBtn = findViewById(R.id.ProfileBtn);
        mAddFriendBtn = findViewById(R.id.AddFriendBtn);

        if (AppManager.getInstance().getCurrentAccountLoggedIn() != null) {
            if (AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap() != null) {
                profileBtn.setImageBitmap(AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap());
            }
        }


   mSwipeRefreshLayout = findViewById(R.id.SwipeRefresh);
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshFeature());
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load profile fragment
                final String TAG = "Proifle_Loader";


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                friendListBtn.setClickable(false);
                mAddFriendBtn.setClickable(false);
                composeMessageBtn.setClickable(false);
                Fragment test = fragmentManager.findFragmentByTag(TAG);
                if (test == null) {
                    ProfileLoaderFragment fragment = new ProfileLoaderFragment(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(), ActivityMessagingInterface.this);
                    fragmentTransaction.add(R.id.Main, fragment, TAG);
                    fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                    fragment.setExitTransition(new Slide(Gravity.RIGHT));
                    fragmentTransaction.commit();
                    Log.i(LOG, "Fragment Tag :" + fragment.getTag());
                }


            }
        });


        composeMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "Compose_Message_fragment";
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                ComposeMessageFragment fragment = new ComposeMessageFragment();
                fragmentTransaction.add(R.id.Main, fragment);
                fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                fragment.setExitTransition(new Slide(Gravity.RIGHT));
                fragmentTransaction.commit();
                Log.i(LOG,"Fragment created");

            }
        });

        friendListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "PFriend_Lisst_Fragment";

                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                    FriendListFragment fragment = new FriendListFragment(AppManager.getInstance().getCurrentAccountLoggedIn());
                    //AddFriendFragment fragment = new AddFriendFragment();

                    fragmentTransaction.add(R.id.Main, fragment, TAG);
                    fragment.setEnterTransition(new Slide(Gravity.LEFT));
                    fragment.setExitTransition(new Slide(Gravity.LEFT));

                    fragmentTransaction.commit();
                    Log.i(LOG, "Fragment created");
                }
                else
                {
                    Log.i(LOG, "Fragment already exists");
                    // replace
                }
            }
        });

        mAddFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "addFriendFragment";
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



                    AddFriendFragment fragment = new AddFriendFragment();

                    fragmentTransaction.add(R.id.Main, fragment, TAG);
                    fragment.setEnterTransition(new Slide(Gravity.LEFT));
                    fragment.setExitTransition(new Slide(Gravity.LEFT));

                    fragmentTransaction.commit();
                    Log.i(LOG, "Fragment created");
                }
                else
                {
                    Log.i(LOG, "Fragment already exists");
                    // replace
                }
            }
        });
       loadConversations(this);

    }


    public void loadConversations(Context context)
    {
      myLayout.removeAllViews();
        loggedInAccount = AppManager.getInstance().getCurrentAccountLoggedIn();
           //loggedInAccount.getConversations().clear();
         //addTestData();


        if (loggedInAccount.getConversations() != null) {
            conversations = loggedInAccount.getConversations();

            Log.i("Debug", loggedInAccount.toString());
        }

        conversations.trimToSize();
        Log.i(this.getClass().getName(),"Conversation size : " + conversations.size());



        int index = 0;
        if (conversations.size() > 0) {
            for (Conversation convo : conversations) {
                if (convo.getMessages().size() > 0) {
                    this.handleConversationUI(convo);
                }
                else
                {
                    conversations.remove(convo);
                    loggedInAccount.saveAccount(this);
                }
                index++;

            }
        }

        loggedInAccount.saveAccount(this);

    }

    public void handleConversationUI(Conversation convo)
    {
        Log.i(LOG, "Hnadinling conversation UI");
        int index = 0;
        final Conversation convoNew = convo;
        index = conversations.lastIndexOf(convo);

        Message m = convo.getMessages().get(convo.getMessages().size() - 1);
        final ConstraintLayout convoPreviewLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.conversation_perview, null, false);


        TextView messagePreview = convoPreviewLayout.findViewById(R.id.TextPreview);
        if (m.isMessageSelfDestruct() || m.isMessageSelfDestructed())
        {
            messagePreview.setText("THIS MESSAGE IS SELF DESTRUCTED");

        }
        else {
            messagePreview.setText(MessageUtils.formatMessageToPreviewStyle(m.getMessage()));
        }

        TextView datePreview = convoPreviewLayout.findViewById(R.id.DatePreview);
        datePreview.setText(m.getConversationDateDisplay());
       // horLayout.setBackgroundColor(Color.argb(43,203,213,213));
        convoPreviewLayout.setContentDescription(Integer.toString(index));
        convoPreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Event", "Layout was cliecked");
                loadConversationActivity(convoNew);
            }
        });

        convoPreviewLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AppManager.getInstance().getCurrentAccountLoggedIn().getConversations().remove(convoNew);
                return true;
            }
        });

      TextView name = convoPreviewLayout.findViewById(R.id.PreviewName);

        if (convo.getAccounts() != null)
        {
            String names = "";
            AccountInformation[] accounts = convo.getAccounts();

            for (int i = 0; i < accounts.length; i++)
            {
                if (accounts[i].equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()) == false) {

                    names += accounts[i].getTextRepresentation();

                }
            }
            name.setText(names);

        }

        myLayout.addView(convoPreviewLayout);

    }


    public void loadConversationActivity(Conversation convo)
    {
        Log.i("ID", Integer.toString(convo.getConversationID()));
        Intent intent = new Intent(this, ActivityConversationInterface.class);

            intent.putExtra(KeyContract.CONVERSATION_KEY, convo );

        startActivity(intent);


    }


    public void composeNewMessage(Conversation convo)
    {

        if (convo != null)
        {
            if(MessageHandler.addConversationToAccount(convo, loggedInAccount) == false)
            {

            }

        }
    }

    public void createConversation(Friend friend)
    {
        AccountInformation[] accountInformations = {friend.getFriendAccount(), AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()};
        createConversation(accountInformations);
    }

    public void createConversation(AccountInformation[] information)
    {
        Conversation conversation = new Conversation(information);

    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG, "On start called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG, "On stop called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG, "On resume called");
        if (AppManager.getInstance().getCurrentAccountLoggedIn() != null) {
            if (AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap() != null) {
                profileBtn.setImageBitmap(AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap());
            }
        }
        this.loadConversations(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG, "On restart called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG, "On pause called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG, "On Destroy called");
    }

    @Override
    public void update() {
        this.loadConversations(this);
    }

    @Override
    public void TransferData(Data data) {
        Data input = null;

        if (data != null)
        {
             input = data;
        }

        if (data.contains(KeyContract.FRIEND_KEY))
        {
            Friend friend = (Friend) data.getObject(KeyContract.FRIEND_KEY);
        }
    }

    @Override
    public void onCall() {

        startActivity(new Intent(this,ActivitySettingsMenu.class));
    }

    @Override
    public void onFragmentDestroyed(Fragment fragment) {

        friendListBtn.setClickable(true);
        mAddFriendBtn.setClickable(true);
        composeMessageBtn.setClickable(true);

    }


    /**
     * Inner class
     */
    private class SwipeRefreshFeature implements SwipeRefreshLayout.OnRefreshListener
    {

        @Override
        public void onRefresh ()
        {
            loadConversations(ActivityMessagingInterface.this);
            Log.i(this.getClass().getName(), "Swipe called back overridden here");
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }


}
