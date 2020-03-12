package com.example.madcompetition;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.madcompetition.BackEnd.Profile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ProfileInfoWindowMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    public ProfileInfoWindowMap(Context context)
    {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Object obj =  marker.getTag();
        View v = View.inflate(context, R.layout.custom_info_window, null);
        // set widget of your custom_layout like below

        if (obj != null) {
            /*
            txtResourceName.setText(detailDto.getResourceName());
            txtResourceAddress.setText(detailDto.getAddress());
            String mUrl = base_Url + detailDto.getImageUrl();
            Picasso.with(MapViewActivity.this).load(mUrl).resize(TARGET_WIDTH, TARGET_HEIGHT).centerInside().into(imageViewPic);

             */

            if (obj instanceof Profile)
            {
                Profile profile = (Profile) obj;
                TextView name = v.findViewById(R.id.ProfileName);
                TextView status = v.findViewById(R.id.ProfileStatus);

                name.setText("Sally");
                status.setText(ActivityStatus.Online.toString());

            }
        }
        return v;
    }
}
