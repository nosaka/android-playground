package me.ns.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * キャッシュUtility
 * <p>
 * Created by shintaro.nosaka on 2017/05/12.
 */
public class CacheUtil {

    /**
     * メモリキャッシュ最大値
     */
    public static final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);

    /**
     * {@link Bitmap}キャッシュディレクトリ名
     */
    private static final String BITMAP_CACHE_DIR_NAME = "bitmap";

    /**
     * {@link Bitmap}メモリキャッシュ
     */
    private static LruCache<String, Bitmap> sBitmapCache = new LruCache<String, Bitmap>(MAX_MEMORY) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    /**
     * {@link Bitmap}キャッシュ取得処理
     *
     * @param context {@link Context}
     * @param key     キャッシュキー
     * @return {@link Bitmap}
     */
    public static Bitmap getBitmapCache(Context context, String key) {

        Bitmap bitmap = sBitmapCache.get(key);
        if (bitmap != null) {
            return bitmap;
        }

        File cacheDir = new File(context.getCacheDir(), BITMAP_CACHE_DIR_NAME);
        File cacheFile = new File(cacheDir, key);
        if (cacheFile.exists() && cacheFile.canRead()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(cacheFile);
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        LogUtil.e(e);
                    }
                }
            }
        }


        return null;
    }

    /**
     * {@link Bitmap}キャッシュ処理
     *
     * @param context {@link Context}
     * @param bitmap  {@link Bitmap}
     * @return キャッシュキー
     */
    public static String saveCache(Context context, Bitmap bitmap) {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString();
        return saveCache(context, bitmap, key);
    }

    /**
     * {@link Bitmap}キャッシュ処理
     *
     * @param context {@link Context}
     * @param bitmap  {@link Bitmap}
     * @return キャッシュキー
     */
    public static String saveCache(Context context, Bitmap bitmap, String key) {
        // メモリキャッシュに保存
        sBitmapCache.put(key, bitmap);

        // ファイルキャッシュに保存
        saveFileCache(context, key, bitmap);

        return key;
    }

    /**
     * {@link Bitmap}メモリキャッシュ処理
     *
     * @param bitmap {@link Bitmap}
     * @return キャッシュキー
     */
    public static String saveCacheOnlyMemory(Bitmap bitmap) {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString();
        return saveCacheOnlyMemory(bitmap, key);
    }

    /**
     * {@link Bitmap}メモリキャッシュ処理
     *
     * @param bitmap {@link Bitmap}
     * @return キャッシュキー
     */
    public static String saveCacheOnlyMemory(Bitmap bitmap, String key) {
        // メモリキャッシュに保存
        sBitmapCache.put(key, bitmap);
        return key;
    }

    /**
     * {@link Bitmap}ファイルキャッシュ処理
     *
     * @param context {@link Context}
     * @param key     キャッシュキー
     * @param bitmap  {@link Bitmap}
     * @return true...キャッシュ成功、false...キャッシュ失敗
     */
    private static boolean saveFileCache(Context context, String key, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            File cacheDir = new File(context.getCacheDir(), BITMAP_CACHE_DIR_NAME);

            // ファイルキャッシュに保存
            File cacheFile = new File(cacheDir, key);
            if (!cacheFile.createNewFile()) {
                return false;
            }
            fos = new FileOutputStream(cacheFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

            return true;
        } catch (Exception e) {
            LogUtil.e(e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }

        return false;
    }
}
