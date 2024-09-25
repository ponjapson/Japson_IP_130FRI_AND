package com.example.news_japson

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private var isLandscape: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)

        isLandscape = findViewById<View?>(R.id.fragment_detail_container) != null
        Log.d("MainActivity", "Is Landscape: $isLandscape")

        if (savedInstanceState == null) {
            Log.d("MainActivity", "SavedInstanceState is null")
            setupFragments()
        } else {
            Log.d("MainActivity", "SavedInstanceState is not null")
            // Check if fragments are already added
            val fragmentHeadlines = supportFragmentManager.findFragmentById(R.id.fragment_headline_container)
            val fragmentDetails = supportFragmentManager.findFragmentById(R.id.fragment_detail_container)

            if (fragmentHeadlines == null) {
                Log.d("MainActivity", "Adding NewsListFragment")
                val newsListFragment = NewsListFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_headline_container, newsListFragment)
                    .commit()
            }

            if (fragmentDetails == null && isLandscape) {
                Log.d("MainActivity", "Adding default NewsDetailFragment")
                val defaultDetailFragment = NewsDetailFragment.newInstance(
                    "Select a headline", "Details will appear here"
                )
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, defaultDetailFragment)
                    .commit()
            }
        }
    }

    private fun setupFragments() {
        val newsListFragment = NewsListFragment()

        if (isLandscape) {
            Log.d("MainActivity", "Landscape mode detected")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_headline_container, newsListFragment)
                .commit()
            Log.d("MainActivity", "Added NewsListFragment to fragment_headline_container")

            val defaultDetailFragment = NewsDetailFragment.newInstance(
                "Select a headline", "Details will appear here"
            )
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_container, defaultDetailFragment)
                .commit()
            Log.d("MainActivity", "Added NewsDetailFragment to fragment_detail_container")
        } else {
            Log.d("MainActivity", "Portrait mode detected")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsListFragment)
                .commit()
            Log.d("MainActivity", "Loading NewsListFragment into fragment_container")
        }
    }

    fun showDetailsInLandscape(headline: String, content: String) {
        if (isLandscape) {
            val detailFragment = NewsDetailFragment.newInstance(headline, content)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_container, detailFragment)
                .commit()
            Log.d("MainActivity", "Replacing detail fragment in fragment_detail_container")
        }
    }

    class NewsListFragment : Fragment() {

        private val newsHeadlines = arrayOf(
            "Headline 1:  Global Climate Summit Calls for Accelerated Action",
            "Headline 2: Tech Giant Announces Revolutionary AI Advancements",
            "Headline 3: Historic Peace Deal Signed Between Two Rival Nations",
            "Headline 4: Global Stock Markets See Major Fluctuations Amid Recession Fears",
            "Headline 5: Breakthrough in Cancer Treatment Shows Promising Results"
        )

        private val newsContents = arrayOf(
            "At the latest global climate summit in New York, world leaders called for accelerated efforts to tackle the climate crisis. Key agreements were reached to cut greenhouse gas emissions by 40% by 2030, with a focus on clean energy and reforestation. However, disagreements remain over financing for developing nations.",
            "In a major press event, one of the leading tech companies unveiled cutting-edge AI advancements that are set to revolutionize multiple industries. The company revealed new AI-driven tools that promise to enhance automation in sectors like healthcare, finance, and manufacturing, emphasizing both increased efficiency and ethical considerations.",
            "In a major press event, one of the leading tech companies unveiled cutting-edge AI advancements that are set to revolutionize multiple industries. The company revealed new AI-driven tools that promise to enhance automation in sectors like healthcare, finance, and manufacturing, emphasizing both increased efficiency and ethical considerations.",
            "Global stock markets experienced significant volatility this week, as investors reacted to growing concerns of a potential recession. Key indices in the U.S., Europe, and Asia showed sharp declines, particularly in tech and manufacturing sectors. Economists are divided on whether this signals a temporary correction or a longer-term downturn.",
            "A groundbreaking new treatment for cancer has shown promising results in early trials, offering hope for millions of patients. The experimental therapy, which targets genetic mutations in tumor cells, has achieved unprecedented success rates in shrinking tumors without harmful side effects. Experts believe this could mark a new era in personalized cancer treatment."
        )

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.d("NewsListFragment", "onCreateView called")
            val view = inflater.inflate(R.layout.fragment_newslist, container, false)
            val listView = view.findViewById<ListView>(R.id.newsListView)

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                newsHeadlines
            )
            listView.adapter = adapter

            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                Log.d("NewsListFragment", "Item clicked: ${newsHeadlines[position]}")
                val activity = requireActivity() as MainActivity
                val selectedHeadline = newsHeadlines[position]
                val selectedContent = newsContents[position]

                if (activity.isLandscape) {
                    Log.d("NewsListFragment", "Landscape mode: Loading details for \"$selectedHeadline\"")
                    activity.showDetailsInLandscape(selectedHeadline, selectedContent)
                } else {
                    Log.d("NewsListFragment", "Portrait mode: Navigating to details for \"$selectedHeadline\"")
                    val detailFragment = NewsDetailFragment.newInstance(selectedHeadline, selectedContent)
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                    Log.d("NewsListFragment", "Replacing fragment in fragment_container")
                }
            }
            return view
        }
    }

    class NewsDetailFragment : Fragment() {

        private var headline: String? = null
        private var content: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                headline = it.getString(ARG_HEADLINE)
                content = it.getString(ARG_CONTENT)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.d("NewsDetailFragment", "onCreateView called")
            val view = inflater.inflate(R.layout.fragment_newsdetail, container, false)

            val detailTextView = view.findViewById<TextView>(R.id.newsDetailTextView)
            detailTextView.text = headline

            val contentTextView = view.findViewById<TextView>(R.id.newsContentTextView)
            contentTextView.text = content

            return view
        }

        companion object {
            private const val ARG_HEADLINE = "headline"
            private const val ARG_CONTENT = "content"

            @JvmStatic
            fun newInstance(headline: String, content: String) =
                NewsDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_HEADLINE, headline)
                        putString(ARG_CONTENT, content)
                    }
                }
        }
    }
}