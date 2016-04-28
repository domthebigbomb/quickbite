package com.cmsc436.quickbite.dataModel;

import java.util.ArrayList;
import android.location.Address;
import java.util.Date;

/**
 * Class to store local instances of User-related data
 */
public class UserVars {
    public String username;
    public int rank;
    public int points;
    public ArrayList<Address> favorites = new ArrayList<Address>();
    public ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
    public ArrayList<Bite> bites = new ArrayList<Bite>();

    public UserVars(String username, int rank, int points) {
        this.username = username;
        this.rank = rank;
        this.points = points;
    }

    public boolean addFavorite(Address address) {
        if (! favorites.contains(address)) {
            favorites.add(address);
            return true;
        }
        return false;
    }

    public boolean addCheckIn(CheckIn checkIn) {
        if (! checkIns.contains(checkIn)) {
            checkIns.add(checkIn);
            return true;
        }
        return false;
    }

    public boolean addBite(Bite bite) {
        if (!bites.contains(bite)) {
            bites.add(bite);
            return true;
        }
        return false;
    }

    // Class to represent data for a CheckIn event
    public class CheckIn {
        String name; // name of location
        Address address; // address of location
        Date date; // date checked into location

        public CheckIn(String name, Address address, Date date) {
            this.name = name;
            this.address = address;
            this.date = date;
        }
    }

    // Class to represent a review written by the user
    public class Bite {
        CheckIn checkIn;
        String contents;
        Date date;
        int upvotes, downvotes;

        public Bite(CheckIn checkIn, String contents, Date date) {
            this.checkIn = checkIn;
            this.contents = contents;
            this.date = date;
            upvotes = downvotes = 0;
        }

        public void updateUpvotes(int newCount) {
            this.upvotes = newCount;
        }

        public void updateDownvotes(int newCount) {
            this.downvotes = newCount;
        }

    }
}



