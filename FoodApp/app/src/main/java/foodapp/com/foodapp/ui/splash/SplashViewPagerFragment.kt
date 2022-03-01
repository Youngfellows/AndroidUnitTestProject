package foodapp.com.foodapp.ui.splash

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import coil.api.load
import coil.transform.CircleCropTransformation
import foodapp.com.foodapp.base.BaseFragment
import foodapp.com.foodapp.databinding.FragmentHeroImageBinding

/**
 * 每一个具体的图片轮播页
 */
class SplashViewPagerFragment : BaseFragment<FragmentHeroImageBinding>() {

    /**
     * 伴生对象,静态方法和属性
     */
    companion object {

        const val ARG_IMAGE_RES = "image_res"

        const val ARG_IMAGE_URL = "image_url"

        /**
         * 静态方法
         * @param imageRes 本地图片
         * @param imageUrl 网络图片
         * @return
         */
        fun newInstance(
            @DrawableRes imageRes: Int = -1,
            imageUrl: String = ""
        ): SplashViewPagerFragment {
            val fragment = SplashViewPagerFragment()
            val bundle = Bundle()
            if (imageRes != -1) {
                bundle.putInt(ARG_IMAGE_RES, imageRes)
            }
            if (!TextUtils.isEmpty(imageUrl)) {
                bundle.putString(ARG_IMAGE_URL, imageUrl)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireArguments().containsKey(ARG_IMAGE_RES)) {
            val imageRes = requireArguments().getInt(ARG_IMAGE_RES)

            binding.heroImageView.load(imageRes) {
                CircleCropTransformation()
            }
            return
        }

        if (requireArguments().containsKey(ARG_IMAGE_URL)) {
            val imageUrl = requireArguments().getString(ARG_IMAGE_URL)
            ViewCompat.setTransitionName(binding.heroImageView, imageUrl)

            binding.heroImageView.load(imageUrl)

            view.setOnClickListener {
                activity?.let {
                    val p1 = androidx.core.util.Pair.create<View, String>(
                        binding.heroImageView,
                        imageUrl
                    )
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it, p1)
                    //startActivity(DishDetailsActivity.newInstance(it, imageUrl), options.toBundle())
                }
            }
            return
        }
    }

    /**
     * 为Fragment页面绑定ViewBinding
     * @param inflater
     * @param container
     * @param bundle
     * @return
     */
    override fun getBinding(
        inflater: LayoutInflater, container: ViewGroup?,
        bundle: Bundle?
    ) = FragmentHeroImageBinding.inflate(inflater, container, false)
}