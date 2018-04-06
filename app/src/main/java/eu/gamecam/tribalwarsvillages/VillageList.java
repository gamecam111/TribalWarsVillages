package eu.gamecam.tribalwarsvillages;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class VillageList extends AppCompatActivity {

    ArrayList<Village> all_villages;
    ArrayList<Village> all_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_list);
        all_villages= Menu.actualSawBarbarian;
        all_new= Menu.newVillagesAddedToList;

        ListView lvl = (ListView) findViewById(R.id.listview);
        lvl.setAdapter(new VillageItemsAdapter(this, all_villages,all_new));

        lvl.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skp3.divoke-kmene.sk/game.php?village=0&screen=info_village&id="+all_villages.get(position).getId()));
                startActivity(browserIntent);
            }
        });


        Button btn = (Button) findViewById(R.id.to_menu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        Menu.newVillagesAddedToList=new ArrayList<Village>();
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
