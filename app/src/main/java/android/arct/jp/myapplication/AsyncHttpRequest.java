package android.arct.jp.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 非同期処理を行うクラス.
 */
public final class AsyncHttpRequest extends AsyncTask<URL, Void, Integer> {
    private int TODAY_FORCAST_INDEX = 0;
    private Activity mainActivity;

    public AsyncHttpRequest(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    /**
     * 非同期処理で天気情報を取得する.
     * @param urls 接続先のURL
     * @return 取得した天気情報
     */
    @Override
    protected Integer doInBackground(URL... urls) {

        final URL url = urls[0];
        HttpURLConnection con = null;

        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(false);
            con.connect();

            final int statusCode = con.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.err.println("正常に接続できていません。statusCode:" + statusCode);
                return null;
            }

            // レスポンス(JSON文字列)を読み込む準備
            final InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            if (null == encoding) {
                encoding = "UTF-8";
            }
            final InputStreamReader inReader = new InputStreamReader(in, encoding);
            final BufferedReader bufReader = new BufferedReader(inReader);
            StringBuilder response = new StringBuilder();
            String line = null;
            // 1行ずつ読み込む
            while ((line = bufReader.readLine()) != null) {
                response.append(line);
            }
            bufReader.close();
            inReader.close();
            in.close();

            // 受け取ったJSON文字列をパース
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject todayForcasts = jsonObject.getJSONArray("forecasts").getJSONObject(TODAY_FORCAST_INDEX);

            //return todayForcasts.getString("dateLabel") + "の天気は " + todayForcasts.getString("telop");
            String todayImageUrl = todayForcasts.getJSONObject("image").getString("url");
            String[] path = todayImageUrl.split("/", 0);
            String fn = path[path.length - 1];
            int index = fn.lastIndexOf('.');
            String fin = fn.substring(0, index);
            int imageNum = Integer.parseInt(fin);
            return imageNum;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        //} catch (IOException e) {
        //    e.printStackTrace();
        //    return null;
        //} catch (JSONException e) {
        //    e.printStackTrace();
        //    return null;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    /**
     * 非同期処理が終わった後の処理.
     */
    @Override
    protected void onPostExecute(Integer imageNum) {
        ImageView wi = mainActivity.findViewById(R.id.weatherImage);
        switch(imageNum) {
            case 1:
                wi.setImageResource(R.drawable.sunny); //晴れ
                break;
            case 2:
            case 5:
            case 9:
            case 12:
                wi.setImageResource(R.drawable.sunny_cloudy); //晴れor曇り
                break;
            case 3:
            case 6:
            case 10:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                wi.setImageResource(R.drawable.rainy); //雨
                break;
            case 4:
            case 7:
            case 11:
            case 14:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                wi.setImageResource(R.drawable.snowy); //雪
                break;
            case 8:
                wi.setImageResource(R.drawable.cloudy); //曇り
                break;
            case 22:
                wi.setImageResource(R.drawable.stormy); //嵐
                break;
            default:
                break;
        }
    }
}
