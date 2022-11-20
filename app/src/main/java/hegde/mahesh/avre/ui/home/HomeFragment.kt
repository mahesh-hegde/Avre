package hegde.mahesh.avre.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hegde.mahesh.avre.R
import hegde.mahesh.avre.adapters.HistoryAdapter
import hegde.mahesh.avre.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adapter = HistoryAdapter(activity, homeViewModel.historyItems)
        binding.recyclerViewHistory.adapter = adapter
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(context)
        homeViewModel.visibleHistoryStart.observe(viewLifecycleOwner) { begin: Int? ->
            adapter.setBegin(
                begin!!
            )
        }
        homeViewModel.historySize.observe(viewLifecycleOwner) { size: Int ->
            if (size != 0) {
                adapter.notifyItemAppended()
            }
        }
        homeViewModel.currentSnippet.observe(viewLifecycleOwner) { text: String ->
            binding.textCode.setText(text)
            binding.textCode.setSelection(text.length)
        }
        homeViewModel.setInterpreterVariable("context", context)
        homeViewModel.setInterpreterVariable("activity", activity)
        binding.buttonEval.setOnClickListener {
            val code = binding.textCode.text.toString()
            homeViewModel.eval(code)
            binding.textCode.setText("")
            adjustScroll()
        }
        binding.textCode.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> adjustScroll() }
        binding.buttonClear.setOnClickListener { binding.textCode.setText("") }
        binding.buttonUp.setOnClickListener { homeViewModel.previousSnippet() }
        binding.buttonDown.setOnClickListener { homeViewModel.nextSnippet() }
        setHasOptionsMenu(true)
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (val itemId = item.itemId) {
            R.id.clear_history -> {
                homeViewModel.clearVisibleHistory()
            }
            R.id.reset_interpreter -> {
                homeViewModel.reset()
            }
            else -> {
                throw RuntimeException("No handler for menu item id $itemId")
            }
        }
        return true
    }

    private fun adjustScroll() {
        binding.scrollerAvrePrompt.post {
            binding.textCode.requestFocus()
            binding.scrollerAvrePrompt.fullScroll(View.FOCUS_DOWN)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}