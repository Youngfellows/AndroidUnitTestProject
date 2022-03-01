package foodapp.com.foodapp.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import foodapp.com.foodapp.R
import kotlinx.android.synthetic.main.layout_placeholder_view.view.*


/**
 * 错误页面
 */
class ErrorView : FrameLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize()
    }

    private fun initialize() {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        View.inflate(context, R.layout.layout_placeholder_view, this)
    }

    /**
     * 显示空视图
     * @param show 是否显示
     */
    fun showEmptyView(show: Boolean = true) {
        errorImageView.visibility = if (show) View.VISIBLE else View.GONE
        titleTextView.visibility = if (show) View.VISIBLE else View.GONE
        descriptionTextView.visibility = if (show) View.VISIBLE else View.GONE
        actionButtonContainer.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 实在空视图
     * @param imageRes 显示图片资源
     * @param title
     * @param subtitle
     * @param buttonTitle
     * @param action 回调函数
     */
    fun setContents(
        imageRes: Int,
        title: Int,
        subtitle: Int,
        buttonTitle: Int,
        action: () -> (Unit)
    ) {
        errorImageView.setImageResource(imageRes)
        titleTextView.text = context.getString(title)
        descriptionTextView.text = context.getString(subtitle)
        actionButton.text = context.getString(buttonTitle)

        actionButton.setOnClickListener {
            action()
        }
    }

    /**
     * 显示加载
     * @param show 是否显示加载
     */
    fun showProgress(show: Boolean = true) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        errorImageView.visibility = View.GONE
        titleTextView.visibility = View.GONE
        descriptionTextView.visibility = View.GONE
        actionButtonContainer.visibility = View.GONE
    }
}