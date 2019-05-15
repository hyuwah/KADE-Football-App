package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.muhammadwahyudin.kadefootballapp.R

class PreviousMatchFragment : Fragment() {

    companion object {
        fun newInstance() = PreviousMatchFragment()
    }

    private lateinit var viewModel: PreviousMatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.previous_match_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PreviousMatchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
