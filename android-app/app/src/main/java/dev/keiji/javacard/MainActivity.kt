package dev.keiji.javacard

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_ENABLE_FOREGROUND_DISPATCH = 0x01

        private val INTENT_FILTER = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
    }

    private lateinit var buttonStartReader: Button
    private lateinit var status: TextView

    private lateinit var nfcManager: NfcManager
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartReader = findViewById(R.id.start_reader)
        status = findViewById(R.id.status)

        nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager
        nfcAdapter = nfcManager.defaultAdapter

        buttonStartReader.setOnClickListener {
            startForegroundDispatch()
        }
    }

    private fun startForegroundDispatch() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = createPendingIntentCompat(
            applicationContext,
            intent,
            REQUEST_CODE_ENABLE_FOREGROUND_DISPATCH
        )

        val techListsArray = arrayOf(arrayOf<String>(NfcA::class.java.name))
        nfcAdapter.enableForegroundDispatch(
            this,
            pendingIntent,
            arrayOf(INTENT_FILTER),
            techListsArray
        )

        lifecycleScope.launch(Dispatchers.Main) {
            setStatusText("カードをタッチしてください")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createPendingIntentCompat(
        context: Context,
        intent: Intent,
        requestCode: Int
    ): PendingIntent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }

        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            0x0,
        )
    }

    private fun stopForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onStop() {
        super.onStop()

        stopForegroundDispatch()

        lifecycleScope.launch(Dispatchers.Main) {
            setStatusText("")
        }
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val action = intent.toString()

        val (id, tag) = readTag(intent)
        tag ?: return
        id ?: return

        lifecycleScope.launch(Dispatchers.Main) {
            setStatusText("onNewIntent Card detected: ${id.toHex(":")}")
        }
    }

    private suspend fun setStatusText(text: String) = withContext(Dispatchers.Main) {
        status.text = text
    }

    private fun readTag(intent: Intent): Pair<ByteArray?, Tag?> {
        val id: ByteArray? = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        return Pair(id, tag)
    }
}

private fun ByteArray.toHex(delimiter: String) =
    joinToString(delimiter) { "%02x".format(it).uppercase() }
