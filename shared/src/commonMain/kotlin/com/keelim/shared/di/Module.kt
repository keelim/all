import com.keelim.shared.data.UserStateStore
import kotlinx.serialization.json.Json

internal val json = Json { ignoreUnknownKeys = true  }

expect class Factory {
    fun createUserStateStore(): UserStateStore
}
