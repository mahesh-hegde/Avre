package hegde.mahesh.avre.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hegde.mahesh.avre.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var mBinding: FragmentAboutBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        val aboutViewModel = ViewModelProvider(this)[AboutViewModel::class.java]
        mBinding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView = binding.textSlideshow
        aboutViewModel.text.observe(viewLifecycleOwner) { text -> textView.text = text }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}