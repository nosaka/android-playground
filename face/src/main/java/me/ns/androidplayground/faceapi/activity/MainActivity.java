package me.ns.androidplayground.faceapi.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.androidplayground.faceapi.R;
import me.ns.androidplayground.faceapi.camera.CameraSourcePreview;
import me.ns.androidplayground.faceapi.face.FaceGraphic;
import me.ns.androidplayground.faceapi.face.GraphicFaceTrackerFactory;
import me.ns.androidplayground.faceapi.face.GraphicOverlay;
import me.ns.lib.AlertUtil;
import me.ns.lib.LogUtil;

public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * リクエストコード：Google Play 利用不可エラー
     */
    private static final int REQUEST_GOOGLE_API_AVAILABILITY_ERROR = 0x001;

    /**
     * リクエストコード：パーミッション要求
     */
    private static final int REQUEST_PERMISSIONS = 0x002;


    /**
     * View / Face Typeマッピング
     */
    private static final SparseIntArray DICTIONARY_VIEW_FACE_TYPE = new SparseIntArray();

    static {
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_BottomMouthCheckBox, Landmark.BOTTOM_MOUTH);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_LeftCheekCheckBox, Landmark.LEFT_CHEEK);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_LeftEarTipCheckBox, Landmark.LEFT_EAR_TIP);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_LeftEarCheckBox, Landmark.LEFT_EAR);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_LeftEyeCheckBox, Landmark.LEFT_EYE);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_LeftMouthCheckBox, Landmark.LEFT_MOUTH);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_NoseBaseCheckBox, Landmark.NOSE_BASE);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_RightCheekCheckBox, Landmark.RIGHT_CHEEK);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_RightEarTipCheckBox, Landmark.RIGHT_EAR_TIP);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_RightEarCheckBox, Landmark.RIGHT_EAR);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_RightEyeCheckBox, Landmark.RIGHT_EYE);
        DICTIONARY_VIEW_FACE_TYPE.put(R.id.main_RightMouthCheckBox, Landmark.RIGHT_MOUTH);
    }


    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_CameraSourcePreview)
    CameraSourcePreview mCameraSourcePreview;

    @BindView(R.id.main_FaceGraphicOverlay)
    GraphicOverlay mFaceGraphicOverlay;

    @BindView(R.id.main_CameraSwitchFloatingActionButton)
    FloatingActionButton mCameraSwitchFloatingActionButton;

    @BindView(R.id.main_BottomMouthCheckBox)
    CheckBox mBottomMouthCheckBox;

    @BindView(R.id.main_LeftMouthCheckBox)
    CheckBox mLeftMouthCheckBox;

    @BindView(R.id.main_RightMouthCheckBox)
    CheckBox mRightMouthCheckBox;

    @BindView(R.id.main_NoseBaseCheckBox)
    CheckBox mNoseBaseCheckBox;

    @BindView(R.id.main_LeftEarTipCheckBox)
    CheckBox mLeftEarTipCheckBox;

    @BindView(R.id.main_LeftEarCheckBox)
    CheckBox mLeftEarCheckBox;

    @BindView(R.id.main_LeftEyeCheckBox)
    CheckBox mLeftEyeCheckBox;

    @BindView(R.id.main_LeftCheekCheckBox)
    CheckBox mLeftCheekCheckBox;

    @BindView(R.id.main_RightEarTipCheckBox)
    CheckBox mRightEarTipCheckBox;

    @BindView(R.id.main_RightEarCheckBox)
    CheckBox mRightEarCheckBox;

    @BindView(R.id.main_RightEyeCheckBox)
    CheckBox mRightEyeCheckBox;

    @BindView(R.id.main_RightCheekCheckBox)
    CheckBox mRightCheekCheckBox;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link CameraSource}
     */
    private CameraSource mCameraSource;

    /**
     * {@link GraphicFaceTrackerFactory}
     */
    private GraphicFaceTrackerFactory mGraphicFaceTrackerFactory;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mCameraSwitchFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 背面/正面カメラの切替
                switchCamera();
            }
        });

        CompoundButton.OnCheckedChangeListener landmarkCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int landmarkType = DICTIONARY_VIEW_FACE_TYPE.get(buttonView.getId());
                if (mGraphicFaceTrackerFactory != null
                        && mGraphicFaceTrackerFactory.getGraphicFaceTracker() != null
                        && mGraphicFaceTrackerFactory.getGraphicFaceTracker().getFaceGraphic() != null) {
                    FaceGraphic faceGraphic = mGraphicFaceTrackerFactory.getGraphicFaceTracker().getFaceGraphic();
                    faceGraphic.setLandmarkVisibility(landmarkType, isChecked);
                }

            }
        };

        mBottomMouthCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mLeftMouthCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mRightMouthCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mNoseBaseCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mLeftEarTipCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mLeftEarCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mLeftEyeCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mLeftCheekCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mRightEarTipCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mRightEarCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mRightEyeCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);
        mRightCheekCheckBox.setOnCheckedChangeListener(landmarkCheckedChangeListener);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Camera 許可済
            createCameraSource();
        } else {
            // Camera パーミッション要求
            requestCameraPermission();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        stopCameraSource();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        releaseCameraSource();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSIONS) {
            return;
        }

        boolean grants = true;
        for (int grantResult : grantResults) {
            grants &= grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (grants) {
            createCameraSource();
        } else {
            AlertUtil.showAlert(this, "このアプリケーションをご利用になるにはカメラ権限への許可が必要です。\nアプリケーションが終了します", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * パーミッション要求
     */
    private void requestCameraPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
            return;
        }

        Snackbar.make(mFaceGraphicOverlay, "検出にはカメラへのアクセスが必要です",
                Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
                    }
                })
                .show();
    }

    /**
     * カメラ生成処理
     */
    private void createCameraSource() {

        FaceDetector detector = buildFaceDetector();
        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            LogUtil.w("Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    /**
     * 背面/正面カメラ切替処理
     */
    private void switchCamera() {

        // 現在のカメラを解放
        releaseCameraSource();

        FaceDetector detector = buildFaceDetector();
        int facing = mCameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_FRONT
                ? CameraSource.CAMERA_FACING_BACK
                : CameraSource.CAMERA_FACING_FRONT;

        mCameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(facing)
                .setRequestedFps(30.0f)
                .build();

        // カメラを開始
        startCameraSource();
    }

    /**
     * カメラ起動処理
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, code, REQUEST_GOOGLE_API_AVAILABILITY_ERROR).show();
        }

        if (mCameraSource != null) {
            try {
                mCameraSourcePreview.start(mCameraSource, mFaceGraphicOverlay);
            } catch (IOException e) {
                LogUtil.e(e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * カメラ停止処理
     */
    private void stopCameraSource() {
        mCameraSourcePreview.stop();
    }

    /**
     * カメラ解放処理
     */
    private void releaseCameraSource() {
        mCameraSourcePreview.release();
    }

    /**
     * {@link FaceDetector}ビルド処理
     *
     * @return {@link FaceDetector}
     */
    private FaceDetector buildFaceDetector() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setMode(FaceDetector.ACCURATE_MODE)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
        mGraphicFaceTrackerFactory = new GraphicFaceTrackerFactory(mFaceGraphicOverlay);
        detector.setProcessor(new MultiProcessor.Builder<>(mGraphicFaceTrackerFactory).build());
        return detector;
    }


}