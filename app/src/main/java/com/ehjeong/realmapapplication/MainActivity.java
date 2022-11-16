package com.ehjeong.realmapapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1, btn2, btn3, btn4;
    private SosFragment sosFragment;
    private MapFragment mapFragment;
    private FriendFragment friendFragment;
    private SettingFragment settingFragment;

    Context context;
    PermissionListener permissionListener = new PermissionListener(){
        @Override
        public void onPermissionGranted(){
            initView();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(context, "권한 허용을 하지 않으면 서비스를 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getBaseContext();
        checkPermissions();
    }


    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(context)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                            //android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            //android.Manifest.permission.WRITE_EXTERNAL_STORAGE // 기기, 사진, 미디어, 파일 엑세스 권한
                    })
                    .check();

        } else {
            initView();
        }
    }

    private void initView(){
        btn1 = findViewById(R.id.btnSos);
        btn2 = findViewById(R.id.btnFriend);
        btn3 = findViewById(R.id.btnMap);
        btn4 = findViewById(R.id.btnSetting);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        if(findViewById(R.id.container) != null){
            sosFragment = new SosFragment();
            sosFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.container, sosFragment).commitAllowingStateLoss();
        }
    }

    public void onClick(View v){
        sosFragment = new SosFragment();
        friendFragment = new FriendFragment();
        mapFragment = new MapFragment();
        settingFragment = new SettingFragment();

        switch(v.getId()) {
            case R.id.btnSos:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, sosFragment).commit();
                break;
            case R.id.btnFriend:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, friendFragment).commit();
                break;
            case R.id.btnMap:
                selectFragment(mapFragment);
                break;
            case R.id.btnSetting:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        //BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        //bottom_menu.setOnItemSelectedListener(item -> {});
    }


    private void selectFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}