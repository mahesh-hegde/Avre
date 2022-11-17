package hegde.mahesh.avre.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import hegde.mahesh.avre.adapters.HistoryAdapter;
import hegde.mahesh.avre.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private int lastVisibleHistoryStart = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HistoryAdapter adapter = new HistoryAdapter(getActivity(), homeViewModel.getHistoryItems());
        binding.recyclerViewHistory.setAdapter(adapter);
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel.getVisibleHistoryStart().observe(getViewLifecycleOwner(), visibleHistoryStart -> {
            adapter.notifyItemRangeChanged(lastVisibleHistoryStart, visibleHistoryStart);
            lastVisibleHistoryStart = visibleHistoryStart;
        });

        homeViewModel.getLastAppendedPosition().observe(getViewLifecycleOwner(), last -> {
            if (last != -1) {
                adapter.notifyItemInserted(last);
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

        return root;
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