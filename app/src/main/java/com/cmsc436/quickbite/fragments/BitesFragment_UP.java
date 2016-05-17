package com.cmsc436.quickbite.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmsc436.quickbite.Bite;
import com.cmsc436.quickbite.CheckIn;
import com.cmsc436.quickbite.MyApplication;
import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class BitesFragment_UP extends Fragment {
    private Firebase fb = new Firebase("https://quick-bite.firebaseio.com/").child("bites");
    private User user;
    private ArrayList<Bite> bites = new ArrayList<Bite>();
    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);

        user = ((MyApplication) this.getActivity().getApplication()).getCurrentUser();
        fb.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bites.clear();
                for (DataSnapshot biteSnap : dataSnapshot.getChildren()) {
                    Bite bite = biteSnap.getValue(Bite.class);
                    bites.add(0, bite);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                builder.setTitle("Profile Error");
                builder.setMessage(firebaseError.getMessage())
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_userprofile_bites, container, false);
    }
}
