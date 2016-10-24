package zitopay.com.texiaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*final WaveProgressbar waveProgressbar = (WaveProgressbar) findViewById(R.id.wv);
        new Thread(new Runnable() {

            @Override
            public void run() {
                SystemClock.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waveProgressbar.dismss();

                    }
                });
            }
        }).start();*/
    }

}
