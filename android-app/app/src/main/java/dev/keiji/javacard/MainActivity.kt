package dev.keiji.javacard

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.IsoDep
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import dev.keiji.apdu.ApduResponse
import dev.keiji.apdu.command.SelectFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG: String = MainActivity::class.java.simpleName

        private const val REQUEST_CODE_ENABLE_FOREGROUND_DISPATCH = 0x01

        private val INTENT_FILTER = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        private val TECH_LIST_ARRAY =
            arrayOf(
                arrayOf<String>(android.nfc.tech.NfcA::class.java.name),
//                arrayOf<String>(android.nfc.tech.NfcB::class.java.name)
            )
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

        nfcAdapter.enableForegroundDispatch(
            this,
            pendingIntent,
            arrayOf(INTENT_FILTER),
            TECH_LIST_ARRAY
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

    override fun onPause() {
        super.onPause()

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

            val card = IsoDep.get(tag)
            try {
                val apduResponse = select(
                    card,
                    // 0F:02:03:04:05:06:07:03:02
                    byteArrayOf(
                        0x0F,
                        0x02,
                        0x03,
                        0x04,
                        0x05,
                        0x06,
                        0x07,
                        0x03,
                        0x02,
                    )
                )
                setStatusText(
                    "apduResponse: 0x${Integer.toHexString(apduResponse.statusWord1)}, "
                            + "0x${Integer.toHexString(apduResponse.statusWord2)}"
                )
            } catch (exception: TagLostException) {
                Log.e(TAG, "TagLostException", exception)
                setStatusText("exception: ${exception.message}")
            }

        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun select(card: IsoDep, data: ByteArray): ApduResponse =
        withContext(Dispatchers.IO) {
            card.connect()

            Log.d(TAG, "connected ${0xAC.toByte()}")

            val selectCommand = SelectFile(
                0x00,
                arrayOf(SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME),
                arrayOf(
                    SelectFile.P2.FIRST_RECORD,
                    SelectFile.P2.RETURN_FMD_TEMPLATE,
                    SelectFile.P2.RETURN_FCP_TEMPLATE
                ),
                data, false
            )

            val sendData = selectCommand.bytes

            Log.d(TAG, "transceive ready ${sendData.toHex(":")}")

            val responseBytes = card.transceive(sendData)

            Log.d(TAG, "responseBytes ${responseBytes.toHex(":")}")

            val apduResponse = ApduResponse(responseBytes)

            card.close()

            return@withContext apduResponse
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
