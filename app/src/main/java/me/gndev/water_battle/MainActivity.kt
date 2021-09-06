package me.gndev.water_battle

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water_battle.core.constant.SharePreferences
import me.gndev.water_battle.data.share_preferences.PrefManager
import me.gndev.water_battle.util.CommonExtensions.applySystemBarsInset
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var prefManager: PrefManager

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Water)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.initGame()
        viewModel.currentGame.observe(this, {
            // This will cause the whole app lays out in fullscreen
            if (it != null) {
                findViewById<View>(R.id.nav_host_fragment).applySystemBarsInset()
                val navHostFragment =
                    (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                val inflater = navHostFragment.navController.navInflater
                val graph = inflater.inflate(R.navigation.navigation)
                if (prefManager.getBooleanVal(SharePreferences.IS_FIRST_STARTUP, true)) {
                    graph.startDestination = R.id.firstStartupFragment
                } else {
                    graph.startDestination = R.id.mainFragment
                }
                navHostFragment.navController.graph = graph
            }
        })

    }

    // Touch outside dismiss keyboard
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            if (v is EditText) {
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(x, y)) {
                    v.clearFocus()
                    var touchTargetIsEditText = false
                    for (vi in v.getRootView().touchables) {
                        if (vi is EditText) {
                            val clickedViewRect = Rect()
                            vi.getGlobalVisibleRect(clickedViewRect)
                            clickedViewRect.inset(25, 25)
                            if (clickedViewRect.contains(x, y)) {
                                touchTargetIsEditText = true
                                break
                            }
                        }
                    }
                    if (!touchTargetIsEditText) {
                        val imm: InputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}