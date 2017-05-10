package me.ns.androidplayground.faceapi.face;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import me.ns.androidplayground.faceapi.face.util.GraphicFaceTracker;

/**
 * FaceTrackerFactory
 * <p>
 * Created by shintaro.nosaka on 2017/05/10.
 */
public class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {

    /**
     * {@link GraphicOverlay}
     */
    private GraphicOverlay mGraphicOverlay;

    /**
     * {@link GraphicFaceTracker}
     */
    private GraphicFaceTracker mGraphicFaceTracker;

    public GraphicFaceTrackerFactory(GraphicOverlay overlay) {
        mGraphicOverlay = overlay;
    }

    @Override
    public Tracker<Face> create(Face face) {
        mGraphicFaceTracker = new GraphicFaceTracker(mGraphicOverlay);
        return mGraphicFaceTracker;
    }

    /**
     * {@link GraphicFaceTracker}取得処理
     *
     * @return {@link GraphicFaceTracker}
     */
    public GraphicFaceTracker getGraphicFaceTracker() {
        return mGraphicFaceTracker;
    }
}