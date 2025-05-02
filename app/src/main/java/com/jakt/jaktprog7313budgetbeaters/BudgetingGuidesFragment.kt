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

// Fragment that displays budgeting guides with clickable links
class BudgetingGuidesFragment : Fragment() {

    // Suppressing warning related to missing inflated ID (for compatibility purposes)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_budgeting_guides, container, false)

        // List of links to be added to TextViews for budgeting guides
        val linkViews = listOf(
            Pair(R.id.LinkOne, "https://bettermoneyhabits.bankofamerica.com/en/saving-budgeting/creating-a-budget"), // Link 1
            Pair(R.id.LinkTwo, "https://www.investopedia.com/financial-edge/1109/6-reasons-why-you-need-a-budget.aspx"), // Link 2
            Pair(R.id.LinkThree, "https://www.youtube.com/watch?v=w_RKtck8XCA"), // Link 3
            Pair(R.id.LinkFour, "https://www.youtube.com/watch?v=Py3rkSwsbyw") // Link 4
        )

        // Loop through each link and apply it to the corresponding TextView
        for ((id, url) in linkViews) {
            // Find the TextView by its ID
            val textView = view.findViewById<TextView>(id)

            // Create a SpannableString to handle the link text
            val spannable = SpannableString(url)

            // Add clickable links to the text (WEB_URLS)
            Linkify.addLinks(spannable, Linkify.WEB_URLS)

            // Set the text for the TextView with the SpannableString (makes it clickable)
            textView.text = spannable

            // Change the link text color to white
            textView.setLinkTextColor(Color.WHITE)

            // Enable clickable links in the TextView
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        // Return the view for the fragment
        return view
    }
}
