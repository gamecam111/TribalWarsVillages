package eu.gamecam.tribalwarsvillages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Loggss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggss);

        LayoutInflater lf = getLayoutInflater();
        LinearLayout layout = (LinearLayout) findViewById(R.id.id_layour);

        for (int i = 0; i < Menu.all_logs.size(); i++) {
            View v = lf.inflate(R.layout.log_cell, layout, false);
            final TextView txt = (TextView) v.findViewById(R.id.textView);
            final ImageView img = (ImageView) v.findViewById(R.id.imageView2);
            if (Menu.all_logs.get(i).isResult()) img.setImageDrawable(Menu.ok);
            else img.setImageDrawable(Menu.fail);
            txt.setText("Time: " + Menu.all_logs.get(i).getTime());
            layout.addView(v, i);
        }

    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
