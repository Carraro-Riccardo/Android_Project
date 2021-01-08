package com.example.spaceshot.Gioco;

public class CalcolatorePunteggio implements Runnable{

    public static boolean loop = true;

    int delay_punteggio = 20;
    int PUNTEGGIO = 0;
    public static int puntiPerDelay = 10;

    int defaultPuntiPerDelay = 10;

    public static boolean doublePoints = false;

    @Override
    public void run()
    {
        while (GameActivity.CONTINUE)
        {
            if(GameActivity.PAUSE == false) {
                long millis = System.currentTimeMillis();

                while (System.currentTimeMillis() - millis < delay_punteggio) {
                }

                if (doublePoints == false)
                    PUNTEGGIO += puntiPerDelay;
                else
                    PUNTEGGIO += (puntiPerDelay * 2);

                if (GameActivity.txtPunteggio != null)
                    GameActivity.txtPunteggio.setText(PUNTEGGIO + "");
            }
        }
    }

    public static void AvviaPunteggioDoppio()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int durata_effetto = 3000;
                long currentMillis = 0;
                int delta_time = 500;
                boolean changeImg = true;

                CalcolatorePunteggio.doublePoints = true;

                while(durata_effetto > 0)
                {
                    if(GameActivity.PAUSE == false) {
                        if (System.currentTimeMillis() - currentMillis > delta_time) {
                            durata_effetto -= delta_time;
                            currentMillis = System.currentTimeMillis();
                        }
                    }
                }

                CalcolatorePunteggio.doublePoints  = false;
            }
        }).start();
    }

}
