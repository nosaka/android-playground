package me.ns.androidplayground.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ns.lib.LogUtil;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    // //////////////////////////////////////////////////////////////////////////
    // staticフィールド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // Bind UI
    // //////////////////////////////////////////////////////////////////////////

    @BindView(R.id.main_FeliCaHistoryListView)
    ListView mFeliCaHistoryListView;

    // //////////////////////////////////////////////////////////////////////////
    // インスタンスフィールド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * {@link FeliCaHistoryAdapter}
     */
    private FeliCaHistoryAdapter mFeliCaHistoryAdapter;

    // //////////////////////////////////////////////////////////////////////////
    // イベントメソッド
    // //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mFeliCaHistoryAdapter = new FeliCaHistoryAdapter(this);
        mFeliCaHistoryListView.setAdapter(mFeliCaHistoryAdapter);

        receiveNfc(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveNfc(intent);
    }

    // //////////////////////////////////////////////////////////////////////////
    // interface実装メソッド
    // //////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////
    // その他メソッド
    // //////////////////////////////////////////////////////////////////////////

    /**
     * NFC {@link Intent}受信時処理
     *
     * @param intent {@link Intent}
     */
    private void receiveNfc(Intent intent) {
        if (intent == null) {
            return;
        }
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }

        NfcF nfc = NfcF.get(tag);
        if (nfc == null) {
            return;
        }
        try {
            nfc.connect();
            byte[] reqest = readWithoutEncryption(tag.getId(), 10);
            LogUtil.d("req:" + toHex(reqest));
            // カードにリクエスト送信
            byte[] response = nfc.transceive(reqest);
            LogUtil.d("res:" + toHex(response));
            List<FeliCaHistoryItem> items = parse(response);
            mFeliCaHistoryAdapter.clear();
            mFeliCaHistoryAdapter.addAll(items);
        } catch (IOException e) {
            LogUtil.e(e);
            Snackbar.make(mFeliCaHistoryListView, "FeliCa 接続エラー", Snackbar.LENGTH_INDEFINITE).show();
        } catch (FeliCaException e) {
            LogUtil.e(e);
            Snackbar.make(mFeliCaHistoryListView, "FeliCa パースエラー", Snackbar.LENGTH_INDEFINITE).show();
        } finally {
            try {
                nfc.close();
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }

    }

    /**
     * 履歴読み込みFelicaコマンドの取得。
     * - Sonyの「Felicaユーザマニュアル抜粋」の仕様から。
     * - サービスコードは http://sourceforge.jp/projects/felicalib/wiki/suica の情報から
     * - 取得できる履歴数の上限は「製品により異なります」。
     *
     * @param idm  カードのID
     * @param size 取得する履歴の数
     * @return FeliCaコマンド
     */
    private byte[] readWithoutEncryption(byte[] idm, int size) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(100);

        bout.write(0);           // データ長バイトのダミー
        bout.write(0x06);        // FeliCaコマンド「Read Without Encryption」
        bout.write(idm);         // カードID 8byte
        bout.write(1);           // サービスコードリストの長さ(以下２バイトがこの数分繰り返す)
        bout.write(0x0f);        // 履歴のサービスコード下位バイト
        bout.write(0x09);        // 履歴のサービスコード上位バイト
        bout.write(size);        // ブロック数
        for (int i = 0; i < size; i++) {
            bout.write(0x80);    // ブロックエレメント上位バイト 「Felicaユーザマニュアル抜粋」の4.3項参照
            bout.write(i);       // ブロック番号
        }

        byte[] msg = bout.toByteArray();
        msg[0] = (byte) msg.length; // 先頭１バイトはデータ長
        return msg;
    }

    /**
     * 履歴FeliCa応答の解析。
     *
     * @param response FeliCa応答
     * @return {@link FeliCaHistoryItem}リスト
     */
    private List<FeliCaHistoryItem> parse(byte[] response) throws FeliCaException {
        // res[0] = データ長
        // res[1] = 0x07
        // res[2〜9] = カードID
        // res[10,11] = エラーコード。0=正常。
        if (response[10] != 0x00) throw new FeliCaException(response[10]);

        // res[12] = 応答ブロック数
        // res[13+n*16] = 履歴データ。16byte/ブロックの繰り返し。
        int size = response[12];
        List<FeliCaHistoryItem> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // 個々の履歴の解析。
            FeliCaHistoryItem item = FeliCaHistoryItem.parse(response, 13 + i * 16);
            items.add(item);
        }
        return items;
    }

    private String toHex(byte[] id) {
        StringBuilder hexBuilder = new StringBuilder();
        for (int i = 0; i < id.length; i++) {
            String hex = "0" + Integer.toString((int) id[i] & 0x0ff, 16);
            if (hex.length() > 2)
                hex = hex.substring(1, 3);
            hexBuilder.append(" ").append(i).append(":").append(hex);
        }
        return hexBuilder.toString();
    }
}
