import androidx.lifecycle.ViewModel
import com.example.userapp.User

object UserSingleton : ViewModel() {
    val registeredUsers = mutableListOf<User>()
}
