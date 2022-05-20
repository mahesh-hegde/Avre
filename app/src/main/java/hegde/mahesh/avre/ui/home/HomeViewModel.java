package hegde.mahesh.avre.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public static final String TO_BE_IMPLEMENTED = "To be implemented";
    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(TO_BE_IMPLEMENTED);
    }

    public LiveData<String> getText() {
        return mText;
    }
}