package me.ns.androidplayground.faceapi.face.util;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import me.ns.androidplayground.faceapi.face.FaceGraphic;
import me.ns.androidplayground.faceapi.face.GraphicOverlay;

/**
 * Face Graphic Tracker
 * <p>
 * Created by shintaro.nosaka on 2017/05/09.
 */
public class GraphicFaceTracker extends Tracker<Face> {

    private GraphicOverlay mOverlay;

    private FaceGraphic mFaceGraphic;

    public GraphicFaceTracker(GraphicOverlay overlay) {
        mOverlay = overlay;
        mFaceGraphic = new FaceGraphic(overlay);
    }

    @Override
    public void onNewItem(int faceId, Face item) {
        mFaceGraphic.setId(faceId);
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detectionResults, Face face) {
        mOverlay.add(mFaceGraphic);
        mFaceGraphic.updateFace(face);
    }

    @Override
    public void onMissing(Detector.Detections<Face> detectionResults) {
        mOverlay.remove(mFaceGraphic);
    }

    @Override
    public void onDone() {
        mOverlay.remove(mFaceGraphic);
    }

}