package com.muhammadwahyudin.kadefootballapp.views.leaguedetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.gone
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_league_detail.*
import org.jetbrains.anko.childrenRecursiveSequence

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LEAGUE_DETAIL = "league_detail"

/**
 * A simple [Fragment] subclass.
 *
 */
class LeagueDetailFragment : Fragment() {

    private var league: LeagueDetailRes.League? = null

    companion object {
        private const val LEAGUE = "league_arg"
        fun newInstance(league: LeagueDetailRes.League?) = LeagueDetailFragment().apply {
            arguments = bundleOf(LEAGUE to league)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_league_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        league = arguments?.get(LEAGUE) as LeagueDetailRes.League?
        league?.let {
            view.childrenRecursiveSequence().forEach { view ->
                view.visible()
            }
            progressbar_league_info.gone()
            Picasso.get().load(it.strBanner).into(iv_banner)
            Picasso.get().load(it.strTrophy).into(iv_trophy)
            tv_league_desc.text = it.strDescriptionEN
            tv_formed_year.text = "Year Founded: " + it.intFormedYear
            tv_website.text = it.strWebsite
            tv_facebook.text = it.strFacebook
            tv_twitter.text = it.strTwitter
            tv_youtube.text = it.strYoutube
        }
    }


}
