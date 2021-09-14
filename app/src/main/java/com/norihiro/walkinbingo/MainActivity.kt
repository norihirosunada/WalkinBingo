package com.norihiro.walkinbingo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.norihiro.walkinbingo.databinding.ActivityMainBinding
import java.io.File

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L

class MainActivity : AppCompatActivity(), CheckPhotoDialogFragment.CheckPhotoDialogListener {
    private lateinit var binding: ActivityMainBinding
    val words = listOf("りんご", "バナナ", "ぶどう", "メロン", "スイカ", "さくらんぼ", "みかん", "もも", "パイナップル", "キウイ", "イチゴ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val shuffledWords = words.shuffled()
//
//        Log.d("debug", "binding.tableLayout.childCount = " + binding.tableLayout.childCount)
//        val tableLayout = binding.tableLayout
//        for (i in 0 until tableLayout.childCount){
//            val tableRow = tableLayout.getChildAt(i) as TableRow
//            Log.d("debug", "tableRow.childCount = " + tableRow.childCount)
//            for (j in 0 until tableRow.childCount){
//                val square = tableRow.getChildAt(j) as TextView
////                各マスの初期化
//                square.text = shuffledWords[i*3+j]
//
////                各マスのClickListener
//                square.setOnClickListener {
//                    if (checkHit(it)){
//                        Log.d("Square: $it", "checkHit() is True")
//                        (it as TextView).text = "Hit"
//                    }
//                    checkBingo()
//                }
//            }
//        }

//        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            // 呼び出し先のActivityを閉じた時に呼び出されるコールバック
//            if(result.resultCode == Activity.RESULT_OK) {
//                // RESULT_OK時の処理
//                val intent = result.data
//                val uri = intent?.getStringExtra("picture_uri")
//                Log.d("intent", "RESULT_OK return:$uri")
//                binding.textView2.text = uri
//            }
//        }

//        binding.button.setOnClickListener {
//            val intent = Intent(this, CameraxActivity::class.java)
//            launcher.launch(intent)
//        }
    }

//    private fun checkBingo() {
//        var hits = mutableListOf<Boolean>()
//        val tableLayout = binding.tableLayout
//        for (i in 0 until tableLayout.childCount){
//            val tableRow = tableLayout.getChildAt(i) as TableRow
//            for (j in 0 until tableRow.childCount){
//                val square = tableRow.getChildAt(j) as TextView
//                hits.add(square.text == "Hit")
//            }
//        }
//        Log.d("checkBingo()", "hits[] = $hits")
//
//        var dialog = ""
//
//        if (hits[0] && hits[1] && hits[2]) dialog += "BINGO123"
//        if (hits[3] && hits[4] && hits[5]) dialog += "BINGO456"
//        if (hits[6] && hits[7] && hits[8]) dialog += "BINGO789"
//        if (hits[0] && hits[3] && hits[6]) dialog += "BINGO147"
//        if (hits[1] && hits[4] && hits[7]) dialog += "BINGO258"
//        if (hits[2] && hits[5] && hits[8]) dialog += "BINGO369"
//        if (hits[0] && hits[4] && hits[8]) dialog += "BINGO159"
//        if (hits[2] && hits[4] && hits[6]) dialog += "BINGO357"
//
//        binding.textView.text = dialog
//    }
//
//    fun checkHit(view: View): Boolean {
//        return true
//    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

}