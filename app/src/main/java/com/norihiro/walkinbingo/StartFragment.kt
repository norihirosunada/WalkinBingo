package com.norihiro.walkinbingo

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.norihiro.walkinbingo.databinding.FragmentStartBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

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
        Log.d(TAG, "onCreatedView()")

        // Inflate the layout for this fragment
        return FragmentStartBinding.inflate(inflater, container, false)
            .apply {
                hardMode.run {
                    setOnClickListener {
                        viewModel.run {
                            initBingo("hard")
                            findNavController().navigate(R.id.start_Bingo)
                        }
                    }
                }
                newgameButton.run {
                    setOnClickListener {
                        viewModel.run {
                            initBingo("easy")
                            val apple = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                                    "://" + resources.getResourcePackageName(R.drawable.fruit_apple)
                                    + '/' + resources.getResourceTypeName(R.drawable.fruit_apple)
                                    + '/' + resources.getResourceEntryName(R.drawable.fruit_apple)).toString()
                            val banana = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                                        "://" + resources.getResourcePackageName(R.drawable.fruit_banana)
                                        + '/' + resources.getResourceTypeName(R.drawable.fruit_banana)
                                        + '/' + resources.getResourceEntryName(R.drawable.fruit_banana)).toString()
                            val grape = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                                    "://" + resources.getResourcePackageName(R.drawable.fruit_grape)
                                    + '/' + resources.getResourceTypeName(R.drawable.fruit_grape)
                                    + '/' + resources.getResourceEntryName(R.drawable.fruit_grape)).toString()
//                            setHit("Smile", apple)
//                            setHit("Bird", apple)
//                            setHit("Cat", apple)
//                            setHit("Dog", banana)
//                            setHit("Eating", banana)
//                            setHit("Food", banana)
//                            setHit("Mobile phone", grape)
//                            setHit("Toy", grape)
//                            setHit("Ice", grape)
                        }
                        findNavController().navigate(R.id.start_Bingo)
                    }
                }
            }
            .run {
                root
            }
    }

    companion object {
        private const val TAG = "StartFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}