package com.devfutech.nusantecttest.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devfutech.nusantecttest.R
import com.devfutech.nusantecttest.adapter.InputAdapter
import com.devfutech.nusantecttest.databinding.HomeFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), InputAdapter.OnItemClickListener, View.OnClickListener {
    private val viewModel by viewModels<HomeViewModel>()
    private val binding: HomeFragmentBinding by lazy {
        HomeFragmentBinding.inflate(layoutInflater)
    }
    private val input: Array<Double?> = arrayOfNulls(3)
    private var flowCalculate: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView() {
        val inputAdapter = InputAdapter(input.size, this)
        binding.apply {
            rvInput.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = inputAdapter
            }
            btnTambah.setOnClickListener(this@HomeFragment)
            btnKurang.setOnClickListener(this@HomeFragment)
            btnKali.setOnClickListener(this@HomeFragment)
            btnBagi.setOnClickListener(this@HomeFragment)
        }
    }

    private fun initObserver() {
        viewModel.result.observe(viewLifecycleOwner, {
            binding.txtHasil.text = it.toString()
        })
    }

    override fun onItemAddClicked(position: Int, value: Double) {
        input[position] = value
    }

    override fun onItemRemoveClicked(position: Int) {
        input[position] = null
    }

    override fun onClick(view: View?) {
        flowCalculate = ""
        binding.txtCalculate.text = flowCalculate
        if (input.filterNotNull().size > 1) {
            flowCalculate += " Cara Perhitungan : \n"
            val checklist = if (input.filterNotNull().size == input.size) {
                " Semua input"
            } else {
                " ${input.filterNotNull().size} input"
            }
            flowCalculate += " Jumlah checklist\t : $checklist\n"
            when (view?.id) {
                R.id.btnTambah -> {
                    viewModel.operationCalculate(input.filterNotNull(), "+")
                    flowCalculate += " Operasi\t : Penjumlahan(+)\n"
                    flowCalculate += inputValue("+")
                }
                R.id.btnKurang -> {
                    viewModel.operationCalculate(input.filterNotNull(), "-")
                    flowCalculate += " Operasi\t : Pengurangan(-)\n"
                    flowCalculate += inputValue("-")
                }
                R.id.btnKali -> {
                    viewModel.operationCalculate(input.filterNotNull(), "*")
                    flowCalculate += " Operasi\t : Perkalian(x)\n"
                    flowCalculate += inputValue("x")
                }
                R.id.btnBagi -> {
                    viewModel.operationCalculate(input.filterNotNull(), "/")
                    flowCalculate += " Operasi\t : Pembagian(/)\n"
                    flowCalculate += inputValue("/")
                }
            }
            binding.txtCalculate.text = flowCalculate
            hideSoftKeyboard(requireActivity())
        } else {
            Snackbar.make(
                binding.root,
                "Masukan minimal 2 inputan dan checklist",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    private fun inputValue(operation: String): String {
        var value = ""
        var hasil = ""
        input.forEachIndexed { index, d ->
            value += " Input ${index + 1}\t : $d\n"
        }
        input.filterNotNull().forEachIndexed { index, d ->
            hasil += if (index == 0) {
                "$d"
            } else {
                " $operation $d"
            }
        }
        return "$value Cara menghitung\t : $hasil\n"
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.result.removeObservers(viewLifecycleOwner)
    }
}