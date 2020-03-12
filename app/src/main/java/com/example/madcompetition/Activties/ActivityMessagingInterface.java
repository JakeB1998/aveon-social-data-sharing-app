package com.example.madcompetition.Activties;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.Conversation;
import com.example.madcompetition.BackEnd.LocationMessage;
import com.example.madcompetition.BackEnd.LocationData;
import com.example.madcompetition.BackEnd.Message;
import com.example.madcompetition.BackEnd.MessageHandler;
import com.example.madcompetition.R;
import com.example.madcompetition.BackEnd.TextMessage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityMessagingInterface extends AppCompatActivity
{
    private ArrayList<Conversation> conversations = new ArrayList<>(0);

    private Account loggedInAccount;
    private ViewGroup conversationDisplay;
    private LinearLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_interface);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(Color.GRAY);





        conversations = new ArrayList<>(0);
        myLayout = findViewById(R.id.ConversationContrainer);



       loadConversations(this);







       // this.loadConversations(this);
    }


    public void loadConversations(Context context)
    {
        addTestData();


        Log.i("Check",Integer.toString( AppManager.getInstance().getCurrentAccountLoggedIn().getConversations().size()));
        if (loggedInAccount.getConversations() != null) {
            conversations = loggedInAccount.getConversations();
        }
        Log.i("Debug", loggedInAccount.toString());
        conversations.trimToSize();

        Log.i("Check",Integer.toString( AppManager.getInstance().getCurrentAccountLoggedIn().getConversations().size()));

        int index = 0;
        if (conversations.size() > 0) {
            for (Conversation convo : conversations) {
                if (convo.getMessages().size() > 0) {
                    this.handleConversationUI(convo);
                }
                index++;

            }
        }

    }

    public void handleConversationUI(Conversation convo)
    {
        int index = 0;

        final Conversation convoNew = convo;
        index = conversations.lastIndexOf(convo);

        /*
        Message message = new Message(new Account("Alex", LocalDate.now()), new Account("Adam",LocalDate.now()), SerializationOperations.serializeObjectToBtyeArray(new TextMessage("I am Gay")), MessageType.TextToUser);
        byte[] x = SerializationOperations.serializeObjectToBtyeArray(message);
        Message newMessage = (Message) SerializationOperations.deserializeToObject(x);

        message.setDateSent(LocalDate.now());
        message.setTimeSent(LocalTime.now());

        TextMessage newText =  (TextMessage) SerializationOperations.deserializeToObject(newMessage.getDataPayload());

         */

/*
        Message newMessage = new TextMessage("Aids", new Account(), new Account());

        byte[] message = SerializationOperations.serializeObjectToBtyeArray(newMessage);
        TextMessage newText = (TextMessage) SerializationOperations.deserializeToObject(message);

 */


        Message m = convo.getMessages().get(convo.getMessages().size() - 1);


        int layoutHeight = 300;
        int layoutWidth = myLayout.getLayoutParams().width;



        TextView messagePreview = new TextView(this);
        messagePreview.setText(m.getMessage());
        messagePreview.setMinHeight(100);
        messagePreview.setWidth(500);
        messagePreview.setTranslationX(50f);

        messagePreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView datePreview = new TextView(this);
        datePreview.setText(m.getConversationDateDisplay());
        datePreview.setMinHeight(50);
        datePreview.setMinimumWidth(50);
        datePreview.setTranslationY(15f);

        datePreview.setPadding(0,0,0,25);
        datePreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final LinearLayout horLayout = new LinearLayout(this);
        horLayout.setOrientation(LinearLayout.HORIZONTAL);
        horLayout.setLayoutParams(new LinearLayout.LayoutParams(layoutWidth,layoutHeight));
        Drawable draw = ContextCompat.getDrawable(this,R.drawable.conversation_ui_outline);
        horLayout.setBackground(draw);
       // horLayout.setBackgroundColor(Color.argb(43,203,213,213));

        horLayout.setContentDescription(Integer.toString(index));
        horLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversation convo = conversations.get(Integer.parseInt((String)horLayout.getContentDescription()));
                Log.i("Event", "Layout was cliecked");
                loadConversationActivity(convoNew);
            }
        });




        LinearLayout newl = new LinearLayout(this);
        newl.setOrientation(LinearLayout.VERTICAL);

        newl.setLayoutParams(new LinearLayout.LayoutParams(myLayout.getLayoutParams().width,400));
        TextView text = new TextView((this));
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setHeight(300);
        text.setWidth(200);

        if (m.getSender().equals(AppManager.getInstance().getCurrentAccountLoggedIn()))
        {
            //text.setText(m.getReciever().getFullName());

        }
        else
        {
            text.setText(m.getSender().getFullName());

        }

        text.setPadding(50,100,0,0);
        text.setTextSize(20f);
        //newl.addView(text);

        ImageButton profileImage = new ImageButton(this);
        profileImage.setMinimumHeight(200);
        profileImage.setMinimumWidth(200);
        profileImage.setTranslationY(50f);
        profileImage.setBackground(getResources().getDrawable(R.mipmap.default_profile_picture, getTheme()));
        horLayout.addView(profileImage);
        horLayout.addView(text);
        horLayout.addView(messagePreview);
        newl.addView(datePreview);

        View v1 = new View(this);
        v1.setLayoutParams(new ViewGroup.LayoutParams(getApplicationContext().getResources().getDisplayMetrics().widthPixels / 2 + 100,1));
        v1.setX(getApplicationContext().getResources().getDisplayMetrics().widthPixels / 5);

        v1.setBackgroundColor(Color.BLACK);
        horLayout.addView(newl);
        myLayout.addView(horLayout);
        myLayout.addView(v1);
    }


    public void loadConversationActivity(Conversation convo)
    {
        Log.i("ID", Integer.toString(convo.getConversationID()));
        Intent intent = new Intent(this, ActivityConversationInterface.class);

            intent.putExtra("Conversation", convo );

        startActivity(intent);


    }


    public void addTestData()
    {
        // test
        Account david = new Account("David");
        loggedInAccount = AppManager.getInstance().getCurrentAccountLoggedIn();
        Conversation c = new Conversation();
        Conversation c2 = new Conversation();

        Account[] recipients = {david, loggedInAccount};
        c.setAccounts(recipients);
        TextMessage t = new TextMessage("NEEEEEEERRRRRRRRRDDDDD", loggedInAccount, c.getAccounts());
        t.setDateSent(LocalDate.now());
        t.setTimeSent(LocalTime.now());
        TextMessage t1 = new TextMessage("ass", loggedInAccount, c.getAccounts());
        t1.setDateSent(LocalDate.now());
        t1.setTimeSent(LocalTime.now());

        //test end
        Location loco = AppManager.getInstance().locationsFromLastUpdate;
        if (loco == null)
        {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                AppManager.getInstance().locationsFromLastUpdate = location;


                            }
                            else {

                                Log.i("Location", "Location is null" + "From : " + LoginScreenActivity.class.getName());
                            }
                        }
                    });
            loco = AppManager.getInstance().locationsFromLastUpdate;
        }

        LocationMessage t2 = new LocationMessage(new LocationData(), david, c.getAccounts());
        t2.setDateSent(LocalDate.now());
        t2.setTimeSent(LocalTime.now());

        Account[] recipients1 =t.getRecipeints();


        c.setAccounts(recipients);
        c.addMessage(t);
        c.addMessage(t1);
        c.addMessage(t2);

        c2.addMessage(t2);

        Account[] testRecipients = new Account[2];
        testRecipients[0] = new Account("Alice");
        testRecipients[1] = loggedInAccount;
        c2.setAccounts(testRecipients);


        if (MessageHandler.addConversationToAccount(c2, loggedInAccount) == false)
        {

        }

        if(MessageHandler.addConversationToAccount(c, loggedInAccount) == false)
        {

        }
    }











}
