package pl.akademiakodu.calculator;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0;
    Button dodaj, odejmij, razy, dziel;
    Button wynik, czysc;
    TextView wynikText;

    List<String> numbers = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
    List<String> dzialania = Arrays.asList("-", "/", "*", "+");
    StringBuilder numberOne;
    StringBuilder numberTwo;

    String dzialanie;

    ProgressBar progress;

    private float wynikNumber;

    private boolean showResult;
    private boolean isCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberOne = new StringBuilder();
        numberTwo = new StringBuilder();

        dzialanie = "";

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setIndeterminate(true);
        progress.setVisibility(View.GONE);

        b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.b2);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.b3);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.b4);
        b4.setOnClickListener(this);
        b5 = (Button) findViewById(R.id.b5);
        b5.setOnClickListener(this);
        b6 = (Button) findViewById(R.id.b6);
        b6.setOnClickListener(this);
        b7 = (Button) findViewById(R.id.b7);
        b7.setOnClickListener(this);
        b8 = (Button) findViewById(R.id.b8);
        b8.setOnClickListener(this);
        b9 = (Button) findViewById(R.id.b9);
        b9.setOnClickListener(this);
        b0 = (Button) findViewById(R.id.b0);
        b0.setOnClickListener(this);

        dodaj = (Button) findViewById(R.id.dodaj);
        dodaj.setOnClickListener(this);
        odejmij = (Button) findViewById(R.id.odejmij);
        odejmij.setOnClickListener(this);
        razy = (Button) findViewById(R.id.razy);
        razy.setOnClickListener(this);
        dziel = (Button) findViewById(R.id.dziel);
        dziel.setOnClickListener(this);

        wynik = (Button) findViewById(R.id.button15);
        wynik.setOnClickListener(this);

        czysc = (Button) findViewById(R.id.clean);
        czysc.setOnClickListener(this);

        wynikText = (TextView) findViewById(R.id.textWynik);
    }

    private void addNumber(String number){
        if(showResult){
            showResult = false;
            czysc();
        }
       if(dzialanie == null || dzialanie.equals("")) {
          numberOne.append(number);
       }else{
           numberTwo.append(number);
       }
        pokazDzialanie();
    }




    private  boolean sprawdz() {
        if(numberOne.equals("")){
            Toast.makeText(this, "Uzupełnij pierwszą liczbę", Toast.LENGTH_SHORT).show();

            return false;
        }
        if(numberTwo.equals("")){
            Toast.makeText(this, "Uzupełnij drugą liczbę", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dzialanie.equals("")){
            Toast.makeText(this, "Uzupełnij znak działania", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(numberTwo.equals("0")){
            Toast.makeText(this, "Nie dziel przez 0!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    private boolean oblicz(){


        float number1 = Float.valueOf(numberOne.toString());
        float number2 = Float.valueOf(numberTwo.toString());

        switch (dzialanie){
            case "/": wynikNumber = number1 / number2; break;
            case "*": wynikNumber = number1 * number2; break;
            case "+": wynikNumber = number1 + number2; break;
            case "-": wynikNumber = number1 - number2; break;
        }
        return true;
    }

    private void pokazWynik(float liczba){

        wynikText.setText(pokazDzialanie() + " = " + liczba);
        showResult = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(showResult) {
                    czysc();
                }
            }
        }, 8000);

        }

    private void czysc() {
        numberOne = new StringBuilder();
        numberTwo = new StringBuilder();
        dzialanie = "";
        wynikText.setText("Brak");
    }

    private String pokazDzialanie() {
        wynikText.setText(numberOne.toString() + " " + dzialanie + " " + numberTwo.toString());
        return numberOne.toString() + " " + dzialanie + " " + numberTwo.toString();

    }


    @Override
    public void onClick(View v) {

             Button b = (Button) v;
             if(numbers.contains(b.getText())){
                 addNumber(b.getText().toString());
             }else if(dzialania.contains(b.getText())){
                 dzialanie = b.getText().toString();
                 pokazDzialanie();
                 if(showResult){
                     showResult = false;
                     czysc();
                 }
             }else{
                 if(v.getId() == wynik.getId()){
                     new CalTask().execute();
                 }else if(v.getId() == czysc.getId()){
                     czysc();
                 }
             }
    }

    private class CalTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            if(isCorrect){
                oblicz();
            }
            for(int i = 0; i <= 3; i++){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            progress.setVisibility(View.GONE);
            pokazWynik(wynikNumber);
        }

        @Override
        protected void onPreExecute() {
            isCorrect = sprawdz();
            progress.setVisibility(View.VISIBLE);
        }
    }
}
