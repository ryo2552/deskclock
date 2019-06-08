package android.arct.jp.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.net.*
import android.view.View
import android.os.Build
import android.app.Activity
import android.content.Context
import kotlin.concurrent.thread
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.graphics.Typeface






class MainActivity : AppCompatActivity() {

    val handler:Handler? = null
    val r:Runnable? = null
    val handler2:Handler? = null
    val r2:Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val decor = this.window.decorView
        decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val handler = Handler()
        val r = object : Runnable {
            override fun run() {
                // 非同期処理(AsyncHttpRequest#doInBackground())を呼び出す
                try {
                    AsyncHttpRequest(this@MainActivity).execute(URL("http://weather.livedoor.com/forecast/webservice/json/v1?city=220010"))
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                }

                handler.postDelayed(this, 3600000)
            }
        }
        val handler2 = Handler()
        val r2 = object : Runnable {
            override fun run() {
                // 非同期処理(AsyncHttpRequest#doInBackground())を呼び出す
                try {

                    AsyncSocketSend(this@MainActivity).execute("0")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                handler.postDelayed(this, 60000)
            }
        }
        handler.post(r)
        handler2.post(r2)

    }

    public override fun onDestroy() {
        super.onDestroy()
        if(handler != null) {
            handler.removeCallbacks(r)
        }
        if(handler2 != null) {
            handler2.removeCallbacks(r2)
        }
    }

    fun setNavigationbarHide(activity: Activity, hide: Boolean) {
        val decor = activity.window.decorView
        if (hide && Build.VERSION.SDK_INT > 18) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else if (hide && Build.VERSION.SDK_INT > 15) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        } else if (hide && Build.VERSION.SDK_INT > 13) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

    }

    protected override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

}
