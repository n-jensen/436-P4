package com.example.p4

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.one_fragment.*

class AllFragment : Fragment() {
    lateinit var countrytext : EditText
    lateinit var datatext : TextView

    companion object {
        fun newInstance() = AllFragment()
    }

    private lateinit var viewModel: AllViewModel
    var activityCallback: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
                        + " must implement AllCom interface"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_fragment, container, false)

        val enterbtn = view.findViewById<Button>(R.id.enter_btn)
        enterbtn.setOnClickListener { GetCountryStatsResponse() }

        countrytext = view.findViewById<TextInputEditText>(R.id.countryText)
        datatext = view.findViewById<TextInputEditText>(R.id.dataText)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllViewModel::class.java)
    }

    fun GetCountryStatsResponse() {
        try {
            var input = countrytext.text
            activityCallback?.ChangeGlobalCountry(input.toString())
            var countryResponse = activityCallback?.GetOneResponse()
            editTextView(countryResponse)
        }
        catch (e: Exception){
            println("Error: " + e.message)
        }
    }

    //fragment editing functions
    fun editTextView(result : String?)
    {
        if(result == null)
            datatext.text = "no response"
        else if(result != null)
            datatext.text = result
    }


}