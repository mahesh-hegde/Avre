package hegde.mahesh.avre.ui.startup_scripts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartupScriptsViewModel extends ViewModel {

    public static final String TO_BE_IMPLEMENTED = "To be implemented";
    private final MutableLiveData<String> mText;

    public StartupScriptsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(TO_BE_IMPLEMENTED);
    }

    public LiveData<String> getText() {
        return mText;
    }
}