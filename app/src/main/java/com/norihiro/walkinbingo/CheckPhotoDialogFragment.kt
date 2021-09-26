package com.norihiro.walkinbingo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.*
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.norihiro.walkinbingo.databinding.FragmentCheckPhotoDialogBinding
import java.lang.ClassCastException
import java.lang.IllegalStateException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "uri"
private const val ARG_PARAM2 = "param2"

sealed class DialogState<T : DialogFragment> {
    data class Ok<T : DialogFragment>(val dialog: T) : DialogState<T>()
    data class Cancel<T : DialogFragment>(val dialog: T) : DialogState<T>()
    data class Dismiss<T : DialogFragment>(val dialog: T) : DialogState<T>()
}

/**
 * A simple [Fragment] subclass.
 * Use the [CheckPhotoDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckPhotoDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentCheckPhotoDialogBinding
    internal lateinit var listener: CheckPhotoDialogListener

    interface CheckPhotoDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDialogDismiss(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as CheckPhotoDialogListener
        } catch (e: ClassCastException) {
          throw ClassCastException(("$context must implement CheckPhotoDialogListener"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
        // Inflate the layout for this fragment
//        _binding = FragmentCheckPhotoDialogBinding.inflate(inflater, container, false)
//        val view = binding.root
//        return view
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding = FragmentCheckPhotoDialogBinding.inflate(inflater)

//            val source = ImageDecoder.createSource()
//            binding.imageView.setImageURI(Uri.parse(param1))

//            builder.setView(binding.root)
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, which ->
//                    listener.onDialogPositiveClick(this)
//                    dialog.dismiss()
                    viewModel.state.value = DialogState.Ok(this)
                })
//                .setNegativeButton("Cancel",
//                DialogInterface.OnClickListener { dialog, which ->
////                    listener.onDialogNegativeClick(this)
//                    dialog.cancel()
//                })
//                .setNeutralButton("OK",
//                    DialogInterface.OnClickListener { dialog, which ->
//                        dialog.dismiss()
//                    })
//            if (args.labelName != null){
//                builder.setTitle("${args.labelName}を見つけた")
//            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.state.value = DialogState.Cancel(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.state.value = DialogState.Dismiss(this)
    }

    companion object {
        private const val TAG = "CheckPhotoDialogFragment"
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
            CheckPhotoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}