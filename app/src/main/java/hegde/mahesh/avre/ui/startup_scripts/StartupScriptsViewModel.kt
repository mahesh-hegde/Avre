package hegde.mahesh.avre.ui.startup_scripts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartupScriptsViewModel : ViewModel() {
    private val mText: MutableLiveData<String> = MutableLiveData()

    init {
        mText.value = TO_BE_IMPLEMENTED
    }

    val text: LiveData<String>
        get() = mText

    companion object {
        const val TO_BE_IMPLEMENTED = "To be implemented"
    }
}