package com.example.uas_ml2_221351089.ui.simulation

import android.content.res.AssetFileDescriptor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_ml2_221351089.databinding.FragmentSimulationBinding
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.flex.FlexDelegate
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SimulationFragment : Fragment() {

    private var _binding: FragmentSimulationBinding? = null
    private val binding get() = _binding!!
    private lateinit var tflite: Interpreter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimulationBinding.inflate(inflater, container, false)

        // Buat FlexDelegate dan attach ke Interpreter Options
        val options = Interpreter.Options().apply {
            addDelegate(FlexDelegate())
        }

        // Load model dari assets
        tflite = Interpreter(loadModelFile("gold_price.tflite"), options)

        // Tombol prediksi ditekan
        binding.buttonPredict.setOnClickListener {
            predictGoldPrice()
        }

        return binding.root
    }

    private fun predictGoldPrice() {
        try {
            // Ambil input dari user
            val inputs = floatArrayOf(
                binding.inputYear.text.toString().toFloat(),
                binding.inputMonth.text.toString().toFloat(),
                binding.inputAdjClose.text.toString().toFloat(),
                binding.inputHigh.text.toString().toFloat(),
                binding.inputLow.text.toString().toFloat(),
                binding.inputOpen.text.toString().toFloat(),
                binding.inputGdxLow.text.toString().toFloat(),
                binding.inputGdxClose.text.toString().toFloat(),
                binding.inputGdxHigh.text.toString().toFloat(),
                binding.inputGdxAdjClose.text.toString().toFloat(),
                binding.inputGdxOpen.text.toString().toFloat(),
                binding.inputSfPrice.text.toString().toFloat()
            )

            // Format input ke dalam bentuk yang diminta model (1, 12, 1)
            val modelInput = Array(1) { Array(inputs.size) { FloatArray(1) } }
            for (i in inputs.indices) {
                modelInput[0][i][0] = inputs[i]
            }

            val output = Array(1) { FloatArray(1) }

            // Jalankan prediksi
            tflite.run(modelInput, output)

            // Tampilkan hasil
            val result = output[0][0]
            binding.textResult.text = "Prediksi Harga Emas: $result"

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Input tidak valid: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Fungsi untuk load model dari folder assets
    private fun loadModelFile(filename: String): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = requireContext().assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
