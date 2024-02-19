package com.example.mealsappviews.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.mealsappviews.R
import com.example.mealsappviews.activities.MainActivity
import com.example.mealsappviews.adapters.CategoriesAdapter
import com.example.mealsappviews.adapters.MostPopularAdapter
import com.example.mealsappviews.databinding.FragmentHomeBinding
import com.example.mealsappviews.fragments.bottomsheet.MealBottomSheetFragment
import com.example.mealsappviews.pojo.Category
import com.example.mealsappviews.pojo.Meal
import com.example.mealsappviews.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.mealsappviews.fragments.idMeal"
        const val MEAL_NAME = "com.example.mealsappviews.fragments.mealName"
        const val MEAL_THUMB = "com.example.mealsappviews.fragments.mealThumb"
        const val CATEGORY_NAME = "com.example.mealsappviews.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemsRecycleView()
        prepareCategoriesRecycleView()

        binding.imgRandomMeal.setImageDrawable(null)
        Glide.with(this).clear(binding.imgRandomMeal)
        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItems()
        onPopularItemClick()
        popularItemsAdapter.onItemLongClick={
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal info")
        }

        viewModel.getCategories()
        observeCategoryList()
        onCategoryItemClick()
        onSearchIconClick()
    }

    private fun preparePopularItemsRecycleView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun prepareCategoriesRecycleView() {
        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val args = Bundle()
            args.putString("MEAL_ID", randomMeal.idMeal)
            args.putString("MEAL_NAME", randomMeal.strMeal)
            args.putString("MEAL_THUMB", randomMeal.strMealThumb)
            findNavController().navigate(R.id.action_homeFragment_to_mealDetailsFragment, args)
//            val intent = Intent(activity, MealDetailsActivity::class.java)
//            intent.putExtra(MEAL_ID, randomMeal.idMeal)
//            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
//            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
//            startActivity(intent)
        }
    }
    private fun onSearchIconClick(){
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }
    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val args = Bundle()
            args.putString("MEAL_ID", meal.idMeal)
            args.putString("MEAL_NAME", meal.strMeal)
            args.putString("MEAL_THUMB", meal.strMealThumb)
            findNavController().navigate(R.id.action_homeFragment_to_mealDetailsFragment, args)

        }
    }

    private fun onCategoryItemClick() {
        categoriesAdapter.onItemClick = { category ->
            val args = Bundle()
            args.putString("CATEGORY_NAME", category.strCategory)
            findNavController().navigate(R.id.action_homeFragment_to_categoryMealsFragment, args)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) { value ->
            binding.imgLoader.visibility = View.VISIBLE
            Glide.with(this@HomeFragment)
                .load(value.strMealThumb).transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions().skipMemoryCache(true))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide loader on image load failure
                        binding.imgLoader.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide loader on successful image load
                        binding.imgLoader.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.imgRandomMeal)
            this.randomMeal = value
        }
    }

    private fun observePopularItems() {
        viewModel.observePopularItemsLiveData().observe(
            viewLifecycleOwner
        ) { mealsList ->
            popularItemsAdapter.setMealsList(mealsList = mealsList as ArrayList<Meal>)
        }
    }

    private fun observeCategoryList() {
        viewModel.observeCategoryListLiveData().observe(
            viewLifecycleOwner
        ) { categoryList ->
            categoriesAdapter.setCategoryList(categoriesList = categoryList as ArrayList<Category>)
        }
    }
}