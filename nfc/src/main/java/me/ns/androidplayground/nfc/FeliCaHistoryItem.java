package me.ns.androidplayground.nfc;

import android.util.SparseArray;

import java.util.Locale;

/**
 * Created by shintaro.nosaka on 2017/05/16.
 */

public class FeliCaHistoryItem {

    public static final SparseArray<String> TERM_MAP = new SparseArray<>();

    static {
        TERM_MAP.put(3, "精算機");
        TERM_MAP.put(4, "携帯型端末");
        TERM_MAP.put(5, "車載端末");
        TERM_MAP.put(7, "券売機");
        TERM_MAP.put(8, "券売機");
        TERM_MAP.put(9, "入金機");
        TERM_MAP.put(18, "券売機");
        TERM_MAP.put(20, "券売機等");
        TERM_MAP.put(21, "券売機等");
        TERM_MAP.put(22, "改札機");
        TERM_MAP.put(23, "簡易改札機");
        TERM_MAP.put(24, "窓口端末");
        TERM_MAP.put(25, "窓口端末");
        TERM_MAP.put(26, "改札端末");
        TERM_MAP.put(27, "携帯電話");
        TERM_MAP.put(28, "乗継精算機");
        TERM_MAP.put(29, "連絡改札機");
        TERM_MAP.put(31, "簡易入金機");
        TERM_MAP.put(70, "VIEW ALTTE");
        TERM_MAP.put(72, "VIEW ALTTE");
        TERM_MAP.put(199, "物販端末");
        TERM_MAP.put(200, "自販機");
    }

    public static final SparseArray<String> PROC_MAP = new SparseArray<>();

    static {
        PROC_MAP.put(1, "運賃支払(改札出場)");
        PROC_MAP.put(2, "チャージ");
        PROC_MAP.put(3, "券購(磁気券購入)");
        PROC_MAP.put(4, "精算");
        PROC_MAP.put(5, "精算 (入場精算)");
        PROC_MAP.put(6, "窓出 (改札窓口処理)");
        PROC_MAP.put(7, "新規 (新規発行)");
        PROC_MAP.put(8, "控除 (窓口控除)");
        PROC_MAP.put(13, "バス (PiTaPa系)");
        PROC_MAP.put(15, "バス (IruCa系)");
        PROC_MAP.put(17, "再発 (再発行処理)");
        PROC_MAP.put(19, "支払 (新幹線利用)");
        PROC_MAP.put(20, "入A (入場時オートチャージ)");
        PROC_MAP.put(21, "出A (出場時オートチャージ)");
        PROC_MAP.put(31, "入金 (バスチャージ)");
        PROC_MAP.put(35, "券購 (バス路面電車企画券購入)");
        PROC_MAP.put(70, "物販");
        PROC_MAP.put(72, "特典 (特典チャージ)");
        PROC_MAP.put(73, "入金 (レジ入金)");
        PROC_MAP.put(74, "物販取消");
        PROC_MAP.put(75, "入物 (入場物販)");
        PROC_MAP.put(198, "物現 (現金併用物販)");
        PROC_MAP.put(203, "入物 (入場現金併用物販)");
        PROC_MAP.put(132, "精算 (他社精算)");
        PROC_MAP.put(133, "精算 (他社入場精算)");
    }

    public int termId;
    public int procId;
    public int entryLineCode;
    public int entryStationCode;
    public int exitLineCode;
    public int exitStationCode;
    public int year;
    public int month;
    public int day;
    public int remain;
    public int seqNo;
    public int reasion;

    /**
     * コンストラクタ
     */
    private FeliCaHistoryItem() {
        // 生成禁止
    }

    public static FeliCaHistoryItem parse(byte[] res, int off) {
        FeliCaHistoryItem self = new FeliCaHistoryItem();
        self.init(res, off);
        return self;
    }

    private void init(byte[] res, int off) {
        this.termId = res[off] & 0xFF; //0: 端末種
        this.procId = res[off + 1] & 0xFF; //1: 処理
        //2-3: ??
        int mixInt = toInt(res, off, 4, 5); // 4-5: 日付 (先頭から7ビットが年、４ビットが月、残り５ビットが日)
        this.year = (mixInt >> 9) & 0x07f;
        this.month = (mixInt >> 5) & 0x00f;
        this.day = mixInt & 0x01f;
        this.entryLineCode = res[off + 6] & 0xFF; // 6:入線区
        this.entryStationCode = res[off + 7] & 0xFF; // 7:入駅順
        this.exitLineCode = res[off + 8] & 0xFF; // 8:出線区
        this.exitStationCode = res[off + 9] & 0xFF; // 9:出駅順
        this.remain = toInt(res, off, 11, 10); //10-11: 残高 (little endian)
        this.seqNo = toInt(res, off, 12, 13, 14); //12-14: 連番
        this.reasion = res[off + 15]; //15: リージョン
    }

    private int toInt(byte[] res, int off, int... idx) {
        int num = 0;
        for (int anIdx : idx) {
            num = num << 8;
            num += ((int) res[off + anIdx]) & 0x0ff;
        }
        return num;
    }

    public String getProcName() {
        String result = PROC_MAP.get(procId);
        return result != null ? result : Integer.toString(procId);
    }

    public String getTermName() {
        String result = TERM_MAP.get(termId);
        return result != null ? result : Integer.toString(termId);
    }

    public String getEnterRegion() {
        switch (termId) {
            // 店舗名
            case 199: // 物販端末
            case 200: // 自販機
                //  店舗名
                return String.format(Locale.getDefault(), "%1$d,%2$d,%3$d", -1, entryLineCode, entryStationCode);
            case 5:
                //  車載端末(バス)
                return String.format(Locale.getDefault(), "%1$d,%2$d", entryLineCode, entryStationCode);
            default:
                // 駅名
                int areaCode;
                if (entryLineCode >= 7) {
                    areaCode = 0; // JR線
                } else if (reasion == 0) {
                    areaCode = 1; // 関東公営・私鉄
                } else {
                    areaCode = 2; // 関西公営・私鉄
                }
                return String.format(Locale.getDefault(), "%1$d,%2$d,%3$d", areaCode, entryLineCode, entryStationCode);
        }
    }

    public String getExitRegion() {
        switch (termId) {
            // 店舗名
            case 199: // 物販端末
            case 200: // 自販機
                //  店舗名
                return String.format(Locale.getDefault(), "%1$d,%2$d,%3$d", -1, exitLineCode, exitStationCode);
            case 5:
                //  車載端末(バス)
                return String.format(Locale.getDefault(), "%1$d,%2$d", exitLineCode, exitStationCode);
            default:
                // 駅名
                int areaCode;
                if (entryLineCode >= 7) {
                    areaCode = 0; // JR線
                } else if (reasion == 0) {
                    areaCode = 1; // 関東公営・私鉄
                } else {
                    areaCode = 2; // 関西公営・私鉄
                }
                return String.format(Locale.getDefault(), "%1$d,%2$d,%3$d", areaCode, exitLineCode, exitStationCode);
        }
    }

}
