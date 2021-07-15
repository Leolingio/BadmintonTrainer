package com.sensolic.badmintontrainer;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sensolic.badmintontrainer.graphics.CourtDimensions;
import com.sensolic.badmintontrainer.graphics.Figure;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends AppCompatActivity {

    private static final boolean DEBUG = false;

    private Storage storage;
    private CourtDimensions dim;
    private FrameLayout mainLayout;
    private ImageView court;
    private ImageView ownPlayer;
    private ImageView enemyPlayer;
    private ImageView featherBall;
    private ImageView selectedItem;
    private Figure[] figures = new Figure[3];
    private int scorePlayer1;
    private int scorePlayer2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_start);

        storage = new Storage(getApplicationContext());

        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);

        System.out.println(Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }
        else{
            mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        ownPlayer = (ImageView) findViewById(R.id.ownPlayer);
        enemyPlayer = (ImageView) findViewById(R.id.enemyPlayer);
        featherBall = (ImageView) findViewById(R.id.ball);

        court = (ImageView) findViewById(R.id.court);
        figures[0] = new Figure(Figure.Types.OWN, ownPlayer);
        figures[1] = new Figure(Figure.Types.ENEMY, enemyPlayer);
        figures[2] = new Figure(Figure.Types.BALL, featherBall);

        court.setOnTouchListener(onTouchListener());
        for(Figure f : figures){
            f.getImg().setOnTouchListener(onTouchListener());
        }

        ownPlayer.setOnTouchListener(onTouchListener());
        enemyPlayer.setOnTouchListener(onTouchListener());
        featherBall.setOnTouchListener(onTouchListener());

        Button b1 = (Button) findViewById(R.id.newPosButton);
        Button b2 = (Button) findViewById(R.id.setStartPos);
        b1.setEnabled(false);
        b2.setEnabled(false);
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);

        ownPlayer.setVisibility(View.INVISIBLE);
        enemyPlayer.setVisibility(View.INVISIBLE);
        featherBall.setVisibility(View.INVISIBLE);

        scorePlayer1 = 0;
        scorePlayer2 = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void exitTraining(View view) {
        finish();
    }

    int counter = 0;

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                //Debug Code
                TextView debugText = findViewById(R.id.debugTextView);
                String msg;

                //Getting Raw event coordinates
                final int xPos = (int) event.getRawX();
                final int yPos = (int) event.getRawY();
                msg = "x-Coordinates: "+xPos+" y-Coordinates"+yPos;
                if(Settings.debugMode) debugText.setText(msg);

                //Creating float array for 2 dimensional coordinates
                float[] pos = new float[2];
                pos[0] = xPos;
                pos[1] = yPos;

                //Refreshing the dimensions of the court
                dim.initializeDimensions();

                //If the touch-coordinates are not inside the court, the character will not be moved
                if(!dim.isPartOfCourt(pos)) return false;

                //If court is clicked the selected item will not change -> will be placed to that point
                //If a player or the ball is clicked, selectedItem will be changed
                if(view.equals(court)){
                    if(selectedItem == null && !newPosActivated){
                        return false;
                    }
                }
                else{
                    selectedItem = (ImageView) view;
                    refreshSelectedItem();
                }

                //Option if new Start Positions should be selected
                if(newPosActivated && Settings.manualStartPos){
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {

                            if (counter == 0) {
                                pos = dim.getPosOnCenter(ownPlayer, pos);
                                storage.storePos("firstOwnPlayer", pos);
                                setupStartPositions(ownPlayer);
                                counter++;
                                Toast.makeText(getApplicationContext(), "Ok, now the Enemy Player", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (counter == 1) {
                                pos = dim.getPosOnCenter(enemyPlayer, pos);
                                storage.storePos("firstEnemyPlayer", pos);
                                setupStartPositions(enemyPlayer);
                                counter++;
                                Toast.makeText(getApplicationContext(), "Ok, now the Ball", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (counter == 2) {
                                pos = dim.getPosOnCenter(featherBall, pos);
                                storage.storePos("featherBall", pos);
                                counter++;
                                newPosActivated = false;
                                setupStartPositions(featherBall);
                                counter = -1;
                                findViewById(R.id.newPosButton).setClickable(true);
                                Toast.makeText(getApplicationContext(), "Successfully created new start positions", Toast.LENGTH_LONG).show();
                                return true;
                            }

                    }
                    else return true;
                }
                else if(counter == -1){
                    counter = 0;
                    return true;
                }

                //Changing the coordinates, so that the image is positioned relative to its center
                if(selectedItem == null) return false;
                pos = dim.getPosOnCenter(selectedItem, pos);

                //Refresh position of selected item
                selectedItem.setX(pos[0]);
                selectedItem.setY(pos[1]);

                mainLayout.invalidate();
                return true;
            }
        };
    }

    private void defaultPositions(){
        float[] pos = new float[2];

        // Calculation coordinates of own player
        pos[0] = (float) (dim.getCourtCoordinates()[0][0] + (0.55*(dim.getCourtWidth())/2));
        pos[1] = (float) (dim.getCourtCoordinates()[0][1] + (0.55*dim.getCourtHeight()));

        ownPlayer.setX((float) pos[0]);
        ownPlayer.setY((float) pos[1]);

        // Calculation coordinates of ball
        pos[0] = (float) (dim.getCourtCoordinates()[0][0] + (0.6*(dim.getCourtWidth())/2));
        pos[1] = (float) (dim.getCourtCoordinates()[0][1] + (0.50*dim.getCourtHeight()));

        featherBall.setX((float) pos[0]);
        featherBall.setY((float) pos[1]);

        // Calculation coordinates of enemy player
        pos[0] = (float) (dim.getCourtCoordinates()[0][0] + (0.5*(dim.getCourtWidth())/2) + (dim.getCourtWidth())/2.0);
        pos[1] = (float) (dim.getCourtCoordinates()[0][1] + (0.3*dim.getCourtHeight()));

        enemyPlayer.setX((float) pos[0]);
        enemyPlayer.setY((float) pos[1]);

        mainLayout.invalidate();
    }

    /**
     * This method refreshes the positions.xml of all characters, based on the current score and serve
     *
     * @param ownScore      Enter here how much your own score is
     * @param opponentScore Enter here how much your enemy's score is
     * @param ownServe      True if you have the current serve
     */
    private void refreshPositions(int ownScore, int opponentScore, boolean ownServe) {
        if (ownServe) {
            if (ownScore % 2 == 0) ; //TODO Own Score is even -> Own Serve from the right side
            else ;                  //TODO Own Score is odd -> Own Serve from the left side
        } else {
            if (opponentScore % 2 == 0)
                ; //TODO Opponent's Score is even -> Opponent's Serve from the right side
            else
                ;                      //TODO Opponent's Score is odd -> Opponent's Serve from the left side
        }
    }

    /**
     * This method refreshes the selectedItemShower
     */
    private void refreshSelectedItem(){
        ImageView viewer = findViewById(R.id.selectedItemShower);
        viewer.setImageDrawable(selectedItem.getDrawable());
        viewer.setVisibility(View.VISIBLE);
    }

    /**
     * This method resets the selectedItemViewer and the selectedItem
     * @param view selectedItemViewer
     */
    public void resetSelectedItem(View view) {
        selectedItem = null;
        view.setBackground(null);
    }

    private boolean newPosActivated = false;

    public void newPositions(View view) {
        if(Settings.manualStartPos){
            view.setClickable(false);
            newPosActivated = true;
            counter = 0;
            storage.resetFile(false);
            Toast.makeText(this, "Tap the Position of the own player", Toast.LENGTH_SHORT).show();
        }
        else{
            storage.resetFile(false);
            int[] i = new int[2];
            ownPlayer.getLocationOnScreen(i);
            float[] pos = new float[2];
            pos[0] = i[0];
            pos[1] = i[1];
            storage.storePos("firstOwnPlayer",pos);

            enemyPlayer.getLocationOnScreen(i);
            pos[0] = i[0];
            pos[1] = i[1];
            storage.storePos("firstEnemyPlayer",pos);

            featherBall.getLocationOnScreen(i);
            pos[0] = i[0];
            pos[1] = i[1];
            storage.storePos("featherBall",pos);

            Toast.makeText(this, "Successfully save current positions", Toast.LENGTH_LONG).show();
        }
        mainLayout.invalidate();
    }

    public void setupStartPositions(View view){

        if(!Storage.isSaved){
            Toast.makeText(getApplicationContext(), "Es wurde noch keine Start Position gespeichert", Toast.LENGTH_LONG).show();
            return;
        }

        float[] savedPos = storage.getPos("firstOwnPlayer");
        if(savedPos != null) {
            ownPlayer.setX(savedPos[0]);
            ownPlayer.setY(savedPos[1]);
        }
        savedPos = storage.getPos("firstEnemyPlayer");
        if(savedPos != null) {
            enemyPlayer.setX(savedPos[0]);
            enemyPlayer.setY(savedPos[1]);
        }
        savedPos = storage.getPos("featherBall");
        if(savedPos != null) {
            featherBall.setX(savedPos[0]);
            featherBall.setY(savedPos[1]);
        }
        mainLayout.invalidate();
    }

    public void beginTraining(View view){
        ConstraintLayout score = (ConstraintLayout) findViewById(R.id.score);
        score.setVisibility(View.VISIBLE);

        dim = new CourtDimensions(court, getResources());
        if(Storage.isSaved){
            setupStartPositions(view);
        } else defaultPositions();

        Button b1 = (Button) findViewById(R.id.newPosButton);
        Button b2 = (Button) findViewById(R.id.setStartPos);
        Button b3 = (Button) findViewById(R.id.beginButton);
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(false);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.INVISIBLE);


        ownPlayer.setVisibility(View.VISIBLE);
        enemyPlayer.setVisibility(View.VISIBLE);
        featherBall.setVisibility(View.VISIBLE);
    }

    public void scoreIncrementPlayer1(View view){
        Button btn1 = findViewById(R.id.score_sub_player1);
        if(scorePlayer1 == 0) btn1.setEnabled(true);

        TextView score = (TextView) findViewById(R.id.score_player1);
        if(scorePlayer1 != 30) scorePlayer1++;
            score.setText(scorePlayer1+"");

        Button btn2 = findViewById(R.id.score_add_player1);
        if(scorePlayer1 == 30) btn2.setEnabled(false);
    }

    public void scoreDecrementPlayer1(View view){
        Button btn1 = findViewById(R.id.score_sub_player1);
        if(scorePlayer1 == 1) btn1.setEnabled(false);

        TextView score = (TextView) findViewById(R.id.score_player1);
        if(scorePlayer1 != 0) scorePlayer1--;
        score.setText(scorePlayer1+"");

        Button btn2 = findViewById(R.id.score_add_player1);
        if(scorePlayer1 == 29) btn2.setEnabled(true);
    }

    public void scoreIncrementPlayer2(View view){
        Button btn1 = findViewById(R.id.score_sub_player2);
        if(scorePlayer2 == 0) btn1.setEnabled(true);

        TextView score = (TextView) findViewById(R.id.score_player2);
        if(scorePlayer2 != 30) scorePlayer2++;
        score.setText(scorePlayer2+"");

        Button btn2 = findViewById(R.id.score_add_player2);
        if(scorePlayer2 == 30) btn2.setEnabled(false);
    }

    public void scoreDecrementPlayer2(View view){
        Button btn1 = findViewById(R.id.score_sub_player2);
        if(scorePlayer2 == 1) btn1.setEnabled(false);

        TextView score = (TextView) findViewById(R.id.score_player2);
        if(scorePlayer2 != 0) scorePlayer2--;
        score.setText(scorePlayer2+"");

        Button btn2 = findViewById(R.id.score_add_player2);
        if(scorePlayer2 == 29) btn2.setEnabled(true);
    }
}
