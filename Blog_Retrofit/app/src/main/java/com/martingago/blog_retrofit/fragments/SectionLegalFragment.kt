package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.martingago.blog_retrofit.R

class SectionLegalFragment : Fragment() {


    private lateinit var legalText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_section_legal, container, false)
        legalText = view.findViewById(R.id.legal_text)
        legalText.text = HtmlCompat.fromHtml(getString(R.string.privacy_text), HtmlCompat.FROM_HTML_MODE_LEGACY)
        return view
    }

}