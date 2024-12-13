package com.example.dermaaid.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dermaaid.databinding.FragmentResultBinding
import com.example.dermaaid.datariwayat.AppDatabase
import com.example.dermaaid.datariwayat.PredictionHistory
import com.example.dermaaid.helper.ImageClassifierHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.io.FileOutputStream

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var imageUri: Uri? = null

    companion object {
        const val TAG = "imagePicker"
        const val IMAGE_URI = "img_uri"
        const val RESULT_TEXT = "result_text"

        fun newInstance(imageUri: Uri): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString(IMAGE_URI, imageUri.toString())
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        arguments?.getString(IMAGE_URI)?.let {
            imageUri = Uri.parse(it)
            imageUri?.let { uri -> displayImage(uri) }
        }

        val imageClassifierHelper = ImageClassifierHelper(
            contextValue = requireContext(),
            classifierListenerValue = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(errorMessage: String) {
                    Log.d(TAG, "Error: $errorMessage")
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let { showResults(it) }
                }
            }
        )
        imageUri?.let { imageClassifierHelper.classifyImage(it) }

        binding.saveButton.setOnClickListener {
            imageUri?.let { uri ->
                val result = binding.resultText.text.toString()
                savePredictionToDatabase(uri, result)
            } ?: showToast("No image URI provided")
        }

        return binding.root
    }

    private fun displayImage(uri: Uri) {
        Log.d(TAG, "Displaying image: $uri")
        binding.resultImage.setImageURI(uri)
    }

    private fun showResults(results: List<Classifications>) {
        val topResult = results[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }
        binding.resultText.text = "$label ${score.formatToString()}"
    }

    private fun savePredictionToDatabase(imageUri: Uri, result: String) {
        if (result.isNotEmpty()) {
            val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
            val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))
            requireContext().contentResolver.openInputStream(imageUri)?.use { input ->
                FileOutputStream(File(requireContext().cacheDir, fileName)).use { output ->
                    input.copyTo(output)
                }
            }

            val prediction = PredictionHistory(imagePath = destinationUri.toString(), result = result)
            lifecycleScope.launch(Dispatchers.IO) {
                val database = AppDatabase.getDatabase(requireContext())
                try {
                    database.predictionHistoryDao().insertPrediction(prediction)
                    Log.d(TAG, "Prediction saved successfully: $prediction")
                    val predictions = database.predictionHistoryDao().getAllPredictions()
                    Log.d(TAG, "All predictions after save: $predictions")
                    moveToHistory(destinationUri, result)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save prediction: $prediction", e)
                }
            }
        } else {
            Log.e(TAG, "Result is empty, cannot save prediction to database.")
        }
    }

    private fun moveToHistory(imageUri: Uri, result: String) {
        // Handle navigation to history screen if required
        showToast("Data saved")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}