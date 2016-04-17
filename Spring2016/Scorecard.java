package com.umkc.eric.atlasbox;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eric on 3/20/16.
 */

//Extra great class used by
public class Scorecard implements Parcelable {
    final private int MIN_ROUND_SCORE = 6;
    private int fightID;
    private int userID;
    private boolean read_only;
    private int numRounds = 12; //default to 12 because
    private int[] f1scores = new int[12];
    private int[] f2scores = new int[12];
    private int f1Total = 0;
    private int f2Total = 0;

    //default constructor
    public Scorecard(){}

    //meaty constructor
    public Scorecard (int fID, int uID, int numRds, int[] f1sc, int[] f2sc){
        fightID = fID;
        userID = uID;
        numRounds = numRds;
        //drag info from arrays
        for (int i = 0; i < numRounds; i++){
            f1scores[i] = f1sc[i];
            f2scores[i] = f2sc[i];
            f1Total += f1scores[i];
            f2Total += f2scores[i];
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fightID);
        dest.writeInt(userID);
        dest.writeInt(numRounds);
        for(int i = 0; i < 12; i++){
            dest.writeInt(f1scores[i]);
            dest.writeInt(f2scores[i]);
        }
        dest.writeInt(f1Total);
        dest.writeInt(f2Total);

    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {

                @Override
                public Scorecard createFromParcel(Parcel source) {
                    return new Scorecard(source);
                }

                @Override
                public Scorecard[] newArray(int size) {
                    return new Scorecard[0];
                }
            };

    //creator constructor
    public Scorecard(Parcel in){
        fightID = in.readInt();
        userID = in.readInt();
        numRounds = in.readInt();
        for(int i = 0; i < 12; i++){
            f1scores[i] = in.readInt();
            f2scores[i] = in.readInt();
        }
        f1Total = in.readInt();
        f2Total = in.readInt();
    }

    public int getRoundScore(int fid, int rd){
        if (fid == 1){
            return f1scores[rd];
        }
        if (fid == 2){
            return f2scores[rd];
        }
        return -1;
    }

    public void setRoundScore(int fighter, int round, int score){
        if(fighter == 1){
            f1scores[round] = score;
        }
        else if(fighter == 2){
            f2scores[round] = score;
        }
        else{

        }
    }

    public void setFightID (int fid) {fightID = fid;}
    public int getFightID () {return fightID;}
    public void setUserID(int uid) {userID = uid;}
    public int getUserID() {return userID;}
    public void setNumRounds(int numRds) {numRounds = numRds;}
    public int getNumRounds(){return numRounds;}
    public void setRead_only(boolean bool){read_only = bool;}
    public void setFighterTotal(int fighter, int total){
        if(fighter == 1) f1Total = total;
        else if (fighter == 2) f2Total = total;
    }
    public int getFighterTotal(int fighter){
        assert (fighter == 1 || fighter == 2) : "Scorecard.java: getFighterTotal";
        if(fighter == 1) return f1Total;
        if(fighter == 2) return f2Total;
        return 0;
    }

    public int[] getFighterScore(int fighter){
        if(fighter == 1) return f1scores;
        else if (fighter == 2) return f2scores;
        else return null;
    }

    public void decrementRoundScore(Integer roundNumber, Integer fighter) {
        //new variable to make code nicer
        int rd = roundNumber - 1;
        if (!read_only) {
            //if point is to be deducted from fighter 1
            if (fighter == 1) {
                //check if we're at the minimum round score
                if (f1scores[rd] <= MIN_ROUND_SCORE) {
                    f1scores[rd] = 10;
                    //other fighter is only at min if round is unscored
                    if (f2scores[rd] <= MIN_ROUND_SCORE) {
                        f2scores[rd] = 10;
                    }
                } else {
                    f1scores[rd]--;
                }
            }
            //if point is to be deducted from fighter 2
            else if (fighter == 2) {
                //check if we're at the minimum round score
                if (f2scores[rd] <= MIN_ROUND_SCORE) {
                    f2scores[rd] = 10;
                    //other fighter is only at min if round is unscored
                    if (f1scores[rd] <= MIN_ROUND_SCORE) {
                        f1scores[rd] = 10;
                    }
                } else {
                    f2scores[rd]--;
                }
            }
            updateTotals();
        }
    }

    private void updateTotals(){
        f1Total = 0;
        f2Total = 0;
        for(int i = 0; i < 12; i++){
            f1Total += f1scores[i];
            f2Total += f2scores[i];
        }
    }
}
