package me.ns.androidplayground.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.Locale;

import me.ns.lib.LogUtil;

/**
 * Created by shintaro.nosaka on 2017/05/17.
 */

public class Constants {

    /**
     * センサー名取得
     *
     * @param type {@link Sensor#getType()}
     * @return センサー名
     */
    public static String getSensorName(int type) {
        String text = null;
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                text = "加速度センサー";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                text = "周囲温度センサー";
                break;
            case Sensor.TYPE_DEVICE_PRIVATE_BASE:
                text = "TYPE_DEVICE_PRIVATE_BASE";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                text = "回転ベクトルセンサー(磁場なし)";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                text = "地磁気回転ベクトルセンサー";
                break;
            case Sensor.TYPE_GRAVITY:
                text = "重力センサー";
                break;
            case Sensor.TYPE_GYROSCOPE:
                text = "ジャイロセンサー";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                text = "ジャイロスコープ(生データ)";
                break;
            case Sensor.TYPE_HEART_BEAT:
                text = "心拍検出";
                break;
            case Sensor.TYPE_HEART_RATE:
                text = "心拍数";
                break;
            case Sensor.TYPE_LIGHT:
                text = "照度センサー";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                text = "加速度センサー(重力を除く)";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                text = "地磁気センサー";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                text = "地磁気センサー(生データ)";
                break;
            case Sensor.TYPE_MOTION_DETECT:
                text = "動作継続検出";
                break;
            case Sensor.TYPE_ORIENTATION:
                text = "傾きセンサー";
                break;
            case Sensor.TYPE_POSE_6DOF:
                text = "拡張回転ベクトルセンサー";
                break;
            case Sensor.TYPE_PRESSURE:
                text = "気圧センサー";
                break;
            case Sensor.TYPE_PROXIMITY:
                text = "近接センサー";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                text = "相対湿度センサー";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                text = "回転ベクトルセンサー";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                text = "動作検出センサー";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                text = "歩行センサー";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                text = "歩行検出センサー";
                break;
            case Sensor.TYPE_TEMPERATURE:
                text = "温度センサー";
                break;
            default:
                LogUtil.d("★★★★", Integer.toString(type));
                text = "不明";
        }

        return text;
    }


    /**
     * データ取得
     *
     * @param event {@link SensorEvent}
     * @return データテキスト
     */
    public static String getData(SensorEvent event) {
        String text = null;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                // 加速度センサー
                text = String.format(Locale.getDefault(), "x:=%1$f, y:=%2$f, z:=%3$f",
                        event.values[0], event.values[1], event.values[2]);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                // 周囲温度センサー
                text = String.format(Locale.getDefault(), "%1$f度",
                        event.values[0]);
                break;
            case Sensor.TYPE_DEVICE_PRIVATE_BASE:
                // DEVICE_PRIVATE_BASE
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                // 回転ベクトルセンサー(磁場なし)
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                // 地磁気回転ベクトルセンサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_GRAVITY:
                // 重力センサー;
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                // ジャイロスコープ(生データ)
                text = String.format(Locale.getDefault(), "x:=%1$f, y:=%2$f, z:=%3$f",
                        event.values[0], event.values[1], event.values[2]);
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                // 心拍検出
                text = String.format(Locale.getDefault(), "x:=%1$f, y:=%2$f, z:=%3$f",
                        event.values[0], event.values[1], event.values[2]);
                break;
            case Sensor.TYPE_HEART_BEAT:
                // 心拍数
                text = String.format(Locale.getDefault(), "%1$f",
                        event.values[0]);
                break;
            case Sensor.TYPE_HEART_RATE:
                // 心拍数";
                text = String.format(Locale.getDefault(), "%1$f回",
                        event.values[0]);
                break;
            case Sensor.TYPE_LIGHT:
                // 光度";
                text = String.format(Locale.getDefault(), "%1$fルクス",
                        event.values[0]);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                // 加速度センサー(重力を除く)
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                // 地磁気センサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                // 地磁気センサー(生データ)
                text = String.format(Locale.getDefault(), "x_uncalib:=%1$f, y_uncalib:=%2$f, z_uncalib:=%3$f\nx_bias:=%4$f, y_bias:=%5$f, z_bias:=%6$f",
                        event.values[0], event.values[1], event.values[2], event.values[3], event.values[4], event.values[5]);
                break;
            case Sensor.TYPE_MOTION_DETECT:
                // 動作継続検出
                text = "FIRE";
                break;
            case Sensor.TYPE_ORIENTATION:
                // 傾きセンサー
                text = String.format(Locale.getDefault(), "%1$f, %2$f, %3$f",
                        event.values[0], event.values[1], event.values[2]);
                break;
            case Sensor.TYPE_POSE_6DOF:
                // 拡張回転ベクトルセンサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_PRESSURE:
                // 気圧センサー
                text = String.format(Locale.getDefault(), "%1$f mbar",
                        event.values[0]);
                break;
            case Sensor.TYPE_PROXIMITY:
                // 近接センサー
                text = String.format(Locale.getDefault(), "%1$f cm",
                        event.values[0]);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                // 相対湿度センサー
                text = String.format(Locale.getDefault(), "%1$f",
                        event.values[0]);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                // 回転ベクトルセンサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                // 動作検出センサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_STEP_COUNTER:
                // 歩行センサー
                text = String.format(Locale.getDefault(), "%1$f歩",
                        event.values[0]);
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                // 歩行検出センサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
            case Sensor.TYPE_TEMPERATURE:
                // 温度センサー
                for (float value : event.values) {
                    text += Float.toString(value);
                    text += ",";
                }
                break;
        }

        return text;
    }
}
