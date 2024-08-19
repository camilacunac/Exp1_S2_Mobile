import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateListOf
    import androidx.lifecycle.ViewModel
    import com.example.userapp.User

    class UserViewModel : ViewModel() {
        val registeredUsers = mutableStateListOf<User>()
    }
