package com.umkc.eric.atlasbox;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Toast;


/**
 * Created by eric on 4/15/16.
 */
public class ScoreCardCreatorController {
    final int MIN_ROUND_SCORE = 6;
    private Context SCContext;
    private Scorecard scorecard;
    private Fight fight;
    int selectedRound;
    boolean read_only;
    ScoreCardCreator SCUI;

    ScoreCardCreatorController (Context context){
        SCContext = context;
        SCUI = ((ScoreCardCreator) context);
        SCUI.mKnockoutButton
                .setOnClickListener(this.listener);
        scorecard = new Scorecard();
        updateBoutScores();
    }
    public void setReadOnly(boolean b){read_only = b;}


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.scoreKnockoutButton:
                    Toast.makeText(SCContext,
                            "I AM A BANANA",
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.scoreSubmitButton:submitScorecard(SCContext); break;
                case R.id.f1r1Button:decrementRoundScore(1, 1); break;
                case R.id.f1r2Button:decrementRoundScore(2, 1); break;
                case R.id.f1r3Button:decrementRoundScore(3, 1); break;
                case R.id.f1r4Button:decrementRoundScore(4, 1); break;
                case R.id.f1r5Button:decrementRoundScore(5, 1); break;
                case R.id.f1r6Button:decrementRoundScore(6, 1); break;
                case R.id.f1r7Button:decrementRoundScore(7, 1); break;
                case R.id.f1r8Button:decrementRoundScore(8, 1); break;
                case R.id.f1R9Button:decrementRoundScore(9, 1); break;
                case R.id.f1r10Button:decrementRoundScore(10, 1); break;
                case R.id.f1r11Button:decrementRoundScore(11, 1); break;
                case R.id.f1r12Button:decrementRoundScore(12, 1); break;
                case R.id.f2r1Button:decrementRoundScore(1, 2); break;
                case R.id.f2r2Button:decrementRoundScore(2, 2); break;
                case R.id.f2r3Button:decrementRoundScore(3, 2); break;
                case R.id.f2r4Button:decrementRoundScore(4, 2); break;
                case R.id.f2r5Button:decrementRoundScore(5, 2); break;
                case R.id.f2r6Button:decrementRoundScore(6, 2); break;
                case R.id.f2r7Button:decrementRoundScore(7, 2); break;
                case R.id.f2r8Button:decrementRoundScore(8, 2); break;
                case R.id.f2r9Button:decrementRoundScore(9, 2); break;
                case R.id.f2r10Button:decrementRoundScore(10, 2); break;
                case R.id.f2r11Button:decrementRoundScore(11, 2); break;
                case R.id.f2r12Button:decrementRoundScore(12, 2); break;
            }
        }
    };

    public void setScoreCardDetails(Fight fight){
        scorecard.setNumRounds(fight.numRounds);
        scorecard.setFightID(fight.fightID);
    }

    private void decrementRoundScore(Integer roundNumber, Integer fighter) {
            selectRound(roundNumber);
            scorecard.decrementRoundScore(roundNumber,fighter);
            updateBoutScores();
        }

    private void selectRound(Integer round) {
        //Only available in slightly later SDKs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //reset color of previously selected round
            if (selectedRound != 0) {
                SCUI.RoundButtonsFighter1[selectedRound - 1]
                        .setBackgroundTintList
                                (SCUI.getResources()
                                        .getColorStateList(R.color.roundRed));
                SCUI.RoundButtonsFighter2[selectedRound - 1]
                        .setBackgroundTintList
                                (SCUI.getResources()
                                        .getColorStateList(R.color.roundBlue));

            }
            //update selected round
            selectedRound = round;
            SCUI.RoundButtonsFighter1[selectedRound - 1]
                    .setBackgroundTintList(SCUI.getResources().getColorStateList(R.color.roundRedFocus));
            SCUI.RoundButtonsFighter2[selectedRound - 1]
                    .setBackgroundTintList(SCUI.getResources().getColorStateList(R.color.roundBlueFocus));

        }
        ((ScoreCardCreator) SCContext).mRoundNumber.setText(round.toString());
        selectedRound = round;
    }

    private void updateBoutScores(){
        //TODO implement logic to update round scores in view from Scorecard model
        for (int i = 0; i < scorecard.getNumRounds(); i++){
            int f1score = scorecard.getRoundScore(1, i);
            int f2score = scorecard.getRoundScore(2, i);

            if(f1score == 0){
                SCUI.RoundButtonsFighter1[i].setText("-");
            }else
                SCUI.RoundButtonsFighter1[i].setText(Integer.toString(f1score));
            if(f2score == 0){
                SCUI.RoundButtonsFighter2[i].setText("-");
            }
            else
                SCUI.RoundButtonsFighter2[i].setText(Integer.toString(f2score));
        }
        SCUI.fighter1Score.setText
                (Integer.toString(scorecard.getFighterTotal(1)));
        SCUI.fighter2Score.setText
                (Integer.toString(scorecard.getFighterTotal(2)));
    }

    public void submitScorecard(Context context){
        //build request string
        StringBuilder url = new StringBuilder();
        //start with basic server url
        url.append(ApplicationState.getInstance().getServerURL());
        //add script name
        url.append("create_scorecard.php?uID=");
        //add user ID
        url.append(Integer
                .toString(scorecard.getUserID()));
        //continue with fight ID
        url.append("&fID=" + Integer.toString(scorecard.getFightID()));
        //numrounds comes next
        url.append("&numRounds=" + Integer.toString(scorecard.getNumRounds()));
        //fighter totals
        url.append("&f1tot=" + Integer.toString(scorecard.getFighterTotal(1)));
        url.append("&f2tot=" + Integer.toString(scorecard.getFightID()));
        //NOW THE ROUNDS
        for (int i = 0; i < scorecard.getNumRounds(); i++){
            url.append("&f1r" + Integer.toString(i+1) + "="
                    + Integer.toString(scorecard.getRoundScore(1, i)));
            url.append("&f2r" + Integer.toString(i+1) + "="
                    + Integer.toString(scorecard.getRoundScore(1, i)));
        }
        String URL = url.toString();
        //ship it off to the req Q controller
        RequestQueueController.getContext(context).submitScorecard(URL, context);

    }
}

