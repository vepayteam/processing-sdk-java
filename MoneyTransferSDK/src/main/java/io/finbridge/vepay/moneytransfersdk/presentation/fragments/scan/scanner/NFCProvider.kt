package io.finbridge.vepay.moneytransfersdk.presentation.fragments.scan.scanner

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException


class NFCProvider : IProvider {
    private var isoDepTag: IsoDep? = null

    @Throws(CommunicationException::class)
    override fun transceive(pCommand: ByteArray): ByteArray? {
        return try {
            isoDepTag?.transceive(pCommand)
        } catch (e: IOException) {
            throw CommunicationException(e.message)
        }
    }

    override fun getAt(): ByteArray? = isoDepTag?.historicalBytes

    fun setTagCom(isoDepTag: IsoDep?) {
        this.isoDepTag = isoDepTag
    }
}
