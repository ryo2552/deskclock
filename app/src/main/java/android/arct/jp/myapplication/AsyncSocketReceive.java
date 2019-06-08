package android.arct.jp.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class AsyncSocketReceive extends AsyncTask<String, Void, String> {
    private int TODAY_FORCAST_INDEX = 0;
    private Activity mainActivity;

    public AsyncSocketReceive(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    /**
     * 非同期処理で温湿度を取得する.
     */
    @Override
    protected String doInBackground(String... str) {

        DatagramSocket sock = null;
        String host = "192.168.3.5";
        int port = 3610;
        //byte[] data = {0x10, (byte) 0x81, 0x00, 0x00, 0x05, (byte) 0xFF, 0x01, 0x01, 0x35, 0x01, 0x60, 0x01, (byte) 0x80, 0x01, 0x30}; //電源ON
        //byte[] data = {0x10, (byte) 0x81, 0x00, 0x00, 0x05, (byte) 0xFF, 0x01, 0x01, 0x35, 0x01, 0x62, 0x01, (byte) 0xF1, 0x00}; //温湿度取得


        try {
            //byte[] sendData = data.getBytes("UTF-8");//UTF-8バイト配列の作成
            byte[] data = new byte[256];
            sock = new DatagramSocket(port);//UDP送信用ソケットの構築
            DatagramPacket packet = new DatagramPacket(data, data.length);//指定アドレス、ポートへ送信するパケットを構築
            sock.receive(packet);//パケットの送信
            return "0";
        } catch (Exception  e) {
            e.printStackTrace();
            return null;
        } finally {
            if (sock != null) {
                sock.close();//ソケットのクローズ;
            }

        }
    }


}