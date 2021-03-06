package com.example.doctorsearchapp;

import static com.example.doctorsearchapp.MainActivity.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.example.doctorsearchapp.fragments.ComposeFragment;
import com.example.doctorsearchapp.fragments.DetailFragment;
import com.example.doctorsearchapp.models.Doctor;
import com.example.doctorsearchapp.models.Users;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.concurrent.Callable;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder> {

    private Context context;
    Doctor doctor;
    String address;
    Users user;

    // Bundle test
    public HeaderAdapter(Context context, Doctor currentDoctor, ParseUser currUser)
    {
        this.context = context;
        doctor = currentDoctor;
        this.user = new Users(currUser);
    }

    // Inflate the view and return the HeaderViewHolder
    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_layout,parent,false);
        return new HeaderViewHolder(view);
    }

    // Bind information to the Header
    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }



    class HeaderViewHolder extends  RecyclerView.ViewHolder
    {
        private TextView nameTV;
        private TextView addressTV;
        private Button reviewBtn;
        private Button searchBtn;
        private Button favoritesBtn;
        private RatingBar rbDoctorRating;
        private ImageView businessPicIV;
        private String address;
        private List<Doctor> allDoctors;

        // Bundle test

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            reviewBtn = itemView.findViewById(R.id.reviewBtn);
            searchBtn = itemView.findViewById(R.id.btnSearch);
            favoritesBtn = itemView.findViewById(R.id.favoritesBtn);
            rbDoctorRating = itemView.findViewById(R.id.rbDoctorRating);
            businessPicIV = itemView.findViewById(R.id.businessPicIV);

            nameTV.setText(doctor.getDoctorName());
            addressTV.setText(doctor.getLocation());
            doctor.setRating(rbDoctorRating);
            Glide.with(context).load(doctor.getImage().getUrl()).into(businessPicIV);

            if(user.getFavorites().contains(doctor.getObjectId()))
            {
                ((CheckBox)favoritesBtn).setChecked(true);
            }
            else
            {
                ((CheckBox)favoritesBtn).setChecked(false);
            }

            reviewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get doctor object from DetailFragment
                    // Send doctor object to ComposeFragment
                    ComposeFragment composeFragment = new ComposeFragment();
                    Bundle b = new Bundle();
                    b.putParcelable("doctor", doctor);
                    composeFragment.setArguments(b);

                    MainActivity activity = (MainActivity) context;
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,composeFragment).commit();
                }
            });

            searchBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public  void onClick(View view){

                    Uri gmIntentUri =  Uri.parse("geo:0,0?q=" + doctor.getLocation());

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    context.startActivity(mapIntent);

                }
            });

            favoritesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CheckBox) view).isChecked())
                    {
                        Toast.makeText(context, "Adding to favorites", Toast.LENGTH_SHORT).show();
                        user.addFavorite(doctor.getObjectId());
                    }
                    else
                    {
                        Toast.makeText(context, "Removing from favorites", Toast.LENGTH_SHORT).show();
                        user.removeFavorite(doctor.getObjectId());
                    }
                }
            });
        }
    }

}
