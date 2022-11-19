package hegde.mahesh.avre.ui.home;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import hegde.mahesh.avre.R;
import hegde.mahesh.avre.adapters.HistoryAdapter;
import hegde.mahesh.avre.databinding.FragmentHomeBinding;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HistoryAdapter adapter = new HistoryAdapter(getActivity(), homeViewModel.getHistoryItems());
        binding.recyclerViewHistory.setAdapter(adapter);
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel.getVisibleHistoryStart().observe(getViewLifecycleOwner(), adapter::setBegin);

        homeViewModel.getHistorySize().observe(getViewLifecycleOwner(), size -> {
            if (size != 0) {
                adapter.notifyItemAppended();
            }
        });

        homeViewModel.getCurrentSnippet().observe(getViewLifecycleOwner(), text -> {
            binding.textCode.setText(text);
            binding.textCode.setSelection(text.length());
        });

        homeViewModel.setInterpreterVariable("context", getContext());
        homeViewModel.setInterpreterVariable("activity", getActivity());

        binding.buttonEval.setOnClickListener(v -> {
            String code = binding.textCode.getText().toString();
            homeViewModel.eval(code);
            binding.textCode.setText("");
            adjustScroll();
        });

        binding.textCode.addOnLayoutChangeListener((v, left, top, right, bottom,
                oldLeft, oldTop, oldRight, oldBottom) -> adjustScroll());

        binding.buttonClear.setOnClickListener(v -> binding.textCode.setText(""));

        binding.buttonUp.setOnClickListener(v -> homeViewModel.previousSnippet());
        binding.buttonDown.setOnClickListener(v -> homeViewModel.nextSnippet());

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.clear_history) {
            homeViewModel.clearVisibleHistory();
        } else if (itemId == R.id.reset_interpreter) {
            homeViewModel.reset();
        } else {
            throw new RuntimeException("No handler for menu item id " + itemId);
        }
        return true;
    }

    private void adjustScroll() {
        binding.scrollerAvrePrompt.post(() -> {
            binding.textCode.requestFocus();
            binding.scrollerAvrePrompt.fullScroll(View.FOCUS_DOWN);
       });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}