package com.example.android.quizworld;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizworld.R;

import static android.R.attr.duration;
import static android.R.attr.id;
import static android.graphics.Color.GREEN;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.android.quizworld.R.id.mainscreen;
import static com.example.android.quizworld.R.id.player2;
import static java.lang.Math.floor;
import static java.lang.Math.random;


public class MainActivity extends AppCompatActivity {

    final int TOTAL_OF_ANIMALS = 3;
    final int TOTAL_OF_QUESTIONS = 5; // final is Java-esque for "const"
    final int ANSWERS_PER_QUESTION = 4;
    String[] animalArray, questionArray, answerArray;
    int currentAnimal, currentQuestion, chosenAnswerA, chosenAnswerB, scoreA, scoreB, players;
    int[] answerOrder = new int[ANSWERS_PER_QUESTION];
    int[] questionOrder = new int [TOTAL_OF_QUESTIONS];
    int[] animalOrder = new int [TOTAL_OF_ANIMALS];
    int[] imageRefs = { //store the IDs of the different images here in the same order as the questions
            R.drawable.elephant,
            R.drawable.elephanthabitat,
            R.drawable.elephanttusks,
            R.drawable.hungryelephant,
            R.drawable.elephantearsflap,
            R.drawable.zebra,
            R.drawable.zebrafood,
            R.drawable.zebrasavanna,
            R.drawable.zebrastripes,
            R.drawable.zebratamed,
            R.drawable.buffalo,
            R.drawable.buffalofood,
            R.drawable.buffalohabitat,
            R.drawable.buffalohorns,
            R.drawable.furbuffalo,

    };
    int[] buttonsA = {
            R.id.button1a,
            R.id.button2a,
            R.id.button3a,
            R.id.button4a
    };
    int[] buttonsB = {
            R.id.button1b,
            R.id.button2b,
            R.id.button3b,
            R.id.button4b
    };
    private final int SECOND_MS = 1000; // 1 Second
    private int msLeft = 0; // Entirety of waiting time, updated every second to show time
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            aSecondAfter();
        }
    };
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        animalArray = res.getStringArray(R.array.Animals);
        questionArray = res.getStringArray(R.array.Questions);
        answerArray = res.getStringArray(R.array.Answers);
        currentAnimal = -1;
        currentQuestion = -1;
        chosenAnswerA = -1; // Player A
        chosenAnswerB = -1; // Player B
        players = -1;
        scoreA = 0;
        scoreB = 0;
        setGone();

        shuffleAnimalOrder(); //DEBUGGING WORKS NOW

        // set the main activity linear layout not clickable, hide and make not clickable results

        //pickNewAnimal(); //DEBUGGED UP TO PICK NEW QUESTION, TRIGGERS GAME START
    }
    public void setGone() {
        LinearLayout gameField = (LinearLayout) findViewById(R.id.activity_main);
        gameField.setClickable(false);
        gameField.setVisibility(GONE);


    }


    public void result(View view) {
        context = getApplicationContext();
        TextView text = (TextView)findViewById(R.id.nameViewA);
        String value = text.getText().toString();
        String score_of_A = String.valueOf(scoreA);
        String score_of_B = String.valueOf(scoreB);
        TextView text2 = (TextView)findViewById(R.id.nameViewB);
        String value2 = text2.getText().toString();
        Toast.makeText(context, "Hurray! " + value + ": " + score_of_A + ", " + value2 + ": " + score_of_B, Toast.LENGTH_SHORT).show();


    }
    public void startGameOnePlayer(View view) {

        LinearLayout start = (LinearLayout) findViewById(R.id.mainscreen);
        start.setVisibility(GONE);
        start.setClickable(false);
        LinearLayout gameField = (LinearLayout) findViewById(R.id.activity_main);
        gameField.setClickable(true);
        gameField.setVisibility(VISIBLE);
        LinearLayout justOnePlayer = (LinearLayout) findViewById(R.id.player2);
        justOnePlayer.setVisibility(GONE);
        justOnePlayer.setClickable(false);
        players = 1;

        pickNewAnimal(); //DEBUGING

        //set the 2nd player area invisible but still taking place, and not clickable
        //make the mainmenu area invisible completely and not clickable

    }

    public void startGameTwoPlayers(View view) {
        LinearLayout start = (LinearLayout) findViewById(R.id.mainscreen);
        start.setVisibility(GONE);
        start.setClickable(false);
        LinearLayout gameField = (LinearLayout) findViewById(R.id.activity_main);
        gameField.setClickable(true);
        gameField.setVisibility(VISIBLE);
        players = 2;

        pickNewAnimal(); //DEBUGING

        //make the mainmenu area invisible completely and not clickable
    }

    public void rollBackTimer(int ms) {
        msLeft = ms;
        TextView counter = (TextView)findViewById(R.id.timer);
        counter.setText(String.valueOf((int)(msLeft / 1000))); // update when you know how to do this
    }

    public void resetButtons () {
        Button butt;
        for (int i= 0; i < ANSWERS_PER_QUESTION; i++) {
            butt = (Button)findViewById(buttonsA[i]);
            butt.setBackgroundColor(0xFFCCBBAA);
            butt = (Button)findViewById(buttonsB[i]);
            butt.setBackgroundColor(0xFFCCBBAA);
        }
    }

    public void button1A (View view) {
        Button butt;
        chosenAnswerA = 0;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerA) {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }
    public void button2A (View view) {
        Button butt;
        chosenAnswerA = 1;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerA) {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }
    public void button3A (View view) {
        Button butt;
        chosenAnswerA = 2;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerA) {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }

    public void button4A (View view) {
        Button butt;
        chosenAnswerA = 3;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerA) {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }

    public void button1B (View view) {
        Button butt;
        chosenAnswerB = 0;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerB) {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }

    public void button2B (View view) {
        Button butt;
        chosenAnswerB = 1;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerB) {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }

    public void button3B (View view) {
        Button butt;
        chosenAnswerB = 2;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerB) {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }

    public void button4B (View view) {
        Button butt;
        chosenAnswerB = 3;
        for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (i == chosenAnswerB) {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFFFA500); // light... orange?
            }
            else {
                butt = (Button)findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFFCCBBAA); // light... orange?
            }
        }
    }
    public void pickNewAnimal() {
        // we assume a start from a value of -1 on CurrentAnimal
        currentAnimal += 1;
        if (currentAnimal >= TOTAL_OF_ANIMALS)
            endOfTheGame(); //end of the game?
        else {
            shuffleQuestions();
            currentQuestion = -1;
            pickNewQuestion(); // CURRENTLY DEBUGGING
        }

    }

    public void pickNewQuestion() { // SURPRISINGLY IT WORKS!? ALL BUT IMG ***
        currentQuestion += 1;
        if (currentQuestion >= TOTAL_OF_QUESTIONS)
            pickNewAnimal();
        else {
            chosenAnswerA = -1; // Player A resetting choice
            chosenAnswerB = -1; // Player B resetting choice

            final int questionID = (currentAnimal * TOTAL_OF_QUESTIONS) + currentQuestion;
            ImageView img = (ImageView) findViewById(R.id.Image);
            img.setImageResource(imageRefs[questionID]); // DEBUG check out why it won't work
            TextView txt = (TextView)findViewById(R.id.question);
            txt.setText(questionArray[questionID]);
            shuffleAnswers();
            Button butt;
            for (int i = 0; i < ANSWERS_PER_QUESTION; i++) {
                butt = (Button) findViewById(buttonsA[i]);
                butt.setText(answerArray[(questionID * ANSWERS_PER_QUESTION) + answerOrder[i]]);
                butt = (Button) findViewById(buttonsB[i]);
                butt.setText(answerArray[(questionID * ANSWERS_PER_QUESTION) + answerOrder[i]]);
                // the jewel of the crown here
                // we pick currentAnimal and multiply to get to the beginning of the answers for the animal
                // we pick currentQuestion and multiply to get to the beginning of the answers for this question
                // we add whatever the question for this button is according to answerOrder
                // we use the number as index to get the text of each of the answers
                // BTW, this bit worked on the first time, BOOYAH!
            }
            if(players == 1) {
                rollBackTimer(10000);
            }
            else if (players == 2) {
                rollBackTimer(15000);
            }
            resetButtons();

            handler.postDelayed(runnable, SECOND_MS);
        }
    }

    public void shuffleAnimalOrder() { // copy of shuffleAnswers
        int index = -1, i = 0, j = 0;
        double highestValue = -1;
        double[] randomArray = new double[TOTAL_OF_ANIMALS];

        while(i < TOTAL_OF_ANIMALS) {
            randomArray[i] = random(); // generates a double >0 and <=1
            i = i + 1;
        }

        i = 0;
        while(i < TOTAL_OF_ANIMALS) { // per position in answerOrder...

            while(j < TOTAL_OF_ANIMALS) { // ... register the highest value index and set it to 0
                if (randomArray[j] > highestValue) {
                    highestValue = randomArray[j];
                    index = j;
                }
                j = j + 1;
            }

            animalOrder[i] = index; // register the highest index as next in answerOrder
            randomArray[index] = 0; // set to 0 so the next highest is found next

            index = -1; // resetting for next iteration
            highestValue = -1;
            j = 0; // resetting j for next iteration

            i = i + 1;
        }
    }

    public void shuffleQuestions() {
        int index = -1, i = 0, j = 0;
        double highestValue = -1;
        double[] randomArray = new double[TOTAL_OF_QUESTIONS - 1];
        questionOrder[0] = 0; //first question is always the first question in the list

        while(i < TOTAL_OF_QUESTIONS - 1) {
            randomArray[i] = random(); // generates a double >0 and <=1
            i = i + 1;
        }

        i = 1;
        while(i < TOTAL_OF_QUESTIONS) { // per position in answerOrder...

            while(j < (TOTAL_OF_QUESTIONS - 1)) { // ... register the highest value index and set it to 0
                if (randomArray[j] > highestValue) {
                    highestValue = randomArray[j];
                    index = j;
                }
                j = j + 1;
            }

            questionOrder[i] = index + 1; // register the highest index as next in answerOrder
            randomArray[index] = 0; // set to 0 so the next highest is found next

            index = -1; // resetting for next iteration
            highestValue = -1;
            j = 0; // resetting j for next iteration

            i = i + 1;
        }
    }

    public void shuffleAnswers() {
        int index = -1, i = 0, j = 0;
        double highestValue = -1;
        double[] randomArray = new double[ANSWERS_PER_QUESTION];

        while(i < ANSWERS_PER_QUESTION) {
            randomArray[i] = random(); // generates a double >0 and <=1
            i = i + 1;
        }

        i = 0;
        while(i < ANSWERS_PER_QUESTION) { // per position in answerOrder...

            while(j < ANSWERS_PER_QUESTION) { // ... register the highest value index and set it to 0
                if (randomArray[j] > highestValue) {
                    highestValue = randomArray[j];
                    index = j;
                }
                j = j + 1;
            }

            answerOrder[i] = index; // register the highest index as next in answerOrder
            randomArray[index] = 0; // set to 0 so the next highest is found next

            index = -1; // resetting for next iteration
            highestValue = -1;
            j = 0; // resetting j for next iteration

            i = i + 1;
        }
    }

    public void aSecondAfter() {
        msLeft = msLeft - SECOND_MS;
        TextView counter = (TextView)findViewById(R.id.timer);
        counter.setText(String.valueOf((int)(msLeft / 1000))); // update when you know how to do this
        if (msLeft <= 0) {
            timesUp();
        }
        else {
            handler.postDelayed(runnable, SECOND_MS); // wait another second with checking
        }
    }

    public void timesUp() {
        Button butt;
        TextView txt;
        for(int i = 0; i < ANSWERS_PER_QUESTION; i++) {
            if (answerOrder[i] == 0) /* if the right answer is the first one */ {
                butt = (Button) findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFF118811); //Greenish
                butt = (Button) findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFF118811); //Greenish
            }
            else{
                butt = (Button) findViewById(buttonsA[i]);
                butt.setBackgroundColor(0xFF666666); //Greyish
                butt = (Button) findViewById(buttonsB[i]);
                butt.setBackgroundColor(0xFF666666); //Greyish
            }
        }
        if (chosenAnswerA != -1 && answerOrder[chosenAnswerA] == 0) // if answered and right
        {
            scoreA = scoreA + 1;
            txt = (TextView)findViewById(R.id.ScoreA);
            txt.setText(String.valueOf(scoreA));
            // update the textview of the counter
        }
        if (chosenAnswerB != -1 && answerOrder[chosenAnswerB] == 0) // if answered and right
        {
            scoreB = scoreB + 1;
            txt = (TextView)findViewById(R.id.ScoreB);
            txt.setText(String.valueOf(scoreB));
            // update the textview of the counter
        }
        // shit out a toast
    }

    public void redBackground(View view) {
        LinearLayout mainstuff = (LinearLayout) findViewById(R.id.mainstuff);
        LinearLayout mainstuff2 = (LinearLayout) findViewById(R.id.activity_main);
        boolean checked = ((CheckBox) view).isChecked();
        if (checked)
            mainstuff.setBackgroundColor(Color.parseColor("#CC233E"));
            mainstuff2.setBackgroundColor(Color.parseColor("#CC233E"));



    }
    public void endOfTheGame() {
        context = getApplicationContext();
        Toast.makeText(context, "There are no more questions. I hope you enjoyed it!", Toast.LENGTH_SHORT).show();
    }

    public void goToNextQuestion(View view) {
        if (msLeft <= 0)
            pickNewQuestion();
        else {
            context = getApplicationContext();
            Toast.makeText(context, "This question still is unanswered, though!", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetGame(View view) { // largely a copy of onCreate
        if (msLeft <= 0) {
            currentAnimal = -1;
            currentQuestion = -1;
            chosenAnswerA = -1; // Player A
            chosenAnswerB = -1; // Player B
            players = -1;
            scoreA = 0;
            scoreB = 0;
            setGone();
            LinearLayout start = (LinearLayout) findViewById(R.id.mainscreen);
            start.setVisibility(VISIBLE);
            start.setClickable(true);
            LinearLayout justOnePlayer = (LinearLayout) findViewById(R.id.player2);
            justOnePlayer.setVisibility(VISIBLE);
            justOnePlayer.setClickable(true);
            TextView txt;
            txt = (TextView)findViewById(R.id.ScoreA); //DEBUGGING
            txt.setText(String.valueOf(scoreA));
            txt = (TextView)findViewById(R.id.ScoreB); //DEBUGGING
            txt.setText(String.valueOf(scoreB));
            shuffleAnimalOrder();
        }
        else {
            context = getApplicationContext();
            Toast.makeText(context, "This question still is unanswered, though!", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Make one of these per button:

    public void onclickerinoA0() {
        if (msLeft > 0) {
            chosenAnswerA = 0;
        }
        else
            resetGame();
    }
     */


//    int scoreA = 0;

//    public void scoredA(View view) {
//        scoreA = scoreA + 1;
//        displayA(scoreA);
//    }
//    private void displayA(int number) {
//        TextView scoreATextView = (TextView) findViewById(R.id.scoreA);
//        scoreATextView.setText("" + number);
//    }
//
//    int FoulcountA = 0;
//    public void foulsA(View view) {
//        FoulcountA = FoulcountA + 1;
//        displayFCA(FoulcountA);
//    }
//    private void displayFCA(int number) {
//        TextView FoulcountA = (TextView) findViewById(R.id.FoulcountA);
//        FoulcountA.setText("" + number);
//    }
//
//    int outA = 0;
//    public void outA(View view) {
//        outA += 1;
//        displayOutA(outA);
//    }
//    private void displayOutA(int number) {
//        TextView outCountATextView = (TextView) findViewById(R.id.outCountA);
//        outCountATextView.setText("" + number);
//    }
//    int setA = 0;
//    public void setA(View view) {
//        setA = setA + 1;
//        displaySetA(setA);
//    }
//    private void displaySetA(int number) {
//        TextView setsATextView = (TextView) findViewById(R.id.setsA);
//        setsATextView.setText("" + number);}
//
//    int setB = 0;
//    public void setB(View view) {
//        setB = setB + 1;
//        displaySetB(setB);
//    }
//    private void displaySetB(int number) {
//        TextView setsBTextView = (TextView) findViewById(R.id.setsB);
//        setsBTextView.setText("" + number);}


//
//    public void reset(View view) {
//        scoreA = 0;
//        scoreB = 0;
//        displayA(scoreA);
//        displayB(scoreB);
//        FoulcountA = 0;
//        FoulcountB = 0;
//        displayFCA(FoulcountA);
//        displayFCB(FoulcountB);
//        outA = 0;
//        outB = 0;
//        displayOutA(outA);
//        displayOutB(outB);
//        setA = 0;
//        setB = 0;
//        displaySetA(setA);
//        displaySetB(setB);
//
//    }
//
//    int scoreB = 0;
//
//    public void scoredB(View view) {
//        scoreB = scoreB + 1;
//        displayB(scoreB);
//    }
//    private void displayB(int number) {
//        TextView scoreBTextView = (TextView) findViewById(R.id.scoreB);
//        scoreBTextView.setText("" + number);
//    }
//
//    int FoulcountB = 0;
//
//    public void foulsB(View view) {
//        FoulcountB = FoulcountB + 1;
//        displayFCB(FoulcountB);
//    }
//
//    private void displayFCB(int number) {
//        TextView FoulcountBTextView = (TextView) findViewById(R.id.FoulcountB);
//        FoulcountBTextView.setText("" + number);
//    }
//    int outB = 0;
//
//    public void outB(View view) {
//        outB = outB + 1;
//        displayOutB(outB);
//    }
//
//    private void displayOutB(int number) {
//        TextView outCountBTextView = (TextView) findViewById(R.id.outCountB);
//        outCountBTextView.setText("" + number);}

    // start of code done night of 3/14/17

    //String[]

 }