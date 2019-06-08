package android.arct.jp.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public final class AsyncSocketSend extends AsyncTask<String, Void, byte[]> {
    private int TODAY_FORCAST_INDEX = 0;
    private Activity mainActivity;

    public AsyncSocketSend(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    /**
     * 非同期処理で温湿度を取得する.
     */
    @Override
    protected byte[] doInBackground(String... str) {

        DatagramSocket sock = null;
        String host = "192.168.3.5";
        int port = 3610;
        int BUFSIZE = 256;
        byte[] buf = new byte[BUFSIZE]; //送受信バッファ
        //byte[] data = {0x10, (byte) 0x81, 0x00, 0x00, 0x05, (byte) 0xFF, 0x01, 0x01, 0x35, 0x01, 0x60, 0x01, (byte) 0x80, 0x01, 0x30}; //電源ON
        byte[] data = {0x10,(byte)0x81,0x00,0x00,0x05,(byte)0xFF,0x01,0x01,0x35,0x01,0x62,0x01,(byte)0xF1,0x00}; //温湿度取得


        try {
            //byte[] sendData = data.getBytes("UTF-8");//UTF-8バイト配列の作成
            sock = new DatagramSocket(port);//UDP送信用ソケットの構築
            sock.setSoTimeout(10000);
            DatagramPacket packet = new DatagramPacket(data, data.length, new InetSocketAddress(host, port));//指定アドレス、ポートへ送信するパケットを構築
            sock.send(packet);//パケットの送信
            buf = new byte[BUFSIZE];
            packet = new DatagramPacket(buf,buf.length);
            sock.receive(packet); //エコーデータ受信
            //String data2 = new String(packet.getData(),0,packet.getLength());
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (sock != null) {
                sock.close();//ソケットのクローズ;
            }

        }
    }

    /**
     * 非同期処理が終わった後の処理.
     */
    @Override
    protected void onPostExecute(byte[] result) {
        int temp = result[17]; //温度
        int hum = result[18]; //湿度
        TextView temp_text = mainActivity.findViewById(R.id.temp);
        temp_text.setText(Integer.toString(temp));
        TextView hum_text = mainActivity.findViewById(R.id.hum);
        hum_text.setText(Integer.toString(hum));
    }
}
