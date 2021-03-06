package me.ns.androidplayground.barcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.LogUtil;

public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * リクエストコード：ギャラリーオープン
     */
    private static final int REQUEST_OPEN_GALLERY = 0x001;

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_ExtractBarcodeImageButton)
    Button mExtractBarcodeImageButton;


    @BindView(R.id.main_ContentsTextView)
    TextView mContentsTextView;

    @BindView(R.id.main_BarcodeImageView)
    ImageView mBarcodeImageView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link BarcodeDetector}
     */
    private BarcodeDetector mBarcodeDetector;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mContentsTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        mBarcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        mExtractBarcodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT)
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("image/*"), REQUEST_OPEN_GALLERY);
            }
        });
        if (!mBarcodeDetector.isOperational()) {
            mContentsTextView.setText("セットアップが完了できませんでした");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_OPEN_GALLERY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null && data.getData() != null) {
                            Bitmap bitmap = getBitmap(data.getData());
                            mBarcodeImageView.setImageBitmap(bitmap);
                            decodeBarcode(bitmap);
                        }
                        break;
                    default:
                        // 処理なし
                }
                break;
            default:
                // 処理なし
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link Bitmap}取得処理
     *
     * @param uri {@link Uri}
     * @return {@link Bitmap}
     */
    private Bitmap getBitmap(Uri uri) {

        ParcelFileDescriptor descriptor = null;
        try {
            descriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (descriptor == null) {
                return null;
            }
            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
            return BitmapFactory.decodeFileDescriptor(fileDescriptor);
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            if (descriptor != null) {
                try {
                    descriptor.close();
                } catch (IOException ignored) {
                    LogUtil.e(ignored);
                }
            }
        }

        return null;
    }

    /**
     * バーコードデコード処理
     *
     * @param bitmap {@link Bitmap}
     */
    private void decodeBarcode(Bitmap bitmap) {
        mContentsTextView.setText("");
        if (bitmap == null) {
            mContentsTextView.setText("無効な画像");
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodeItems = mBarcodeDetector.detect(frame);
        for (int i = 0; i < barcodeItems.size(); i++) {
            int key = barcodeItems.keyAt(i);
            Barcode barcode = barcodeItems.get(key);
            mContentsTextView.append(barcode.rawValue);
            mContentsTextView.append("\n");
        }
        if (mContentsTextView.getText() == null || mContentsTextView.getText().length() <= 0) {
            mContentsTextView.setText("有効なバーコードデータがありません");
        }
    }

}
