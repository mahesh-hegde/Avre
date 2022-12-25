package hegde.mahesh.avre.ui.startup_scripts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import hegde.mahesh.avre.databinding.FragmentStartupScriptsBinding;

public class StartupScriptsFragment extends Fragment {

    private FragmentStartupScriptsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StartupScriptsViewModel startupScriptsViewModel =
                new ViewModelProvider(this).get(StartupScriptsViewModel.class);

        binding = FragmentStartupScriptsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        startupScriptsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}