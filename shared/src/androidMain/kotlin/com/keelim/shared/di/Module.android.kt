
import android.content.Context
import com.keelim.shared.data.UserStateStore
import okio.FileSystem

actual class Factory(private val context: Context) {
    actual fun createUserStateStore(): UserStateStore {
        return UserStateStore(
            fileSystem = FileSystem.SYSTEM,
        ) {
            context.filesDir.resolve(
                "userState.json",
            ).absolutePath
        }
    }
}
