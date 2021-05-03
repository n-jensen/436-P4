package com.example.p4

//import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.one_fragment.*

interface OneCom{
    fun GetOneResponse() : String
}

class OneFragment : Fragment() {

    companion object {
        fun newInstance() = OneFragment()
    }

    private lateinit var viewModel: OneViewModel
    var activityCallback: MainActivity? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            activityCallback = context as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
                        + " must implement OneCom interface"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.one_fragment, container, false)

        val getbtn = view.findViewById<Button>(R.id.get_btn)
        getbtn.setOnClickListener {
            GetOneResponse()
        }

        activityCallback?.ChangeGlobalCountry("USA")

        return view
    }

    fun GetOneResponse(){
        var usResponse : String? = ""
        usResponse = activityCallback?.GetOneResponse()
        editTextView(usResponse)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OneViewModel::class.java)
    }


    //fragment editing functions
    fun editTextView(result : String?)
    {
        if(result == null)
            confirmed_text.text = "no response"
        else if(result != null)
            confirmed_text.text = result
    }
}