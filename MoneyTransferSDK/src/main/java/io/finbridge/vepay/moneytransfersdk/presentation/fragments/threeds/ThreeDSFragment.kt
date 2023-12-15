package io.finbridge.vepay.moneytransfersdk.presentation.fragments.threeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentThreeDsBinding
import io.finbridge.vepay.moneytransfersdk.presentation.MoneyTransferViewModel
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ThreeDSFragment : Fragment() {
    private lateinit var binding: FragmentThreeDsBinding
    private val viewModel: ThreeDSViewModel by viewModels()
    private val activityViewModel: MoneyTransferViewModel by activityViewModels()

    private val threeDsUrl: String
        get() = requireArguments().getString(THREE_DS_URL_KEY) ?: emptyString()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                binding.progressBar.isVisible = progress in 0..100
            }
        }
        binding.webView.loadUrl(threeDsUrl)

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
        private const val THREE_DS_URL_KEY = "3_DS_URL"
        private const val UUID_KEY = "uuid_invoice"

        @JvmStatic
        fun newInstance(threeDsUrl: String, invoiceUuid: String) = ThreeDSFragment().apply {
            arguments = bundleOf(
                THREE_DS_URL_KEY to threeDsUrl,
                UUID_KEY to invoiceUuid
            )
        }
    }
}
