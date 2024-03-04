package io.finbridge.vepay.moneytransfersdk.presentation.fragments.threeds

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentThreeDsBinding
import io.finbridge.vepay.moneytransfersdk.presentation.MoneyTransferViewModel
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ThreeDSFragment : Fragment() {
    private lateinit var binding: FragmentThreeDsBinding
    private val viewModel: ThreeDSViewModel by viewModels()
    private val activityViewModel: MoneyTransferViewModel by activityViewModels()

    private val acsUrl: String
        get() = requireArguments().getString(ARG_ACS_URL) ?: emptyString()

    private val md: String
        get() = requireArguments().getString(ARG_MD) ?: emptyString()

    private val paReq: String
        get() = requireArguments().getString(ARG_PA_REQ) ?: emptyString()

    private val term: String
        get() = requireArguments().getString(ARG_TERM) ?: emptyString()

    private val invoiceUuid: String
        get() = requireArguments().getString(UUID_KEY) ?: emptyString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.sseInitialization(invoiceUuid)
        binding = FragmentThreeDsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true

        try {
            val params = StringBuilder()
                .append("PaReq=").append(URLEncoder.encode(paReq, "UTF-8"))
                .append("&TermUrl=").append(URLEncoder.encode(term, "UTF-8"))
                .append("&MD=").append(URLEncoder.encode(md, "UTF-8"))
                .toString()
            binding.webView.postUrl(acsUrl, params.toByteArray())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        lifecycleScope.launch {
            viewModel.paymentStatus.collect {
                when (it) {
                    TransferStatus.DONE -> activityViewModel.changePaymentStatus(TransferStatus.DONE)
                    TransferStatus.ERROR -> activityViewModel.changePaymentStatus(TransferStatus.ERROR)
                    TransferStatus.WAITING -> activityViewModel.changePaymentStatus(TransferStatus.WAITING)
                    else -> Unit
                }
            }
        }
    }

    companion object {
        private const val UUID_KEY = "uuid_invoice"
        private const val ARG_ACS_URL = "acs_url"
        private const val ARG_MD = "md"
        private const val ARG_PA_REQ = "pa_req"
        private const val ARG_TERM = "term"

        @JvmStatic
        fun newInstance(
            acsUrl: String,
            md: String,
            paReq: String,
            term: String,
            invoiceUuid: String
        ) = ThreeDSFragment().apply {
            arguments = bundleOf(
                UUID_KEY to invoiceUuid,
                ARG_ACS_URL to acsUrl,
                ARG_MD to md,
                ARG_PA_REQ to paReq,
                ARG_TERM to term
            )
        }
    }
}
