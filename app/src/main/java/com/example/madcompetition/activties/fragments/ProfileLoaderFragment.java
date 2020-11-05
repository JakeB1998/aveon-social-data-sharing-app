package com.example.madcompetition.activties.fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.backend.account.AccountInformation;
import com.example.madcompetition.backend.utils.BitmapUtils;
import com.example.madcompetition.backend.utils.Data;
import com.example.madcompetition.backend.utils.FileType;
import com.example.madcompetition.backend.utils.KeyboardUtils;
import com.example.madcompetition.ProfileSelectionActivity;
import com.example.madcompetition.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static com.example.madcompetition.activties.LoginScreenActivity.PICK_IMAGE_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ProfileLoaderFragment extends Fragment {

    private FrameLayout mFragmentView;
    private Button logoutBtn;
    private ImageButton backBtn;
    private ImageButton settingsBtn;
    private ImageView profileImage;
    private TextView usernameText;

    private boolean isHost;
    private AccountInformation profileInfo;
    private FragmentDestroyedCallback callback;

    public ProfileLoaderFragment(AccountInformation information, FragmentDestroyedCallback callback) {
        // Required empty public constructor
        profileInfo = information;
        this.callback = callback;
        if (AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation().equals(information)) {
            isHost = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeyboardUtils.hideKeyboard(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = (FrameLayout) inflater.inflate(R.layout.fragment_profile_loader, container, false);
        profileImage = mFragmentView.findViewById(R.id.ProfilePicture);
        usernameText = mFragmentView.findViewById(R.id.UsernameText);
        logoutBtn = mFragmentView.findViewById(R.id.LogOutBtn);
        usernameText.setText(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation().getUserName());
        backBtn = mFragmentView.findViewById(R.id.BackBtn);
        settingsBtn = mFragmentView.findViewById(R.id.SettingsBtn);
        if (AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap() == null) {
            //profileImage.setImageBitmap( BitmapFactory.decodeResource(getContext().getResources(),
            // R.mipmap.default_profile_picture));
        } else {
            profileImage.setImageBitmap(AppManager.getInstance().getCurrentAccountLoggedIn().getProfileBitmap());
        }

        settingsBtn.setVisibility(View.VISIBLE);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onCall();
                }
                getFragmentManager().beginTransaction().remove(ProfileLoaderFragment.this).commit();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(ProfileLoaderFragment.this).commit();
            }
        });

        if (isHost) {
            this.loadHostProfile();
        } else {
            this.loadNonHostProfile();
        }

        return mFragmentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (callback != null) {
            callback.onFragmentDestroyed(this);
        }

    }

    private void pickPicture(Uri uri, String fileType, int recCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(fileType);

        if (uri != null) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        }

        startActivityForResult(intent, recCode);

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        final Data tData = new Data();
        Log.e(this.getClass().getName() + "-Result", "Redcode :" + reqCode);


        if (resultCode == RESULT_OK && reqCode == PICK_IMAGE_REQUEST_CODE) {
            if (data != null) {
                final Uri imageUri = data.getData();
                Bitmap bitmap = null;
                // Open a specific media item using InputStream.
                ContentResolver resolver = getActivity().getApplicationContext()
                        .getContentResolver();
                try (InputStream stream = resolver.openInputStream(imageUri)) {
                    bitmap = BitmapFactory.decodeStream(stream);
                    Bitmap croppedBitmap = BitmapUtils.getCroppedBitmap(bitmap);
                    if (croppedBitmap != null) {
                        bitmap = croppedBitmap;
                        croppedBitmap = null;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    profileImage.setImageBitmap(bitmap);
                    Log.i(this.getActivity().getClass().getName(), "Image was set to profile picture");
                    //AppManager.getInstance().getCurrentAccountLoggedIn().setProfileBitmap(bitmap, getContext());
                    AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(getContext());
                } else {
                    Log.i(this.getActivity().getClass().getName(), "Bitmap did not construct properly");
                }

            }

        } else {
            // Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            Log.e(MediaPickerFragment.class.getName(), "Result Code : " + resultCode);
            Log.e(this.getClass().getName(), "Data + " + data.getData());


        }

    }

    public void loadHostProfile() {
        if (isHost) {


            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileLoaderFragment.this.getContext());
                    builder1.setMessage("Are you sure you want to logout. All messages will be deleted.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(ProfileLoaderFragment.this.getActivity(), ProfileSelectionActivity.class));
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });

            profileImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pickPicture(null, FileType.IMAGE_TYPE, PICK_IMAGE_REQUEST_CODE);
                    return true;
                }
            });
        }

    }

    public void loadNonHostProfile() {
        mFragmentView.removeView(logoutBtn);

    }


}
