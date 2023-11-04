package com.marneux.marneweather.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.marneux.marneweather.ui.home.HomeViewModel
import com.marneux.marneweather.ui.navigation.Navigation
import com.marneux.marneweather.ui.theme.MarneTheme
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope


class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope: Scope by activityRetainedScope()
    private val homeViewModel: HomeViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !homeViewModel.isReady.value
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MarneTheme {
                // Envuelve el contenido de tu UI con Surface para aplicar el tema correctamente
                Surface {
                    // Navegación composable que maneja la navegación en tu aplicación
                    Navigation()
                }
            }
        }
    }
}