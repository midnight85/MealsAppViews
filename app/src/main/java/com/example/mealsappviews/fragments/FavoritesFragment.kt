package com.example.mealsappviews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mealsappviews.R
import com.example.mealsappviews.activities.MainActivity
import com.example.mealsappviews.adapters.FavoritesAdapter
import com.example.mealsappviews.databinding.FragmentFavoritesBinding
import com.example.mealsappviews.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycleView()
        observeFavorites()
        onFavoriteItemClick()

        val itemTouchHelper=object:ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedMeal = favoritesAdapter.differ.currentList[position]
                viewModel.deleteMeal(swipedMeal)
                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.upsertMeal(swipedMeal)
                    }
                ).show()
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recFavorites)

    }

    private fun prepareRecycleView() {
        favoritesAdapter = FavoritesAdapter()
        binding.recFavorites.apply {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner) { meals ->
            if (meals.isEmpty()){
                binding.tvNoMeals.visibility=TextView.VISIBLE
            }
            binding.tvNoMeals.visibility=TextView.GONE
            favoritesAdapter.differ.submitList(meals)
        }
    }

    private fun onFavoriteItemClick() {
        favoritesAdapter.onItemClick = {
            val args = Bundle()
            args.putString("MEAL_ID", it.idMeal)
            args.putString("MEAL_NAME", it.strMeal)
            args.putString("MEAL_THUMB", it.strMealThumb)
            findNavController().navigate(R.id.action_favoritesFragment_to_mealDetailsFragment, args)
        }
    }

}