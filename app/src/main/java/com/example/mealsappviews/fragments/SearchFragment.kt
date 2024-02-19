package com.example.mealsappviews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealsappviews.R
import com.example.mealsappviews.activities.MainActivity
import com.example.mealsappviews.adapters.SearchAdapter
import com.example.mealsappviews.databinding.FragmentSearchBinding
import com.example.mealsappviews.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var searchAdapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycleView()
        observeSearchResult()
        viewModel.resetSearchResult()
        var searchJob:Job?=null
        binding.edSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob=lifecycleScope.launch {
                delay(500)
                viewModel.getMealsBySearch(it.toString())
            }
        }
        searchAdapter.onItemClick={
            val args = Bundle()
            args.putString("MEAL_ID", it.idMeal)
            args.putString("MEAL_NAME", it.strMeal)
            args.putString("MEAL_THUMB", it.strMealThumb)
            findNavController().navigate(R.id.mealDetailsFragment,args)
        }
    }

    private fun prepareRecycleView(){
        searchAdapter=SearchAdapter()
        binding.recSearchResultList.apply {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            adapter=searchAdapter
        }
    }
    private fun observeSearchResult(){
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner){meals->
            searchAdapter.differ.submitList(meals)
        }
    }

}