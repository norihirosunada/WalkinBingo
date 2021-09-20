package com.norihiro.walkinbingo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.norihiro.walkinbingo.databinding.FragmentCheckPhotoDialogBinding
import java.lang.IllegalStateException

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "uri"
private const val ARG_PARAM2 = "param2"

class ResultDialogFragment : DialogFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: MainViewModel by activityViewModels()

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
        return inflater.inflate(R.layout.blank_fragment, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle("BINGO")
                .setPositiveButton("End Game",
                DialogInterface.OnClickListener { dialog, which ->
//                    listener.onDialogPositiveClick(this)
//                    dialog.dismiss()
                    findNavController().navigate(R.id.action_resultDialogFragment_to_startFragment)
                })
                .setNegativeButton("Continue",
                DialogInterface.OnClickListener { dialog, which ->
//                    listener.onDialogNegativeClick(this)
                    dialog.cancel()
                })
//                .setNeutralButton("OK",
//                    DialogInterface.OnClickListener { dialog, which ->
//                        dialog.dismiss()
//                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        private const val TAG = "ResultDialogFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckPhotoDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}