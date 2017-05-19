package me.ns.androidplayground.room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.androidplayground.room.ds.AppDao;
import me.ns.androidplayground.room.ds.AppDatabase;
import me.ns.androidplayground.room.ds.Image;
import me.ns.androidplayground.room.greendao.GreenDaoImage;
import me.ns.androidplayground.room.greendao.GreenDaoImageDao;

public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    private static int MEASURE_BULK_INS_COUNT = 100000;

    private static int MEASURE_EACH_ONE_INS_COUNT = 30000;

    private static int BULK_INS_BUFF_PROGRESS = 20000;

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_ProgressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.main_MeasureGreenDaoButton)
    TextView mMeasureGreenDaoButton;

    @BindView(R.id.main_MeasureGreenDaoResultTextView)
    TextView mMeasureGreenDaoResultTextView;

    @BindView(R.id.main_MeasureBulkGreenDaoButton)
    TextView mMeasureBulkGreenDaoButton;

    @BindView(R.id.main_MeasureBulkGreenDaoResultTextView)
    TextView mMeasureBulkGreenDaoResultTextView;

    @BindView(R.id.main_MeasureRoomButton)
    TextView mMeasureRoomButton;

    @BindView(R.id.main_MeasureRoomResultTextView)
    TextView mMeasureRoomResultTextView;

    @BindView(R.id.main_MeasureBulkRoomButton)
    TextView mMeasureBulkRoomButton;

    @BindView(R.id.main_MeasureBulkRoomResultTextView)
    TextView mMeasureBulkRoomResultTextView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    private AppDao mAppDao;

    private GreenDaoImageDao mGreenDaoImageDao;

    public interface ProgressListener {
        void onProgress(int value);
    }


    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppDao = AppDatabase.getInMemoryDatabase(MainActivity.this).model();
        mGreenDaoImageDao = new AppDaoOpenHelper(this, null).getDaoSession().getGreenDaoImageDao();

        ButterKnife.bind(this);

        mMeasureRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEnableAllButton(false);

                mAppDao.deleteImage();

                final Handler handler = new Handler();
                final ProgressListener listener = generateProgressListener(MEASURE_EACH_ONE_INS_COUNT);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        long insBegin = System.currentTimeMillis();

                        for (int i = 0; i < MEASURE_EACH_ONE_INS_COUNT; i++) {
                            Image image = new Image();
                            image.parentPath = null;
                            image.path = "path:" + Integer.toString(i);
                            mAppDao.insertImage(image);
                            listener.onProgress(i);
                        }

                        long insEnd = System.currentTimeMillis();

                        final long insDiff = insEnd - insBegin;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setEnableAllButton(true);
                                mMeasureRoomResultTextView.setText("");
                                mMeasureRoomResultTextView.append("登録処理:" + Long.toString(insDiff) + "ミリ秒");
                            }
                        });

                    }
                });
            }
        });
        mMeasureBulkRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEnableAllButton(false);

                mAppDao.deleteImage();

                final Handler handler = new Handler();
                final ProgressListener listener = generateProgressListener(MEASURE_BULK_INS_COUNT + BULK_INS_BUFF_PROGRESS);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        long insBegin = System.currentTimeMillis();

                        List<Image> images = new ArrayList<>();
                        for (int i = 0; i < MEASURE_BULK_INS_COUNT; i++) {
                            Image image = new Image();
                            image.parentPath = null;
                            image.path = "path:" + Integer.toString(i);
                            images.add(image);
                            listener.onProgress(i);
                        }

                        mAppDao.insertImage(images);

                        long insEnd = System.currentTimeMillis();

                        final long insDiff = insEnd - insBegin;

                        // ==== 取得処理

                        long getBegin = System.currentTimeMillis();

                        List<Image> all = mAppDao.allImage();

                        long getEnd = System.currentTimeMillis();
                        final long getDiff = getEnd - getBegin;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setEnableAllButton(true);
                                mMeasureBulkRoomResultTextView.setText("");
                                mMeasureBulkRoomResultTextView.append("登録処理:" + Long.toString(insDiff) + "ミリ秒");
                                mMeasureBulkRoomResultTextView.append("\n");
                                mMeasureBulkRoomResultTextView.append("取得処理:" + Long.toString(getDiff) + "ミリ秒");

                            }
                        });

                    }
                });
            }
        });
        mMeasureGreenDaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEnableAllButton(false);

                mGreenDaoImageDao.deleteAll();

                final Handler handler = new Handler();
                final ProgressListener listener = generateProgressListener(MEASURE_EACH_ONE_INS_COUNT);
                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {

                        long insBegin = System.currentTimeMillis();

                        for (int i = 0; i < MEASURE_EACH_ONE_INS_COUNT; i++) {
                            GreenDaoImage image = new GreenDaoImage();
                            image.setParentPath(null);
                            image.setPath("path:" + Integer.toString(i));
                            mGreenDaoImageDao.insert(image);
                            listener.onProgress(i);
                        }

                        long insEnd = System.currentTimeMillis();

                        final long insDiff = insEnd - insBegin;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setEnableAllButton(true);
                                mMeasureGreenDaoResultTextView.setText("");
                                mMeasureGreenDaoResultTextView.setText(Long.toString(insDiff) + "ミリ秒");
                            }
                        });
                    }
                });
            }
        });
        mMeasureBulkGreenDaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEnableAllButton(false);

                mGreenDaoImageDao.deleteAll();

                final Handler handler = new Handler();
                final ProgressListener listener = generateProgressListener(MEASURE_BULK_INS_COUNT + BULK_INS_BUFF_PROGRESS);
                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {

                        long insBegin = System.currentTimeMillis();

                        List<GreenDaoImage> images = new ArrayList<>();
                        for (int i = 0; i < MEASURE_BULK_INS_COUNT; i++) {
                            GreenDaoImage image = new GreenDaoImage();
                            image.setParentPath(null);
                            image.setPath("path:" + Integer.toString(i));
                            images.add(image);
                            listener.onProgress(i);
                        }

                        mGreenDaoImageDao.insertInTx(images);

                        long insEnd = System.currentTimeMillis();

                        final long insDiff = insEnd - insBegin;

                        // ==== 取得処理

                        long getBegin = System.currentTimeMillis();

                        List<GreenDaoImage> alls = mGreenDaoImageDao.loadAll();

                        long getEnd = System.currentTimeMillis();
                        final long getDiff = getEnd - getBegin;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setEnableAllButton(true);
                                mMeasureBulkGreenDaoResultTextView.setText("");
                                mMeasureBulkGreenDaoResultTextView.append("登録処理:" + Long.toString(insDiff) + "ミリ秒");
                                mMeasureBulkGreenDaoResultTextView.append("\n");
                                mMeasureBulkGreenDaoResultTextView.append("取得処理:" + Long.toString(getDiff) + "ミリ秒");
                            }
                        });
                    }
                });
            }
        });
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    private ProgressListener generateProgressListener(int max) {
        mProgressBar.setMax(max);
        return new ProgressListener() {
            @Override
            public void onProgress(int value) {
                mProgressBar.setProgress(value);
            }
        };
    }

    private void setEnableAllButton(boolean enabled) {
        mProgressBar.setProgress(0);
        mMeasureRoomButton.setEnabled(enabled);
        mMeasureBulkRoomButton.setEnabled(enabled);
        mMeasureGreenDaoButton.setEnabled(enabled);
        mMeasureBulkGreenDaoButton.setEnabled(enabled);
    }


}
