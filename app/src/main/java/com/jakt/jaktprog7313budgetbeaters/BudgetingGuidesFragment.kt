package com.jakt.jaktprog7313budgetbeaters

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class BudgetingGuidesFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budgeting_guides, container, false)

        val linkViews = listOf(
            Pair(R.id.LinkOne, "https://bettermoneyhabits.bankofamerica.com/en/saving-budgeting/creating-a-budget"),
            Pair(R.id.LinkTwo, "https://www.investopedia.com/financial-edge/1109/6-reasons-why-you-need-a-budget.aspx"),
            Pair(R.id.LinkThree, "https://www.youtube.com/watch?v=w_RKtck8XCA"),
            Pair(R.id.LinkFour, "https://www.youtube.com/watch?v=Py3rkSwsbyw")
        )

        for ((id, url) in linkViews) {
            val textView = view.findViewById<TextView>(id)
            val spannable = SpannableString(url)
            Linkify.addLinks(spannable, Linkify.WEB_URLS)
            textView.text = spannable
            textView.setLinkTextColor(Color.WHITE)
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        return view
    }
}
