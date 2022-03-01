package foodapp.com.foodapp.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Fragment基类
 * @param VB
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected val TAG: String = this.javaClass.simpleName

    /**
     * ViewBinding
     */
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView::")
        _binding = getBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    /**
     * 绑定ViewBinding
     * @param inflater
     * @param container
     * @param bundle
     * @return
     */
    protected abstract fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?, bundle: Bundle?
    ): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}