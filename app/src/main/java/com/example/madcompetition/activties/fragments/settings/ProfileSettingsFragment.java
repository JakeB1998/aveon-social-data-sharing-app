package com.example.madcompetition.activties.fragments.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.madcompetition.backend.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.backend.settings.account.AccessibilitySettings;
import com.example.madcompetition.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSettingsFragment extends Fragment {

    private int currentSelection;
    private int previousSelection;
    private AccessibilitySettings settings;

    private HashMap<Integer,View> matchedPairs;


    private final String PICTURE_TAG = "picture";
    private FrameLayout mLayout;
    private ImageButton mBackBtn;


    public ProfileSettingsFragment(FragmentDestroyedCallback fragmentDestroyedCallback) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     mLayout = (FrameLayout)  inflater.inflate(R.layout.fragment_profile_settings, container, false);
        mBackBtn = mLayout.findViewById(R.id.BackBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(ProfileSettingsFragment.this).commit();
            }
        });

        new VisibilitySetting().init();
        return mLayout;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class VisibilitySetting
    {
        private View enabledOption;
        private View disabledOption;
        private View selectedView;
        private int selectionIndex;

        private HashMap<Integer, View> selectionMap;


        public VisibilitySetting()
        {

        }

        public void init()
        {
            selectionMap = new HashMap<>(0);
            enabledOption = mLayout.findViewById(R.id.FriendsOnlyOption);
            disabledOption = mLayout.findViewById(R.id.EveryOneOption);
            selectionMap.put(1,enabledOption);
            selectionMap.put(2,disabledOption);
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
//                    settings.setSwipeFeatureEnabled(true);
                    handleGui();
                    //getFragmentManager().beginTransaction().detach(ProfileSettingsFragment.this).attach(ProfileSettingsFragment.this).commit();
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
                   // settings.setSwipeFeatureEnabled(false);
                    handleGui();
                   // getFragmentManager().beginTransaction().detach(ProfileSettingsFragment.this).attach(ProfileSettingsFragment.this).commit();
                }
            });

        }

        public void handleGui()
        {
            //getSelectedView().findViewWithTag(PICTURE_TAG).setVisibility(View.VISIBLE);
        }

        public void disableGUI()
        {
            //getSelectedView().findViewWithTag(PICTURE_TAG).setVisibility(View.INVISIBLE);
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
