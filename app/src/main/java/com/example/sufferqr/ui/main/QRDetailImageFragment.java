package com.example.sufferqr.ui.main;


import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Bundle;


import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;

import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sufferqr.R;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

/**
 * showing surrounding image and settings
 */
public class QRDetailImageFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    TextView t_info;
    SwitchMaterial imgEnable;
    Bundle myImageBundle;
    CardView qr_card;
    String mode;
    Uri imageUri;
    ImageButton qrbt;
    View gbview;

    /**
     * launch class
     * @param adapterImageBundle saveinstance state
     */
    public QRDetailImageFragment(Bundle adapterImageBundle) {
        myImageBundle = adapterImageBundle;
        mode = myImageBundle.getString("mode");
        if (mode.length()<1){
            mode="new";
        }
        if (mode.equals("new")){
            String imageU = myImageBundle.getString("QRpath");
            imageUri = Uri.parse(imageU);
        }
    }

    /**
     * lister sync to QRdetail tab
     */
    public interface OnFragmentInteractionListener{
        void onImageUpdate(Boolean imageOn);
    }

    /**
     * launch
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * attach to listener
     * @param context sync listener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * launch
     * @return view
     * @see com.example.sufferqr.QRDetailActivity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_r_detail_image, container, false);
        gbview = view;
        imgEnable = view.findViewById(R.id.qr_detail_image_enable_switch);
        qr_card = view.findViewById(R.id.qr_detail_image_qrimage_cardview);
        qrbt = view.findViewById(R.id.qr_detail_image_qrimage_button);
        t_info= view.findViewById(R.id.qr_detail_image_privacy_text);

        if (Objects.equals(mode, "new")) {
            listener.onImageUpdate(imgEnable.isChecked());
        }

        // image buton listener(privict)
        imgEnable.setOnClickListener(v -> {
                listener.onImageUpdate(imgEnable.isChecked());
        });

        // load image at new mode
        if (Objects.equals(mode, "new")){
            Drawable yourDrawable;
            try {
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
                yourDrawable = Drawable.createFromStream(inputStream, imageUri.toString() );
                qrbt.setBackground(yourDrawable);
            } catch (FileNotFoundException e) {
                Toast.makeText(requireContext(), "load image error", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    /**
     * data callback refresh ui
     * @param iView view info
     * @param userName11 user name
     * @param data hashmap from firebase
     */
    public void ActivityCallBack(View iView, String userName11,HashMap<String, Object> data){
        // general page change load info



        // image page chage load info
        SwitchMaterial imgEnable;
        imgEnable = iView.findViewById(R.id.qr_detail_image_enable_switch);
        Boolean imgE = (Boolean) data.get("imageExist");
        CardView c2= iView.findViewById(R.id.qr_detail_image_qrimage_cardview);
        TextView t1= iView.findViewById(R.id.qr_detail_image_privacy_text);

        if (Boolean.FALSE.equals(imgE)){
            // if not the creator disble change option
            imgEnable.setChecked(false);
            imgEnable.setEnabled(false);

            t1.setVisibility(View.INVISIBLE);
            c2.setVisibility(View.INVISIBLE);
        } else {
            // since image exist load content
            c2.setVisibility(View.VISIBLE);
            imgEnable.setChecked(true);
            if (userName11.equals((String) data.get("user"))){
                imgEnable.setEnabled(true);
                t1.setVisibility(View.VISIBLE);
            } else {
                imgEnable.setEnabled(false);
                t1.setVisibility(View.INVISIBLE);
            }
            // conect firebase storage
            imageFetchFirestone(iView,(String) data.get("QRpath"));
        }
    }

    /**
     * petching existing image
     */
    private void imageFetchFirestone(View iView,String FilePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(FilePath);
        final long ONE_MEGABYTE = 1024 * 1024; //1mb
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Drawable image = null;
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            image = Drawable.createFromStream(is, "QR code surrounding");
            ImageButton qrbt = iView.findViewById(R.id.qr_detail_image_qrimage_button);
            qrbt.setBackground(image);
        }).addOnFailureListener(exception -> {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
            ImageButton qrbt = iView.findViewById(R.id.qr_detail_image_qrimage_button);
            qrbt.setBackground(null);
        });
    }
}