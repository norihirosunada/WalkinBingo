package com.norihiro.walkinbingo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.norihiro.walkinbingo.databinding.FragmentBingoBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BingoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BingoFragment : Fragment(), CheckPhotoDialogFragment.CheckPhotoDialogListener{

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var bingoListAdapter: CustomAdapter

    val args: BingoFragmentArgs by navArgs()

    private var _binding: FragmentBingoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Log.d(TAG, "onCreate()")
//        Log.d(TAG, "viewModel.bingoLabels: ${viewModel.bingoCard.value}")

        // 設定した requestKey を元にbundleを受け取る
        setFragmentResultListener("request_key") { requestKey, bundle ->
            val labelName = bundle.getString("result_key1")
            val savedUri = bundle.getString("result_key2")    // 222

//            val action = BingoFragmentDirections.actionBingoFragmentToCheckPhotoDialogFragment(
//                result1
//            )
//            findNavController().navigate(action)

            viewModel.setHit(labelName, savedUri)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")

        // Inflate the layout for this fragment
        _binding = FragmentBingoBinding.inflate(inflater, container, false)

        binding.gridRecyclerView.run {
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            adapter = CustomAdapter(this@BingoFragment.viewModel).also {
                bingoListAdapter = it
            }
            setHasFixedSize(true)
        }

        binding.cameraButton.setOnClickListener {
            findNavController().navigate(R.id.action_bingoFragment_to_permissionFragment)
        }

//        binding.themeText.text = "${viewModel.theme}を探そう"
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated()")
        Log.d(TAG, "BingoLabels: ${viewModel.bingoCard.value}")

        viewModel.run {
            bingoCard.observe(viewLifecycleOwner, {
                bingoListAdapter.submitList(it)
                if (isBingo() && countBingo() > bingoNum) {
                    Log.d(TAG, "Bingo Count: ${viewModel.countBingo()}")
                    // create image from recycler view
                    bingoNum = countBingo()
                    binding.gridRecyclerView.post {
                        val bitmap = binding.gridRecyclerView.drawToBitmap(config = Bitmap.Config.ARGB_8888)
                        val action = BingoFragmentDirections.actionBingoFragmentToResultFragment(bitmap)
                        findNavController().navigate(action)
                    }

                }
            })

            state.observe(viewLifecycleOwner, {
                when (it) {
                    is DialogState.Ok -> {}
                    is DialogState.Cancel -> {}
                    is DialogState.Dismiss -> {
                        Log.d(TAG, "onDialogDismiss()")

                    }
                }
            })
        }

        if (viewModel.isHit){
            viewModel.isHit = false

        }

    }

    companion object {
        private const val TAG = "BingoFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BingoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BingoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun onDialogDismiss(dialog: DialogFragment) {


    }
}