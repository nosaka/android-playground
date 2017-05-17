package me.ns.androidplayground.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.LogUtil;

public class MainActivity extends AppCompatActivity implements SensorEventListener, CompoundButton.OnCheckedChangeListener {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////


    @BindView(R.id.main_DrawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.main_ListView)
    ListView mListView;

    @BindView(R.id.main_NavigationContainer)
    ViewGroup mNavigationContainer;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link SensorManager}
     */
    private SensorManager mSensorManager;

    /**
     * {@link SensorEventAdapter}
     */
    private SensorEventAdapter mSensorEventAdapter;

    /**
     * {@link Sensor}一覧
     */
    private Map<Integer, Sensor> mSensors = new HashMap<>();

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mSensorEventAdapter = new SensorEventAdapter(this);
        mListView.setAdapter(mSensorEventAdapter);

        mSensorManager = getSystemService(SensorManager.class);

        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            String[] stringType = sensor.getStringType().split("\\.");
            String sensorName = stringType[stringType.length - 1];
            LogUtil.d("dynamic sensor:=" + sensorName);

            int viewId = addSensorCheckBox(sensor);
            mSensors.put(viewId, sensor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_ClearLog:
                mSensorEventAdapter.clear();
                return true;
            default:
                // 処理なし
        }
        return super.onOptionsItemSelected(item);
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    public void onSensorChanged(SensorEvent event) {
        mSensorEventAdapter.insert(event, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Sensor sensor = mSensors.get(buttonView.getId());
        if (sensor != null) {
            if (isChecked) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                mSensorManager.unregisterListener(this, sensor);
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link CheckBox}追加処理
     *
     * @param sensor {@link Sensor}
     * @return View Id
     */
    private int addSensorCheckBox(Sensor sensor) {
        int id = View.generateViewId();
        CheckBox checkBox = new CheckBox(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 16, 0, 16);
        checkBox.setText(Constants.getSensorName(sensor.getType()));
        checkBox.setId(id);
        checkBox.setChecked(false);
        checkBox.setOnCheckedChangeListener(this);
        mNavigationContainer.addView(checkBox, lp);

        return id;
    }

}
