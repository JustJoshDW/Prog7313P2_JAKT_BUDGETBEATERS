package com.jakt.jaktprog7313budgetbeaters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Menu_NavFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Menu_NavFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu__nav, container, false)

        view.findViewById<Button>(R.id.viewPieChartBtn).setOnClickListener {
            startActivity(Intent(requireContext(), PieChartActivity::class.java))
        }

        view.findViewById<Button>(R.id.viewAllExpensesBtn).setOnClickListener {
            startActivity(Intent(requireContext(), ViewAllExpensesActivity::class.java))
        }

        view.findViewById<Button>(R.id.viewDailySpendingBtn).setOnClickListener {
            startActivity(Intent(requireContext(), ViewAllSpendingActivity::class.java))
        }

        view.findViewById<Button>(R.id.viewProgressDashboardBtn).setOnClickListener {
            startActivity(Intent(requireContext(), ProgressDashboardActivity::class.java))
        }

        view.findViewById<Button>(R.id.sharedBudgetingBtn).setOnClickListener {
            startActivity(Intent(requireContext(), SharedBudgetingActivity::class.java))
        }

        return view
    }
}
