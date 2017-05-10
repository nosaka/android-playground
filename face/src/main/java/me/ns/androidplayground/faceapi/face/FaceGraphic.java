package me.ns.androidplayground.faceapi.face;
/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseBooleanArray;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;
import java.util.Locale;

import me.ns.lib.LogUtil;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
public class FaceGraphic extends GraphicOverlay.Graphic {

    /**
     * {@link Landmark}表示フラグ
     */
    private SparseBooleanArray mVisibilityLandmarks = new SparseBooleanArray();

    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;

    public FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    public void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    public void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);

        drawText(canvas, face);
        drawLandmarks(canvas, face);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }

    private void drawText(Canvas canvas, Face face) {

        float bottomText = canvas.getHeight();
        float paddingText = 50;

        float smiling = face.getIsSmilingProbability();
        float rightEyeOpen = face.getIsRightEyeOpenProbability();
        float leftEyeOpen = face.getIsLeftEyeOpenProbability();
        canvas.drawText("笑顔度: " + String.format(Locale.getDefault(), "%.2f", smiling),
                0, bottomText - paddingText * 1, mIdPaint);
        canvas.drawText("右目の開閉度: " + String.format(Locale.getDefault(), "%.2f", rightEyeOpen),
                0, bottomText - paddingText * 2, mIdPaint);
        canvas.drawText("左目目の開閉度: " + String.format(Locale.getDefault(), "%.2f", leftEyeOpen),
                0, bottomText - paddingText * 3, mIdPaint);
    }

    private void drawLandmarks(Canvas canvas, Face face) {
        List<Landmark> landmarks = face.getLandmarks();
        for (Landmark landmark : landmarks) {
            if (!getLandmarkVisibility(landmark.getType())) {
                LogUtil.e("not visible");
                continue;
            }
            float xLandmark = translateX(landmark.getPosition().x);
            float yLandmark = translateY(landmark.getPosition().y);
            canvas.drawCircle(xLandmark, yLandmark, FACE_POSITION_RADIUS, mFacePositionPaint);
        }
    }

    /**
     * {@link Landmark}表示設定
     *
     * @param landmarkType {@link Landmark#getType()}
     * @param visibility   表示是非
     */
    public void setLandmarkVisibility(int landmarkType, boolean visibility) {
        mVisibilityLandmarks.put(landmarkType, visibility);
    }

    /**
     * {@link Landmark}表示取得
     *
     * @param landmarkType {@link Landmark#getType()}
     */
    public boolean getLandmarkVisibility(int landmarkType) {
        // デフォルトは表示
        if (mVisibilityLandmarks.indexOfKey(landmarkType) != -1) {
            return mVisibilityLandmarks.get(landmarkType);
        }
        return true;
    }

}