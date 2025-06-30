package com.example.uas_ml2_221351089.ui.dataset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ml2_221351089.databinding.FragmentDatasetBinding
import com.example.uas_ml2_221351089.ui.model.DatasetItem
import java.io.BufferedReader
import java.io.InputStreamReader

class DatasetFragment : Fragment() {

    private var _binding: FragmentDatasetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatasetBinding.inflate(inflater, container, false)

        val data = loadDatasetFromCSV()

        val columnTitles = listOf("Date", "Adj Close", "High", "Low", "Open", "GDX_Low", "GDX_Close", "GDX_High", "GDX_Adj Close", "GDX_Open", "SF_Price")
        val columnData = List(columnTitles.size) { mutableListOf<String>() }

        // Pisahkan tiap kolom ke list masing-masing
        data.forEach { item ->
            columnData[0].add(item.date)
            columnData[1].add(item.adjClose.toString())
            columnData[2].add(item.high.toString())
            columnData[3].add(item.low.toString())
            columnData[4].add(item.open.toString())
            columnData[5].add(item.gdxLow.toString())
            columnData[6].add(item.gdxClose.toString())
            columnData[7].add(item.gdxHigh.toString())
            columnData[8].add(item.gdxAdjClose.toString())
            columnData[9].add(item.gdxOpen.toString())
            columnData[10].add(item.sfPrice.toString())
        }

        // Atur adapter recycler horizontal
        val adapter = DatasetColumnAdapter(columnTitles, columnData)
        binding.recyclerViewDataset.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewDataset.adapter = adapter

        return binding.root
    }

    private fun loadDatasetFromCSV(): List<DatasetItem> {
        val dataList = mutableListOf<DatasetItem>()

        try {
            val inputStream = requireContext().assets.open("FINAL_USO.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine() // skip header

            reader.forEachLine { line ->
                val tokens = line.split(",")
                if (tokens.size >= 11) {
                    try {
                        val item = DatasetItem(
                            date = tokens[0],
                            adjClose = tokens[1].toDouble(),
                            high = tokens[2].toDouble(),
                            low = tokens[3].toDouble(),
                            open = tokens[4].toDouble(),
                            gdxLow = tokens[5].toDouble(),
                            gdxClose = tokens[6].toDouble(),
                            gdxHigh = tokens[7].toDouble(),
                            gdxAdjClose = tokens[8].toDouble(),
                            gdxOpen = tokens[9].toDouble(),
                            sfPrice = tokens[10].toDouble()
                        )
                        dataList.add(item)
                    } catch (e: Exception) {
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dataList.take(10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
