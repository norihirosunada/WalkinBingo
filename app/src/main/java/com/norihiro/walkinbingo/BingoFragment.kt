package com.norihiro.walkinbingo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.norihiro.walkinbingo.databinding.FragmentBingoBinding
import com.norihiro.walkinbingo.databinding.FragmentCheckPhotoDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BingoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BingoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBingoBinding? = null
    private val binding get() = _binding!!

    val words = listOf("りんご", "バナナ", "ぶどう", "メロン", "スイカ", "さくらんぼ", "みかん", "もも", "パイナップル", "キウイ", "イチゴ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // 設定した requestKey を元にbundleを受け取る
        setFragmentResultListener("request_key") { requestKey, bundle ->
            val result1 = bundle.getString("result_key1") // "result1"
            val result2 = bundle.getInt("result_key2")    // 222


            Log.d("fragmentResultListener", "RESULT_OK return:$result1")
            binding.textView6.text = result1
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBingoBinding.inflate(inflater, container, false)
        val view = binding.root

        val shuffledWords = words.shuffled()

        Log.d("debug", "binding.tableLayout.childCount = " + binding.tableLayout.childCount)
        val tableLayout = binding.tableLayout
        for (i in 0 until tableLayout.childCount){
            val tableRow = tableLayout.getChildAt(i) as TableRow
            Log.d("debug", "tableRow.childCount = " + tableRow.childCount)
            for (j in 0 until tableRow.childCount){
                val square = tableRow.getChildAt(j) as TextView
//                各マスの初期化
                square.text = shuffledWords[i*3+j]

//                各マスのClickListener
                square.setOnClickListener {
                    if (checkHit(it)){
                        Log.d("Square: $it", "checkHit() is True")
                        (it as TextView).text = "Hit"
                    }
                    checkBingo()
                }
            }
        }

        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.action_bingoFragment_to_permissionFragment)
        }

        return view
    }

    private fun checkBingo() {
        var hits = mutableListOf<Boolean>()
        val tableLayout = binding.tableLayout
        for (i in 0 until tableLayout.childCount){
            val tableRow = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until tableRow.childCount){
                val square = tableRow.getChildAt(j) as TextView
                hits.add(square.text == "Hit")
            }
        }
        Log.d("checkBingo()", "hits[] = $hits")

        var dialog = ""

        if (hits[0] && hits[1] && hits[2]) dialog += "BINGO123"
        if (hits[3] && hits[4] && hits[5]) dialog += "BINGO456"
        if (hits[6] && hits[7] && hits[8]) dialog += "BINGO789"
        if (hits[0] && hits[3] && hits[6]) dialog += "BINGO147"
        if (hits[1] && hits[4] && hits[7]) dialog += "BINGO258"
        if (hits[2] && hits[5] && hits[8]) dialog += "BINGO369"
        if (hits[0] && hits[4] && hits[8]) dialog += "BINGO159"
        if (hits[2] && hits[4] && hits[6]) dialog += "BINGO357"

        binding.textView5.text = dialog
    }

    fun checkHit(view: View): Boolean {
        return true
    }

    companion object {
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
}