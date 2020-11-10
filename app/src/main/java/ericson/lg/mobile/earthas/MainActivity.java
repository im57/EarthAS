package ericson.lg.mobile.earthas;

import android.content.Intent;
import android.os.Bundle;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Confusion;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import ericson.lg.mobile.earthas.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity  {

    private long backKeyPressedTime = 0;

    private ImageButton btnBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        btnBluetooth = findViewById(R.id.btn_bluetooth);
        btnBluetooth.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

//        Confusion confusion = Confusion.builder()
//                .name("뼈다귀")
//                .type("general")
//                .build();
//
//        Amplify.DataStore.save(
//                confusion,
//                success -> Log.i("Tutorial", "Saved item: " + success.item().getName()),
//                error -> Log.e("Tutorial", "Could not save item to DataStore", error)
//        );
        Amplify.DataStore.observe(Confusion.class,
                started -> Log.i("Tutorial", "Observation began."),
                change -> Log.i("Tutorial", change.item().toString()),
                failure -> Log.e("Tutorial", "Observation failed.", failure),
                () -> Log.i("Tutorial", "Observation complete.")
        );

    }

    //back 버튼 클릭 처리
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }else{
            AppFinish();
        }
    }

    //앱종료
    public void AppFinish(){
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}