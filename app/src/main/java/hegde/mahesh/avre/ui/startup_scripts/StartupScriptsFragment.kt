package hegde.mahesh.avre.ui.startup_scripts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hegde.mahesh.avre.databinding.FragmentStartupScriptsBinding

class StartupScriptsFragment : Fragment() {
    private var mBinding: FragmentStartupScriptsBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val startupScriptsViewModel = ViewModelProvider(this)[StartupScriptsViewModel::class.java]
        mBinding = FragmentStartupScriptsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView = binding.textGallery
        startupScriptsViewModel.text.observe(viewLifecycleOwner) { text: String? -> textView.text = text }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}