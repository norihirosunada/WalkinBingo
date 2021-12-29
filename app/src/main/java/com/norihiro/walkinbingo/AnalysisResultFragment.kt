package com.norihiro.walkinbingo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.norihiro.walkinbingo.databinding.FragmentAnalysisResultBinding
import com.norihiro.walkinbingo.databinding.FragmentCameraBinding
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnalysisResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalysisResultFragment : Fragment() {

    private var _binding: FragmentAnalysisResultBinding? = null
    private val binding get() = _binding!!

    val args: AnalysisResultFragmentArgs by navArgs()

    private val viewModel: MainViewModel by activityViewModels()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAnalysisResultBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedUri = Uri.parse(args.savedUri)
        binding.imageView4.setImageURI(savedUri)

        var outputText = ""
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        val image: InputImage
        var labelName = ""
        try {
            image = InputImage.fromFilePath(context, savedUri)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        Log.d("AnalysisResult", "Label: $text, $confidence, $index")
                        outputText += "$text, $confidence, $index \n"
                    }
                    labelName = labels[0].text
                    binding.firstLabel.text = "${labelName}を発見"
                    binding.labelsText.text = outputText
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Log.e("AnalysisResult", "Label: $e")
                    binding.firstLabel.text = "解析失敗"
//                    binding.labelsText.text = outputText
                    binding.submitButton.visibility = View.GONE
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.submitButton.setOnClickListener {

            viewModel.isHit = true

            setFragmentResult("request_key", bundleOf(
                "result_key1" to labelName,
                "result_key2" to savedUri.toString()
            ))
            findNavController().navigate(R.id.action_analysisResultFragment_to_bingoFragment)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnalysisResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnalysisResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}