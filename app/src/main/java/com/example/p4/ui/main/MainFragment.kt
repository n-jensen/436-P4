package com.example.p4.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.view.View.inflate
import androidx.fragment.app.Fragment
import android.widget.Button
import com.example.p4.*
import kotlinx.android.synthetic.main.main_activity.*

class MainFragment : Fragment() {
    lateinit var onebtn: Button
    lateinit var allbtn: Button

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    var activityCallback: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(
                    context.toString()
                            + " must implement MenuSwitch interface"
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        onebtn = view.findViewById<Button>(R.id.one_btn)
        onebtn.setOnClickListener { StartNewFragments("OneFragment") }

        allbtn = view.findViewById<Button>(R.id.all_btn)
        allbtn.setOnClickListener { StartNewFragments("AllFragment") }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun StartNewFragments(type: String){
        if(type == "OneFragment"){
            activityCallback?.SwitchView("One")
        }
        else if (type == "AllFragment"){
            activityCallback?.SwitchView("All")
        }
    }

    interface MenuSwitch {
        fun SwitchView(viewLbl : String)
    }
}