package com.example.madcompetition.activties.Fragments.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.BackEnd.settings.app.AppSettings;
import com.example.madcompetition.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppSettingsFragment extends Fragment {

    private FrameLayout mLayout;
    private ImageButton mBackBtn;
    private FragmentDestroyedCallback fragmentDestroyedCallback;

    public AppSettingsFragment(FragmentDestroyedCallback fragmentDestroyedCallback) {
      this.fragmentDestroyedCallback = fragmentDestroyedCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLayout = (FrameLayout)  inflater.inflate(R.layout.fragment_profile_settings, container, false);
        mBackBtn = mLayout.findViewById(R.id.BackBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AppSettingsFragment.this).commit();

            }
        });

        new SSelectionOption().init();
        return mLayout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fragmentDestroyedCallback != null)
        {
            fragmentDestroyedCallback.onFragmentDestroyed(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class SSelectionOption
    {
        private View enabledOption;
        private View disabledOption;
        private View selectedView;
        private int selectionIndex;

        private HashMap<Integer, View> selectionMap;

        public void init()
        {
            selectionMap = new HashMap<>(0);
            enabledOption = mLayout.findViewById(R.id.FriendsOnlyOption);
            disabledOption = mLayout.findViewById(R.id.EveryOneOption);
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
                    handleGui();
                    getFragmentManager().beginTransaction().detach(AppSettingsFragment.this).attach(AppSettingsFragment.this).commit();
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

                    handleGui();
                    getFragmentManager().beginTransaction().detach(AppSettingsFragment.this).attach(AppSettingsFragment.this).commit();
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
