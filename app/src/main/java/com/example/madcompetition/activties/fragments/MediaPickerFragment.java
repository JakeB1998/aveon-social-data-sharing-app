package com.example.madcompetition.activties.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.madcompetition.backend.AppManager;
import com.example.madcompetition.backend.Interfaces.DataTransferCallback;
import com.example.madcompetition.backend.messaging.system.FileMessage;
import com.example.madcompetition.backend.messaging.system.LocationMessage;
import com.example.madcompetition.backend.messaging.system.PictureMessage;
import com.example.madcompetition.backend.security.KeyContract;
import com.example.madcompetition.backend.utils.Data;
import com.example.madcompetition.backend.utils.FileUtils;
import com.example.madcompetition.backend.utils.SerializationOperations;
import com.example.madcompetition.backend.utils.StringUtils;
import com.example.madcompetition.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static com.example.madcompetition.activties.LoginScreenActivity.PICK_IMAGE_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaPickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class MediaPickerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_FILE = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

private LocationManager mLocationManager;
    private FrameLayout mylayout;
    private ImageButton sendFileBtn;
    private ImageButton sendPictureBtn;
    private ImageButton locationBtn;
    private ImageButton cancelBtn;

    private DataTransferCallback callback;

    private boolean toggle;

    int x;
    int y;

    private float startY;

    private OnFragmentInteractionListener mListener;

    public MediaPickerFragment(DataTransferCallback callback) {
        // Required empty public constructor
        this.callback = callback;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       mylayout = (FrameLayout)inflater.inflate(R.layout.fragment_media_picker, container, false);
       sendFileBtn = mylayout.findViewById(R.id.FileBtn);
       sendPictureBtn = mylayout.findViewById(R.id.PictureBtn);
       cancelBtn = mylayout.findViewById(R.id.CancelBtn);
       locationBtn = mylayout.findViewById(R.id.LocationBtn);


       cancelBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               slide();
           }
       });

       locationBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Log.i(this.getClass().getName(), "Location clicked");
              FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
               fusedLocationClient.getLastLocation()
                       .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                           @Override
                           public void onSuccess(Location location) {
                               Log.i(this.getClass().getName(), "Recieved location");
                               if (location != null) {
                                  LocationMessage location1 =  new LocationMessage(location,null,null);
                                   if (callback != null)
                                   {
                                       Data data = new Data();
                                       data.add(KeyContract.MESSAGE_KEY, location1);
                                       callback.TransferData(data);
                                       Log.i(this.getClass().getName(), "Location messagevalid");
                                   }
                               }
                           }
                       });

               getFragmentManager().beginTransaction().remove(MediaPickerFragment.this).commit();
           }
       });


       sendPictureBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               handleAddPicture();
           }
       });

        sendFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddFile();

            }
        });



        return mylayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View layout = mylayout;
        final float MAX_X = this.getResources().getDisplayMetrics().widthPixels;
        final float MAX_Y = this.getResources().getDisplayMetrics().heightPixels;
        mylayout.setPadding(25,25,10,10);

       final Resources resources = this.getResources();




        mylayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                int navHeight = -1;
                if (resourceId > 0) {
                      navHeight =  resources.getDimensionPixelSize(resourceId);
                }
                mylayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mylayout.setX((MAX_X /2) - mylayout.getWidth() / 2);
                slide();
                mylayout.setY(MAX_Y - mylayout.getHeight() - 250);
                startY = mylayout.getY();
            }
        });


        Log.i("Debug", "Layout : " + layout.getY());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public View getFragmentView()
    {
        return  mylayout;
    }


    private void handleAddFile()
    {
        this.openFileExplorer(null, "*/*", PICK_FILE);
    }

    private void handleAddPicture()
    {
        this.RequestUserToPickPicture();
    }

    private void RequestUserToPickPicture()
    {
        this.openFileExplorer(null, "image/*", PICK_IMAGE_REQUEST_CODE);
    }

    private PictureMessage handlePicture(Uri uri)
    {
        String generateId = StringUtils.generateID(8);
        try {
            final InputStream imageStream =this.getActivity().getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                return new PictureMessage(selectedImage,AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(MediaPickerFragment.class.getName(), e.getCause().toString());
            /// Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        return null;

    }

    private FileMessage handleFile(Uri uri)
    {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        Log.i(this.getClass().getName(), "File tyoe : " + type);
        File file = new File(getContext().getFilesDir(), StringUtils.generateID(8) + "." +  type);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream input = null;
        String[] mimeTpyes = {"*/*"};
        try {
            if (FileUtils.isVirtualFile(uri,getActivity())) {
                input = FileUtils.getInputStreamForVirtualFile(uri, "text/plain" , getActivity());
                Log.i(this.getClass().getName(), "file is virtual");
            } else {
                input = getActivity().getContentResolver().openInputStream(uri);
                Log.i(this.getClass().getName(), "file is not virtual");

            }
            FileOutputStream out = new FileOutputStream(file);
           byte[] data =  SerializationOperations.readBytes(input);
           out.write(data,0,data.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(this.getClass().getName(), "File Size : " + file.length());
        return new FileMessage(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation(),null,file);
    }


    private void openFileExplorer(Uri uri, String fileType, int recCode)
    {
        Intent intent = new Intent();
        if (fileType.equals("*/*"))
        {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(fileType);

        if (uri != null)
        {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        }

        startActivityForResult(intent, recCode);

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        final Data tData = new Data();



        if (resultCode == RESULT_OK && reqCode == PICK_IMAGE_REQUEST_CODE) {
            if (data != null) {
                final Uri imageUri = data.getData();
                PictureMessage message = handlePicture(imageUri);

                if (message != null) {
                    tData.add(KeyContract.MESSAGE_KEY, message);
                    if (callback != null) {
                        callback.TransferData(tData);
                    }

                }
            }


        }
        else if (resultCode == RESULT_OK && reqCode == PICK_FILE)
        {

            if (data != null) {
                final Uri uri = data.getData();
                Log.i(this.getClass().getName(), "Decoded uri : " + Uri.decode(uri.getEncodedPath()) );
                FileMessage fileMessage = handleFile(uri);
                if (fileMessage != null) {
                    tData.add(KeyContract.MESSAGE_KEY, fileMessage);

                    if (callback != null) {
                        callback.TransferData(tData);

                    }
                }
            }

        }else {
           // Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            Log.e(MediaPickerFragment.class.getName(), "Result Code : " + resultCode);
            Log.e(this.getClass().getName(), "Data + " +  data.getData());


        }
        getFragmentManager().beginTransaction()
                .remove(MediaPickerFragment.this).commit();

    }


    public void slide()
    {
        new FragmentAnimationHandler().toggleAnimation(mylayout);

    }



    public class FragmentAnimationHandler
    {
        public void slideUp(View view)
        {
            TranslateAnimation animate = new TranslateAnimation(
                    1600,                 // fromXDelta
                    0,                 // toXDelta
                    0,  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);



            Animation slide_up = AnimationUtils.loadAnimation(getContext().getApplicationContext(),
                    R.anim.slide_up);

// Start animation
            //view.startAnimation(slide_up);

        }

        public void slideDown(View view)
        {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    1600,                 // toXDelta
                    0,  // fromYDelta
                   0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
           view.startAnimation(animate);


            Animation slide_down = AnimationUtils.loadAnimation(getContext().getApplicationContext(),
                    R.anim.slide_down);
            //view.startAnimation(slide_down);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getFragmentManager().beginTransaction().remove(MediaPickerFragment.this).commit();
                }
            }, 500);


        }


        public void toggleAnimation(View view)
        {
            if (toggle)
            {
                this.slideDown(view);
                toggle = false;
            }
            else
            {
                this.slideUp(view);
                toggle = true;
            }
        }
    }







}
