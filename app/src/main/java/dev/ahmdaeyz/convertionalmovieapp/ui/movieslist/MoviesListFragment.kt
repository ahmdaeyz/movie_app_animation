package dev.ahmdaeyz.convertionalmovieapp.ui.movieslist

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import dev.ahmdaeyz.convertionalmovieapp.R
import dev.ahmdaeyz.convertionalmovieapp.model.movies
import kotlin.math.abs


class MoviesListFragment : Fragment() {

    private var ticketBtnClicked: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movies_list_fragment, container, false)
        var gotToStartOfTheScreen = false
        val displayMetrics = DisplayMetrics()
        val screenWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().windowManager.currentWindowMetrics.bounds.width()
        } else {
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }

        val postersAdapter = BackgroundPostersPagerAdapter()
        postersAdapter.setHasStableIds(true)
        postersAdapter.submitList(movies.map { it.bgResId })
        val bgPostersPager: RecyclerView = view.findViewById(R.id.bg_posters_pager)
        bgPostersPager.adapter = postersAdapter
        // disabling touch events to prevent scrolling of the background posters
        bgPostersPager.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
        })

        val moviesCarouselAdapter = MoviesCarouselAdapter()
        moviesCarouselAdapter.setHasStableIds(true)
        moviesCarouselAdapter.submitList(movies)
        val moviesCarousel: ViewPager2 = view.findViewById(R.id.movies_carousel)
        moviesCarousel.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            val sliderLikeTransformers = CompositePageTransformer()
            prepareSliderLikeTransformers(sliderLikeTransformers)
            setPageTransformer(sliderLikeTransformers)
            adapter = moviesCarouselAdapter
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        moviesCarousel.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    bgPostersPager.smoothScrollToPosition(position)
                    if (ticketBtnClicked) {
                        animatePage(moviesCarousel)
                    }
                    super.onPageSelected(position)
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (gotToStartOfTheScreen) {
                        (moviesCarousel.getChildAt(0) as RecyclerView).translationX = 0f
                        gotToStartOfTheScreen = false
                    }
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }
            })
        val stretchBackTransformers = CompositePageTransformer()
        prepareStretchBackTransformers(screenWidth, stretchBackTransformers)

        val buyTicketsBtn: Button = view.findViewById(R.id.buy_tickets_btn)

        buyTicketsBtn.setOnClickListener {
            ticketBtnClicked = true
            // position the internal recyclerview to the beginning
            // of the screen for realistic stretch of the card
            (moviesCarousel.getChildAt(0) as RecyclerView).translationX =
                -screenWidth * 0.15f
            moviesCarousel.setPageTransformer(stretchBackTransformers)
            gotToStartOfTheScreen = true
            animatePage(moviesCarousel)
        }
        return view
    }

    private fun animatePage(moviesCarousel: ViewPager2) {
        val currentPage = (moviesCarousel.getChildAt(0) as RecyclerView)[moviesCarousel.currentItem]
        val root: ConstraintLayout = currentPage.findViewById(R.id.movie_card_root)
        val actorsList: RecyclerView = root.findViewById(R.id.actors_list)
        val itemMotionLayout: MotionLayout = root.findViewById(R.id.constraintLayout)
        itemMotionLayout.transitionToEnd()
        itemMotionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                animationSequence(p1, itemMotionLayout, actorsList)
            }

            override fun onTransitionTrigger(
                p0: MotionLayout?,
                p1: Int,
                p2: Boolean,
                p3: Float
            ) {
            }
        })
    }

    private fun prepareSliderLikeTransformers(sliderLikeTransformer: CompositePageTransformer) {
        val marginTransformer = MarginPageTransformer(56)
        val alphaAndScaleTransformer =
            ViewPager2.PageTransformer { page, position ->
                val positionIndicator = 1 - abs(position)
                page.scaleY = 1 + (abs(0.15f * positionIndicator) - 0.15f)
                page.alpha = 1 + (abs(0.3f * positionIndicator) - 0.3f)
            }
        sliderLikeTransformer.addTransformer(marginTransformer)
        sliderLikeTransformer.addTransformer(alphaAndScaleTransformer)
    }

    private fun prepareStretchBackTransformers(
        screenWidth: Int,
        stretchBackTransformers: CompositePageTransformer
    ) {
        val newMarginTransformer = MarginPageTransformer(0)
        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            page.scaleY = 1f
            page.updatePadding(left = 0, right = 0)
            page.updateLayoutParams<RecyclerView.LayoutParams> {
                width = screenWidth
                marginEnd = 10
                marginStart = 10
            }
            page.alpha = 1f
        }
        stretchBackTransformers.addTransformer(newMarginTransformer)
        stretchBackTransformers.addTransformer(pageTransformer)
    }

    private fun animationSequence(
        p1: Int,
        itemMotionLayout: MotionLayout,
        actorsList: RecyclerView
    ) {
        // switching on the end state of each transition (transition names are kind of irrelevant :D)
        when (p1) {
            R.id.posterShrink -> {
                itemMotionLayout.setTransition(R.id.details_rise_up_transition)
                itemMotionLayout.transitionToEnd()
            }
            R.id.titleRiseUp -> {
                itemMotionLayout.setTransition(
                    R.id.details_fluctuation_transition
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.titleFlucuation -> {
                itemMotionLayout.setTransition(
                    R.id.third_star_rise_up
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.thirdStarRiseUpEnd -> {
                itemMotionLayout.setTransition(
                    R.id.fourth_star_rise_up
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.fourthStarRiseUpEnd -> {
                itemMotionLayout.setTransition(
                    R.id.fifth_star_rise_up
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.fifthStarRiseUpEnd -> {
                itemMotionLayout.setTransition(
                    R.id.adjustment_1
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.starTwoSettleDown -> {
                itemMotionLayout.setTransition(
                    R.id.adjustment_2
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.starThreeSettleDown -> {
                itemMotionLayout.setTransition(
                    R.id.adjustment_3
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.starFourSettleDown -> {
                itemMotionLayout.setTransition(
                    R.id.adjustment_4
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.starFiveSettleDown -> {
                itemMotionLayout.setTransition(
                    R.id.beginning_of_movie_details
                )
                itemMotionLayout.transitionToEnd()
            }
            R.id.movieDetailsRiseUp -> {
                itemMotionLayout.setTransition(
                    R.id.movie_details_1
                )
                itemMotionLayout.transitionToEnd()
                runLayoutAnimation(actorsList)
            }
            R.id.restOfMovieDetailsRiseUp -> {
                itemMotionLayout.setTransition(
                    R.id.movie_details_2
                )
                itemMotionLayout.transitionToEnd()
            }
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.actors_list_animation_come_up)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}