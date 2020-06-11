package com.example.madcompetition.activties.Fragments.settings;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.settings.account.AccessibilitySettings;
import com.example.madcompetition.R;

import java.util.HashMap;
import java.util.Locale;

public class AssessibilitySettingsFragment extends Fragment {


    private FrameLayout mLayout;
    private int currentSelection;
    private int previousSelection;
    private AccessibilitySettings settings;

    private SwipeRefreshLayout swipeRefreshLayout;

    private HashMap<Integer,View> matchedPairs;

    private View englishOption;
    private View frenchOption;
    private View spanishOption;
    private View germanOption;

    private ImageButton backBtn;

    private final String PICTURE_TAG = "picture";

    public AssessibilitySettingsFragment() {
        // Required empty public constructor

        settings = AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings();
        previousSelection = 0;
    }

    public int findFirstLanguageSelection()
    {
        Locale current = null;
        if (AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings()!= null)
        {
            current = AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings().getSelectedLocale();
            if (current == null)
            {
                Log.e(getClass().getName(), "saved localed was null, however was auto coreeccted");
                current = getActivity().getResources().getConfiguration().getLocales().get(0);
            }
        }
        else {
           current = getActivity().getResources().getConfiguration().getLocales().get(0);
        }

        String x = current.getLanguage();
        Toast.makeText(getContext(), x, Toast.LENGTH_SHORT).show();
        switch (x)
        {
            case "en":
                englishOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                return 1;
            case "es":
                spanishOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                return 2;
            case "fr":
                frenchOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                return 3;
            case "de":
                germanOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                return 4;
            default:

        }

        return -1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        matchedPairs = new HashMap<>(0);


        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_assessibility_settings, container, false);
       mLayout = swipeRefreshLayout.findViewById(R.id.Main);
      englishOption =  mLayout.findViewById(R.id.FriendsOnlyOption);
       spanishOption = mLayout.findViewById(R.id.EveryOneOption);
       frenchOption = mLayout.findViewById(R.id.FrenchLanguageBtn);
       germanOption = mLayout.findViewById(R.id.GermanLanguageBtn);
       matchedPairs.put(1,englishOption);
       matchedPairs.put(2,spanishOption);
       matchedPairs.put(3,frenchOption);
       matchedPairs.put(4,germanOption);
        currentSelection = findFirstLanguageSelection();

        backBtn = mLayout.findViewById(R.id.BackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AssessibilitySettingsFragment.this).commit();
            }
        });



        if (AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings().isSwipeFeatureEnabled() == false)
        {
            swipeRefreshLayout.setEnabled(false);
        }
        else
        {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        new SwipeToRefreshOption().init();
       handleBtns();
       return swipeRefreshLayout;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public void handleBtns()
    {
        englishOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = 1;
                if (currentSelection != index)
                {
                    Log.i(this.getClass().getName(), "english CLicked");
                    previousSelection = currentSelection;
                    currentSelection = index;
                    update();
                    updateLanguage(Locale.ENGLISH.getLanguage());

                }

            }


        });
        spanishOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = 2;
                if (currentSelection != index)
                {
                    Log.i(this.getClass().getName(), "Spanish CLicked");
                    previousSelection = currentSelection;
                    currentSelection = index;
                    update();
                    updateLanguage("es");
                }

            }


        });
        frenchOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = 3;
                if (currentSelection != index)
                {
                    Log.i(this.getClass().getName(), "French CLicked");
                    previousSelection = currentSelection;
                    currentSelection = index;
                    updateLanguage(Locale.FRENCH.getLanguage());
                    update();

                }

            }


        });
        germanOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = 4;
                if (currentSelection != index)
                {
                    Log.i(this.getClass().getName(), "german CLicked");
                    previousSelection = currentSelection;
                    currentSelection = index;
                    update();
                    updateLanguage(Locale.GERMAN.getLanguage());
                }

            }


        });


    }


    public void update()
    {
        if (previousSelection >= 0)
        {
            View view = matchedPairs.get(previousSelection);
            ImageView image =  view.findViewWithTag("picture");
            image.setVisibility(View.INVISIBLE);

        }
        if (currentSelection >= 0)
        {

           View view = matchedPairs.get(currentSelection);
          ImageView image =  view.findViewWithTag("picture");
          image.setVisibility(View.VISIBLE);

            // reset check mark gui
        }

    }

    public void updateLanguage(String language)
    {
        Log.i(AssessibilitySettingsFragment.class.getName(), "Language started");
        Locale locale;
      //  Sessions session = new Sessions(context);
        //Log.e("Lan",session.getLanguage());
        locale = new Locale(language);
        Configuration config = new Configuration(this.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);

        if (AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings() == null)
        {
            AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().setAccessibilitySettings(new AccessibilitySettings(Locale.US));
            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(getContext());
        }
        AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings().setSelectedLocale(locale);
        AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(getContext());

        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());


        Log.i(this.getClass().getName(), "Language Updated");

        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

    }



    private class SwipeToRefreshOption
    {
        private View enabledOption;
        private View disabledOption;
        private View selectedView;
        private int selectionIndex;

        private HashMap<Integer, View> selectionMap;


        public SwipeToRefreshOption()
        {

        }

        public void init()
        {
            selectionMap = new HashMap<>(0);
            enabledOption = mLayout.findViewById(R.id.Enabled);
            disabledOption = mLayout.findViewById(R.id.Disabled);
            selectionMap.put(1,enabledOption);
            selectionMap.put(2,disabledOption);

            if (AppManager.getInstance().getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings().isSwipeFeatureEnabled())
            {
                enabledOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                disabledOption.findViewWithTag("picture").setVisibility(View.INVISIBLE);
            }
            else
            {
                enabledOption.findViewWithTag("picture").setVisibility(View.INVISIBLE);
                disabledOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
            }
            enabledOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int index = 1;
                    if (selectionIndex != index)
                    {
                        disableGUI();
                        enabledOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                        disabledOption.findViewWithTag("picture").setVisibility(View.INVISIBLE);
                        selectionIndex = index;
                    }
                    selectedView = enabledOption;
                    settings.setSwipeFeatureEnabled(true);
                    handleGui();
                    getFragmentManager().beginTransaction().detach(AssessibilitySettingsFragment.this).attach(AssessibilitySettingsFragment.this).commit();
                }
            });
            disabledOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int index = 2;
                    if (selectionIndex != index)
                    {
                        disableGUI();
                        enabledOption.findViewWithTag("picture").setVisibility(View.INVISIBLE);
                        disabledOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
                        selectionIndex = index;
                    }
                    selectedView = disabledOption;
                    settings.setSwipeFeatureEnabled(false);
                    handleGui();
                    getFragmentManager().beginTransaction().detach(AssessibilitySettingsFragment.this).attach(AssessibilitySettingsFragment.this).commit();
                }
            });

        }

        public void handleGui()
        {
           // enabledOption.findViewWithTag("picture").setVisibility(View.VISIBLE);
        }

        public void disableGUI()
        {
           // disabledOption.findViewWithTag("picture").setVisibility(View.INVISIBLE);
        }




        /**
         *
         * @return
         */
        public View getSelectedView()
        {
            return selectionMap.get(selectionIndex);
        }

        /**
         *
         * @param index
         * @return
         */
        public View getSelectedView(int index)
        {
            if (index > 0 && selectionMap != null) {
                if (selectionMap.containsKey(index)) {

                    return selectionMap.get(index);
                }
            }

            return null;
        }



    }





}
