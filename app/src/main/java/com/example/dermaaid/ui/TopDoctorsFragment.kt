package com.example.dermaaid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dermaaid.adapter.TopDoctorAdapter2
import com.example.dermaaid.R
import com.example.dermaaid.viewModel.HomeViewModel
import com.example.dermaaid.databinding.FragmentTopDoctorsBinding

class TopDoctorsFragment : BaseFragment(R.layout.fragment_top_doctors) {

    private var _binding: FragmentTopDoctorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initTopDoctors()
    }

    private fun initTopDoctors() {
        binding.apply {
            progressBarTopDoctor.visibility = View.VISIBLE

            viewModel.doctors.observe(viewLifecycleOwner) {
                viewTopDoctorList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                viewTopDoctorList.adapter = TopDoctorAdapter2(it)
                progressBarTopDoctor.visibility = View.GONE
            }

            viewModel.loadDoctors()

            backBtn.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
