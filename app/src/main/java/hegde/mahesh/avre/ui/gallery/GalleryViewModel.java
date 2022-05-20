package hegde.mahesh.avre.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    public static final String TO_BE_IMPLEMENTED = "To be implemented";
    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(TO_BE_IMPLEMENTED);
    }

    public LiveData<String> getText() {
        return mText;
    }
}