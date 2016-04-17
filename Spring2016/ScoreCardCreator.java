package com.umkc.eric.atlasbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreCardCreator extends AppCompatActivity {
    /*
    some stuff protected instead of private so the controller
    can have its way with this
     */
    boolean read_only = false;

    //fighter 1
    protected Button[] RoundButtonsFighter1 = new Button[12];
    private TextView fighter1Text;
    protected TextView fighter1Score;
    //fighter 2
    protected Button[] RoundButtonsFighter2 = new Button[12];
    private TextView fighter2Text;
    protected TextView fighter2Score;

    protected Button mSubmitButton;
    protected Button mKnockoutButton;
    protected TextView mRoundNumber;

    protected int[] fighter1scores;
    protected int[] fighter2scores;

    protected Bundle extras;

    private ScoreCardCreatorController controller;
    //holds the fight passed in via extras for setFightDetails
    private Fight fight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //standard oncreate stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card_creator);

        //arrays for the buttons
        fighter1scores = new int[12];
        fighter2scores = new int[12];
        //extras passed from starting activities
        extras = getIntent().getExtras();
        //all the findByViewID() calls in defineViews;
        defineViews();
        //start controller and give it control
        controller = new ScoreCardCreatorController(this);
        //attach buttons to onclick listener in ScoreCardCreatorController
        setListeners();

        //check to see if a "fight" extra has been passed to this activity
        if(getIntent().hasExtra("fight")) {
            fight = getIntent().getExtras().getParcelable("fight");
            controller.setScoreCardDetails(fight);
            setFightDetails();
        }
        //check for a scorecard extra here
        if(getIntent().hasExtra("scorecard")){
            //set activity to "read only"
            read_only = true;
            //Populate our view from the DB
            RequestQueueController.getContext(ScoreCardCreator.this)
            .populateScoresFromDB(fight.fightID,
                    ApplicationState.getInstance().getUserID(),
                    this);
        }


    }

    //assigns all buttons in activity to onclick listener in controller
    protected void setListeners(){
        mKnockoutButton.setOnClickListener(controller.listener);
        mSubmitButton.setOnClickListener(controller.listener);
        for (int i = 0; i < 12; i++){
            RoundButtonsFighter1[i].setOnClickListener(controller.listener);
            RoundButtonsFighter2[i].setOnClickListener(controller.listener);
        }
    }
    //grabs all of the IDs. I'm not 100% sure this is necessary anymore since
    //the listener checks for IDs.
    protected void defineViews(){
        //fighter1
        fighter1Text = (TextView)findViewById(R.id.scoreFighter1Text);
        fighter1Score = (TextView)findViewById(R.id.scoreFighter1Score);
        RoundButtonsFighter1[0] = (Button)findViewById(R.id.f1r1Button);;
        RoundButtonsFighter1[1] = (Button)findViewById(R.id.f1r2Button);
        RoundButtonsFighter1[2] = (Button)findViewById(R.id.f1r3Button);
        RoundButtonsFighter1[3] = (Button)findViewById(R.id.f1r4Button);
        RoundButtonsFighter1[4] = (Button)findViewById(R.id.f1r5Button);
        RoundButtonsFighter1[5] = (Button)findViewById(R.id.f1r6Button);
        RoundButtonsFighter1[6] = (Button)findViewById(R.id.f1r7Button);
        RoundButtonsFighter1[7] = (Button)findViewById(R.id.f1r8Button);
        RoundButtonsFighter1[8] = (Button)findViewById(R.id.f1R9Button);
        RoundButtonsFighter1[9] = (Button)findViewById(R.id.f1r10Button);
        RoundButtonsFighter1[10] = (Button)findViewById(R.id.f1r11Button);
        RoundButtonsFighter1[11] = (Button)findViewById(R.id.f1r12Button);
        //fighter2
        fighter2Text = (TextView)findViewById(R.id.scoreFighter2Text);
        fighter2Score = (TextView)findViewById(R.id.scoreFighter2Score);
        RoundButtonsFighter2[0] = (Button)findViewById(R.id.f2r1Button);
        RoundButtonsFighter2[1] = (Button)findViewById(R.id.f2r2Button);
        RoundButtonsFighter2[2] = (Button)findViewById(R.id.f2r3Button);
        RoundButtonsFighter2[3] = (Button)findViewById(R.id.f2r4Button);
        RoundButtonsFighter2[4] = (Button)findViewById(R.id.f2r5Button);
        RoundButtonsFighter2[5] = (Button)findViewById(R.id.f2r6Button);
        RoundButtonsFighter2[6] = (Button)findViewById(R.id.f2r7Button);
        RoundButtonsFighter2[7] = (Button)findViewById(R.id.f2r8Button);
        RoundButtonsFighter2[8] = (Button)findViewById(R.id.f2r9Button);
        RoundButtonsFighter2[9] = (Button)findViewById(R.id.f2r10Button);
        RoundButtonsFighter2[10] = (Button)findViewById(R.id.f2r11Button);
        RoundButtonsFighter2[11] = (Button)findViewById(R.id.f2r12Button);
        //control panel
        mKnockoutButton = (Button)findViewById(R.id.scoreKnockoutButton);
        mSubmitButton = (Button)findViewById(R.id.scoreSubmitButton);
        mRoundNumber = (TextView)findViewById(R.id.scoreRoundNumber);
    }

    //sets fight details in activity
    public void setFightDetails(){
        //check to see if fight was passed through intent correctly
        if (fight != null){
            fighter1Text.setText(fight.fighter1);
            fighter2Text.setText(fight.fighter2);

            //set appropriate round visibility
            for(int i = 11; i >= fight.numRounds; i--){
                RoundButtonsFighter1[i].setVisibility(View.INVISIBLE);
                RoundButtonsFighter2[i].setVisibility(View.INVISIBLE);
            }
        }
    }
}
