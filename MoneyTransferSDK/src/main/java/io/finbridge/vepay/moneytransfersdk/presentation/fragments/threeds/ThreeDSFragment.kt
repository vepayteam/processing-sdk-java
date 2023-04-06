package io.finbridge.vepay.moneytransfersdk.presentation.fragments.threeds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentThreeDsBinding

class ThreeDSFragment : Fragment() {
    private lateinit var binding: FragmentThreeDsBinding
    private val viewModel: ThreeDSViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentThreeDsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = ThreeDSFragment()
    }
}
