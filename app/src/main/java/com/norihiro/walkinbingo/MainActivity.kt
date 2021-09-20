package com.norihiro.walkinbingo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.norihiro.walkinbingo.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//import kotlinx.serialization.*
//import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L

//@Serializable
//data class Data(val a: List<String>)

@BindingAdapter("imageUri")
fun ImageView.imageUri(stringUri: String?){
    if (stringUri != null) {
        this.setImageURI(Uri.parse(stringUri))
    }
}

class MainActivity : AppCompatActivity(), CheckPhotoDialogFragment.CheckPhotoDialogListener {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        lifecycleScope.launch {
//            saveBingoCard(shuffledLabels)
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

    }

    suspend fun saveBingoCard(bingoLabels: List<String>){
//        val jsonList = Json.stringify(bingoLabels)
//        val BINGOCARD = stringPreferencesKey("bingo_card")
//        dataStore.edit { settings ->
//            settings[BINGOCARD] = jsonList
//        }
    }

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

        private const val TAG = "MainActivity"

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

    override fun onDialogDismiss(dialog: DialogFragment) {
        Log.d(TAG, "onDialogDismiss()")
    }

}