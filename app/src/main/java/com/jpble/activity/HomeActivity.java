package com.jpble.activity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpble.OnItemListener;
import com.jpble.R;
import com.jpble.adapter.AssetAdapter;
import com.jpble.app.MyApplication;
import com.jpble.base.BaseFragment;
import com.jpble.bean.Sim;
import com.jpble.ble.LinkBLE;
import com.jpble.fragment.HomeFragment;
import com.jpble.fragment.MapFragment;
import com.jpble.fragment.MeFragment;
import com.jpble.fragment.SecurityFragment;
import com.jpble.fragment.WebFragment;
import com.jpble.presenter.SimPresenterImp;
import com.jpble.utils.AESUtil;
import com.jpble.utils.Constant;
import com.jpble.utils.DateUtil;
import com.jpble.utils.ToHex;
import com.jpble.utils.Toastor;
import com.jpble.view.SimView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jpble.utils.AESUtil.PHONE_KEY;
import static com.jpble.utils.Constant.ACTION_BLE_KEY_OPERATE_SUCCESSFULLY;
import static com.jpble.utils.Constant.EQUIPMENT_DISCONNECTED;
import static com.jpble.utils.Constant.GPS;
import static com.jpble.utils.Constant.SUCCESSFUL_DEVICE_CONNECTION;
import static com.jpble.utils.Constant.VIBRATION_SWITCH;
import static com.jpble.utils.DateUtil.FORMAT_ONE;
import static com.jpble.utils.ToHex.StringToHex2;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, OnItemListener, SimView {

    @BindView(R.id.home_rgrpNavigation)
    RadioGroup mainRgrpNavigation;
    @BindView(R.id.activity_main)
    DrawerLayout activityMain;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.home_rl)
    RelativeLayout homeRl;
    @BindView(R.id.my_asset_rv)
    RecyclerView myAssetRv;
    AssetAdapter adapter;
    private Fragment[] frags = new Fragment[5];
    protected BaseFragment currentFragment;
    private SecurityFragment securityFragment;
    private MapFragment mapFragment;
    Toastor toastor;
    LinkBLE linkBLE;
    // DevicePresenterImp devicePresenterImp;
    ProgressDialog progressDialog;
    SimPresenterImp simPresenterImp;
    /**
     * 搜索BLE终端
     */
    private BluetoothAdapter mBluetoothAdapter;
    Handler handler;
    // 30s 后停止扫描
    private static final long SCAN_PERIOD = 10000;
    boolean one = true;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initData();
        toastor = new Toastor(this);
        handler = new Handler();
        linkBLE = MyApplication.newInstance().getBleManager();
        mainRgrpNavigation.check(R.id.home_security);
        mainRgrpNavigation.setOnCheckedChangeListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg7));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAssetRv.setLayoutManager(layoutManager);
        adapter = new AssetAdapter(this);
        myAssetRv.setAdapter(adapter);
        adapter.setOnItemCheckListener(this);
        simPresenterImp = new SimPresenterImp(this, this);
        showFragment(2);

        initBle();
        activityMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                scanLeDevice(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                adapter.initRssi();
                scanLeDevice(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.home_riding:
                showFragment(0);
                homeRl.setVisibility(View.GONE);
                break;
            case R.id.home_trail:
                homeRl.setVisibility(View.VISIBLE);
                titleName.setText(getString(R.string.security_title2));
                showFragment(1);
                break;
            case R.id.home_security:
                showFragment(2);
                homeRl.setVisibility(View.VISIBLE);
                titleName.setText(getString(R.string.security_title));

                break;
            case R.id.home_set:
                showFragment(3);
                homeRl.setVisibility(View.GONE);
                break;
            case  R.id.home_web:
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://geo.crops-sports.com/contact/index.html");
                intent.setData(content_url);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initData() {
        if (securityFragment == null) {
            securityFragment = new SecurityFragment();
        }
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        if (!mapFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.home_frame, mapFragment).commit();
            currentFragment = mapFragment;
        }

    }

    private void showFragment(int position) {
        if (frags[position] == null) {
            frags[position] = getFrag(position);
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(), frags[position]);
    }

    @Nullable
    private Fragment getFrag(int index) {
        switch (index) {
            case 0:
                return new HomeFragment();
            case 1:
                return mapFragment;
            case 2:
                return securityFragment;
            case 3:
                return new MeFragment();
            case 4:
                return new WebFragment();
            default:
                return null;
        }
    }

    /**
     * 添加或者显示 fragment
     *
     * @param transaction
     * @param fragment
     */
    protected void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }

        if (!fragment.isAdded()) {
            // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.home_frame, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = (BaseFragment) fragment;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linkBLE.closeBle();
        simPresenterImp.unSubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initb();
        simPresenterImp.getDevice(MyApplication.newInstance().getUser().getData().getToken());
        String msg = MyApplication.newInstance().KEY + "3100";
        linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
    }

    @OnClick(R.id.home_list)
    public void onViewClicked() {
        activityMain.openDrawer(GravityCompat.START);
    }


    @Override
    public void showProgress() {
        // progressDialog.show();
    }

    @Override
    public void disimissProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void loadDataSuccess(Sim tData) {
        if (tData.getData().size()>0){
            MyApplication.newInstance().id= String.valueOf(tData.getData().get(0).getLockId());
        }
        adapter.setItems(tData.getData());
        handler.postDelayed(runnable, 300);
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Log.e("loadDataError", throwable.getMessage());
        toastor.showSingletonToast(getString(R.string.login_msg10));
    }

    @Override
    public void OnItemCheck(RecyclerView.ViewHolder viewHolder, int position, Sim.DataBean bleDevice) {
        if (MyApplication.newInstance().bindMac == null || !MyApplication.newInstance().bindMac.equals(bleDevice.getLockVo().getMac())) {
            if (adapter.getMap(bleDevice.getLockVo().getMac()) > -200) {
                progressDialog.show();
                MyApplication.newInstance().id = String.valueOf(bleDevice.getLockId());
                MyApplication.newInstance().carId = String.valueOf(bleDevice.getId());
                MyApplication.newInstance().name = bleDevice.getLockVo().getName();
                MyApplication.newInstance().deviceKey = "001104" + StringToHex2(AESUtil.aesDecrypt(bleDevice.getLockVo().getPassword(), PHONE_KEY));
                Log.e("OnItemCheck", AESUtil.aesDecrypt(bleDevice.getLockVo().getPassword(), PHONE_KEY));
                linkBLE.LinkBluetooth(bleDevice.getLockVo().getMac());
                activityMain.closeDrawer(Gravity.LEFT);
            } else {

                toastor.showSingletonToast(getString(R.string.ble_disconnect2));
            }

        }
        Intent intent = new Intent();
        intent.setAction(GPS);
        sendBroadcast(intent);
        activityMain.closeDrawer(Gravity.LEFT);
        MyApplication.newInstance().id = String.valueOf(bleDevice.getLockId());
        mainRgrpNavigation.check(R.id.home_trail);
    }

    Runnable runnable;

    private void initBle() {
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter.enable();
    }

    private void scanLeDevice(final boolean enable) {

        if (enable) {
            //搜索置顶服务BLE设备
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(notifyReceiver);
        handler.removeCallbacks(runnable);

    }

    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            if (device.getName() != null && (device.getName().toLowerCase().contains("Track".toLowerCase()))) {
                Log.e("onLeScan", device.getAddress() + " " + rssi);
                adapter.setMap(device.getAddress(), rssi);

            }
        }
    };

    private void initb() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SUCCESSFUL_DEVICE_CONNECTION);
        intentFilter.addAction(EQUIPMENT_DISCONNECTED);
        intentFilter.addAction(VIBRATION_SWITCH);
        intentFilter.addAction(ACTION_BLE_KEY_OPERATE_SUCCESSFULLY);
        registerReceiver(notifyReceiver, intentFilter);
    }

    private BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //设备
            if (SUCCESSFUL_DEVICE_CONNECTION.equals(intent.getAction())) {

                String msg = MyApplication.newInstance().KEY + "3100";
                linkBLE.write(Constant.jiami("FE", ToHex.random(), msg));
                progressDialog.dismiss();
            } else if (ACTION_BLE_KEY_OPERATE_SUCCESSFULLY.equals(intent.getAction())) {

            } else if (EQUIPMENT_DISCONNECTED.equals(intent.getAction())) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    toastor.showSingletonToast(getString(R.string.ble_disconnect));
                }

            } else if (GPS.equals(intent.getAction())) {
                if (intent.getStringExtra("gps").equals("1"))
                    mainRgrpNavigation.check(R.id.home_trail);
            }else if (VIBRATION_SWITCH.equals(intent.getAction())){
                sendNotification(DateUtil.getCurrDate(FORMAT_ONE), "Vibration was sensed with “My GEO”");
            }

        }
    };
    private void sendNotification(String title, String messageBody ) {
        Intent intent = new Intent();
        intent.setAction(GPS);
        sendBroadcast(intent);
        mainRgrpNavigation.check(R.id.home_trail);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
