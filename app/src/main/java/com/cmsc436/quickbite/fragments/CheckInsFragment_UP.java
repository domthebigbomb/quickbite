package com.cmsc436.quickbite.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmsc436.quickbite.CheckIn;
import com.cmsc436.quickbite.MyApplication;
import com.cmsc436.quickbite.R;
import com.cmsc436.quickbite.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class CheckInsFragment_UP extends Fragment {
    private Firebase fb = new Firebase("https://quick-bite.firebaseio.com/").child("checkIns");
    private User user;
    private ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);

        user = ((MyApplication) this.getActivity().getApplication()).getCurrentUser();
        fb.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                checkIns.clear();
                for (DataSnapshot checkInSnap : dataSnapshot.getChildren()) {
                    CheckIn checkIn = checkInSnap.getValue(CheckIn.class);
                    checkIns.add(0, checkIn);
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
        return inflater.inflate(R.layout.activity_userprofile_checkins, container, false);
    }
}
