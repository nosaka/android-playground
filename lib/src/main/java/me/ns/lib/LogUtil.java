package me.ns.lib;


import android.util.Log;

/**
 * ログUtility
 *
 * @author shintaro-nosaka
 */
public final class LogUtil {

    private static final boolean OUTPUT_FLAG = true;

    /**
     * デフォルトログタグ
     */
    private static final String LOG_TAG_DEFAULT = LogUtil.class.getName();

    /**
     * 呼出元スタックトレース行番号
     */
    private static final int CALLER_STACK_TRACE = 4;

    /**
     * コンストラクタ
     */
    private LogUtil() {
        // 生成禁止
    }

    /**
     * デバッグログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     */
    public static void d(final String tag, final String msg) {
        if (OUTPUT_FLAG) {
            Log.d(tag, msg);
        }
    }

    /**
     * デバッグログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void d(final String tag, final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.d(tag, msg, t);
        }
    }

    /**
     * デバッグログ出力
     *
     * @param msg メッセージ
     */
    public static void d(final String msg) {
        if (OUTPUT_FLAG) {
            Log.d(getCallerClass(), msg);
        }
    }

    /**
     * デバッグログ出力
     *
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void d(final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.d(getCallerClass(), msg, t);
        }
    }

    /**
     * デバッグログ出力
     *
     * @param t {@link Throwable}
     */
    public static void d(final Throwable t) {
        if (OUTPUT_FLAG) {
            String tag = getCallerClass();
            if (t != null) {
                Log.d(tag, t.getMessage(), t);
            } else {
                Log.d(getCallerClass(), "null");
            }
        }
    }

    /**
     * インフォメーションログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     */
    public static void i(final String tag, final String msg) {
        if (OUTPUT_FLAG) {
            Log.i(tag, msg);
        }
    }

    /**
     * インフォメーションログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void i(final String tag, final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.i(tag, msg, t);
        }
    }

    /**
     * インフォメーションログ出力
     *
     * @param msg メッセージ
     */
    public static void i(final String msg) {
        if (OUTPUT_FLAG) {
            Log.i(getCallerClass(), msg);
        }
    }

    /**
     * インフォメーションログ出力
     *
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void i(final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.i(getCallerClass(), msg, t);
        }
    }

    /**
     * インフォメーションログ出力
     *
     * @param t {@link Throwable}
     */
    public static void i(final Throwable t) {
        if (OUTPUT_FLAG) {
            if (t != null) {
                Log.i(getCallerClass(), t.getMessage(), t);
            } else {
                Log.i(getCallerClass(), "null");
            }
        }
    }

    /**
     * VERBOSEログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     */
    public static void v(final String tag, final String msg) {
        if (OUTPUT_FLAG) {
            Log.v(tag, msg);
        }
    }

    /**
     * VERBOSEログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void v(final String tag, final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.v(tag, msg, t);
        }
    }

    /**
     * VERBOSEログ出力
     *
     * @param msg メッセージ
     */
    public static void v(final String msg) {
        if (OUTPUT_FLAG) {
            Log.v(getCallerClass(), msg);
        }
    }

    /**
     * VERBOSEログ出力
     *
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void v(final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.v(getCallerClass(), msg, t);
        }
    }

    /**
     * VERBOSEログ出力
     *
     * @param t {@link Throwable}
     */
    public static void v(final Throwable t) {
        if (OUTPUT_FLAG) {
            if (t != null) {
                Log.v(getCallerClass(), t.getMessage(), t);
            } else {
                Log.v(getCallerClass(), "null");
            }

        }
    }

    /**
     * WARNログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     */
    public static void w(final String tag, final String msg) {
        if (OUTPUT_FLAG) {
            Log.w(tag, msg);
        }
    }

    /**
     * WARNログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void w(final String tag, final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.w(tag, msg, t);
        }
    }

    /**
     * WARNログ出力
     *
     * @param msg メッセージ
     */
    public static void w(final String msg) {
        if (OUTPUT_FLAG) {
            Log.w(getCallerClass(), msg);
        }
    }

    /**
     * WARNログ出力
     *
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void w(final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.w(getCallerClass(), msg, t);
        }
    }

    /**
     * WARNログ出力
     *
     * @param t {@link Throwable}
     */
    public static void w(final Throwable t) {
        if (OUTPUT_FLAG) {
            if (t != null) {
                Log.w(getCallerClass(), t.getMessage(), t);
            } else {
                Log.w(getCallerClass(), "null");
            }
        }
    }

    /**
     * エラーログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     */
    public static void e(final String tag, final String msg) {
        if (OUTPUT_FLAG) {
            Log.e(tag, msg);
        }

    }

    /**
     * エラーログ出力
     *
     * @param tag ログタグ
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void e(final String tag, final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.e(tag, msg, t);
        }
    }

    /**
     * エラーログ出力
     *
     * @param msg メッセージ
     */
    public static void e(final String msg) {
        if (OUTPUT_FLAG) {
            Log.e(getCallerClass(), msg);
        }
    }

    /**
     * エラーログ出力
     *
     * @param msg メッセージ
     * @param t   {@link Throwable}
     */
    public static void e(final String msg, final Throwable t) {
        if (OUTPUT_FLAG) {
            Log.e(getCallerClass(), msg, t);
        }
    }

    /**
     * エラーログ出力
     *
     * @param t {@link Throwable}
     */
    public static void e(final Throwable t) {
        if (OUTPUT_FLAG) {
            if (t != null) {
                Log.e(getCallerClass(), t.getMessage(), t);
            } else {
                Log.e(getCallerClass(), "null");
            }
        }
    }

    /**
     * 呼出元クラス名取得処理
     * <p>
     * Threadのスタックとレースから呼出元クラス名を取得する
     * </p>
     *
     * @return 呼出元クラス名
     */
    private static String getCallerClass() {

        StackTraceElement[] stackTraceElements =
                Thread.currentThread().getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length >= CALLER_STACK_TRACE) {
            return Thread.currentThread().getStackTrace()[CALLER_STACK_TRACE].getClassName();
        } else {
            return LOG_TAG_DEFAULT;
        }

    }

}
