package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.muhammadwahyudin.kadefootballapp.R
import kotlinx.android.synthetic.main.next_match_fragment.*

class NextMatchFragment(var data: String? = null) : Fragment() {

    private lateinit var viewModel: NextMatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data?.let {
            tv_test_next.text = it
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NextMatchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
