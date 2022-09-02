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
import dev.keiji.apdu.command.ReadBinary
import dev.keiji.apdu.command.SelectFile
import dev.keiji.apdu.command.Verify
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

        // 0F:02:03:04:05:06:07:03:02
        private val AID = byteArrayOf(
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

        private val CORRECT_PIN = byteArrayOf(
            0x04,
            0x05,
            0x06,
            0x07,
        )

        private val WRONG_PIN = byteArrayOf(
            0x04,
            0x05,
            0x06,
            0x00,
        )

        private val PIN = CORRECT_PIN
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
            setStatusText("カードをタッチしてください", clear = true)
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
            setStatusText("", clear = true)
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

            val isoDep = IsoDep.get(tag)

            try {
                connect(isoDep)
                val selectResponse = select(
                    isoDep,
                    AID,
                )
                setStatusText(
                    "selectResponse: 0x${Integer.toHexString(selectResponse.statusWord1)}, "
                            + "0x${Integer.toHexString(selectResponse.statusWord2)}"
                )

                val verifyResponse = verify(isoDep, PIN)
                setStatusText(
                    "verifyResponse: 0x${Integer.toHexString(verifyResponse.statusWord1)}, "
                            + "0x${Integer.toHexString(verifyResponse.statusWord2)}"
                )
            } catch (exception: TagLostException) {
                Log.e(TAG, "TagLostException", exception)
                setStatusText("exception: ${exception.message}")
            } finally {
                close(isoDep)
            }

        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun connect(isoDep: IsoDep) = withContext(Dispatchers.IO) {
        isoDep.connect()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun close(isoDep: IsoDep) = withContext(Dispatchers.IO) {
        isoDep.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun select(isoDep: IsoDep, data: ByteArray): ApduResponse =
        withContext(Dispatchers.IO) {

            val selectFileCommandData = SelectFile(
                0x00,
                arrayOf(SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME),
                arrayOf(
                    SelectFile.P2.FIRST_RECORD,
                    SelectFile.P2.RETURN_FMD_TEMPLATE,
                    SelectFile.P2.RETURN_FCP_TEMPLATE
                ),
                data, false
            ).bytes

            Log.d(TAG, "transceive:selectFile: ${selectFileCommandData.toHex(":")}")

            val responseBytes = isoDep.transceive(selectFileCommandData)

            Log.d(TAG, "responseBytes:selectFile: ${responseBytes.toHex(":")}")

            val apduResponse = ApduResponse(responseBytes)

            return@withContext apduResponse
        }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun verify(isoDep: IsoDep, pin: ByteArray): ApduResponse =
        withContext(Dispatchers.IO) {

            val selectFileCommandData = Verify(
                0x00,
                Verify.P2.GLOBAL,
                pin
            ).bytes

            Log.d(TAG, "transceive:verify: ${selectFileCommandData.toHex(":")}")

            val responseBytes = isoDep.transceive(selectFileCommandData)

            Log.d(TAG, "responseBytes:verify: ${responseBytes.toHex(":")}")

            val apduResponse = ApduResponse(responseBytes)

            return@withContext apduResponse
        }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun readBinary(isoDep: IsoDep): ApduResponse =
        withContext(Dispatchers.IO) {
            val readBinaryCommandData = ReadBinary(
                0,
                0,
                6,
                false,
            ).bytes

            Log.d(TAG, "transceive:readBinary: ${readBinaryCommandData.toHex(":")}")

            val responseBytes = isoDep.transceive(readBinaryCommandData)

            Log.d(TAG, "responseBytes:readBinary: ${responseBytes.toHex(":")}")

            val apduResponse = ApduResponse(responseBytes)

            return@withContext apduResponse
        }

    private suspend fun setStatusText(
        text: String,
        clear: Boolean = false
    ) = withContext(Dispatchers.Main) {
        if (clear) {
            status.text = text
            return@withContext
        }
        status.text = "${status.text}\n${text}"
    }

    private fun readTag(intent: Intent): Pair<ByteArray?, Tag?> {
        val id: ByteArray? = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        return Pair(id, tag)
    }
}

private fun ByteArray.toHex(delimiter: String) =
    joinToString(delimiter) { "%02x".format(it).uppercase() }
