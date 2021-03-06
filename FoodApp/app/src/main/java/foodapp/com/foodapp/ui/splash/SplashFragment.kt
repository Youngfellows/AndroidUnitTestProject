package foodapp.com.foodapp.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import dagger.hilt.android.AndroidEntryPoint
import foodapp.com.foodapp.R
import foodapp.com.foodapp.base.BaseFragment
import foodapp.com.foodapp.databinding.FragmentSplashBinding
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var mAdapter: SplashViewPager

    @Inject
    lateinit var mPageTransformer: ViewPager.PageTransformer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated:: ")
        binding.splashViewPager.setPageTransformer(true, mPageTransformer)
        binding.splashViewPager.adapter = mAdapter
        binding.tabLayout.setupWithViewPager(binding.splashViewPager, true)

        binding.startButton.setOnClickListener {
            animateButton()
            fadeTextAndShowProgress()
            handleNext()
        }
    }

    private fun handleNext() {
        Handler().postDelayed({
            //隐藏加载对话框动画
            binding.progressBar.animate().alpha(0f).setDuration(200).start()

            Handler().postDelayed({
                findNavController().navigate(R.id.splash_to_dashboard)
            }, 100)
        }, 2000)
    }

    private fun getFabWidth(): Int {
        return resources.getDimension(R.dimen.fab_size).toInt()
    }

    /**
     *淡入淡出文本,并且显示加载进度
     */
    private fun fadeTextAndShowProgress() {
        binding.buttonText.animate().alpha(0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    showProgressDialog()
                }
            })
            .start()
    }

    /**
     * 显示加载
     */
    private fun showProgressDialog() {
        binding.progressBar.alpha = 1f
        binding.progressBar.indeterminateDrawable
            .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * Button做动画
     */
    private fun animateButton() {
        val anim = ValueAnimator.ofInt(binding.startButton.measuredWidth, getFabWidth())

        anim.addUpdateListener { valueAnimator ->
            //更新Button的大小
            val value = valueAnimator.animatedValue as Int
            val layoutParams = binding.startButton.layoutParams
            layoutParams.width = value
            binding.startButton.requestLayout()
        }
        anim.duration = 250
        anim.start()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) =
        FragmentSplashBinding.inflate(inflater, container, false)
}
