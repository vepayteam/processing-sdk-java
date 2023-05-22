package io.finbridge.vepay.moneytransfersdk.presentation.fragments.sse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentSseBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SseFragment : Fragment() {
    private lateinit var binding: FragmentSseBinding
    private val viewModel: SseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollectors()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.sseMessage.collect {
                if (it != emptyString()) {
                    binding.progressBar.isVisible = false
                    binding.sseMessage.isVisible = true
                    binding.sseMessage.text = it
                }
            }
        }
    }

    companion object {
        fun newInstance() = SseFragment()
    }
}
