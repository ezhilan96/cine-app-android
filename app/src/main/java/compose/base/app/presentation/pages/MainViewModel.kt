package compose.base.app.presentation.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.base.app.config.util.NetworkConnectivityObserver
import compose.base.app.config.util.NetworkConnectivityObserver.NetworkStatus.Available
import compose.base.app.config.util.NetworkConnectivityObserver.NetworkStatus.Losing
import compose.base.app.config.util.NetworkConnectivityObserver.NetworkStatus.Lost
import compose.base.app.config.util.NetworkConnectivityObserver.NetworkStatus.UnAvailable
import compose.base.app.config.util.NetworkConnectivityObserver.NetworkStatus.Unknown
import compose.base.app.data.dataSource.local.preference.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ConnectionState { Connected, Disconnected, Unknown }

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val dataStore: UserPreferencesDataStore,
) : ViewModel() {

    private val _connectionState: MutableStateFlow<ConnectionState> =
        MutableStateFlow(ConnectionState.Unknown)
    val connectionState: StateFlow<ConnectionState>
        get() = _connectionState


    private val _loginState: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val loginState: StateFlow<Boolean?>
        get() = _loginState

    init {
        viewModelScope.launch {
            dataStore.isLoggedIn.collect { isLoggedIn ->
                _loginState.emit(isLoggedIn)
            }
        }
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { networkStatus ->
                when (networkStatus) {
                    Unknown, Available -> _connectionState.emit(ConnectionState.Connected)
                    Lost -> _connectionState.emit(ConnectionState.Disconnected)
                    Losing, UnAvailable -> _connectionState.emit(ConnectionState.Unknown)
                }
            }
        }
    }
}