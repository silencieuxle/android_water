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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.gndev.water_battle.core.constant.SharedPreferencesKey
import me.gndev.water_battle.core.constant.Weapon
import me.gndev.water_battle.data.share_preferences.PrefManager
import me.gndev.water_battle.util.CommonExtensions.applySystemBarsInset
import javax.inject.Inject
import android.view.Gravity
import me.gndev.water_battle.util.DimensionUtils


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var prefManager: PrefManager

    private lateinit var mainActivityCoordinatorView: CoordinatorLayout

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Water)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        mainActivityCoordinatorView = findViewById(R.id.mainActivityCoordinatorView)

        // Init data before navigate to main fragment or onboarding fragment
        viewModel.initGame()
        viewModel.currentGame.observe(this, {
            // This will cause the whole app lays out in fullscreen
            if (it != null) {
                val isFirstStartUp =
                    prefManager.getBooleanVal(SharedPreferencesKey.IS_FIRST_STARTUP, true)
                findViewById<View>(R.id.nav_host_fragment).applySystemBarsInset()
                val navHostFragment =
                    (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                val inflater = navHostFragment.navController.navInflater
                val graph = inflater.inflate(R.navigation.navigation)
                if (isFirstStartUp) {
                    initSharedPreferences()
                    graph.startDestination = R.id.onboardingFragment
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

    private fun initSharedPreferences() {
        prefManager.setVal(SharedPreferencesKey.DEFAULT_WEAPON, Weapon.CUP)
        prefManager.setVal(SharedPreferencesKey.DEFAULT_VOLUME, 100)
        prefManager.setVal(SharedPreferencesKey.IS_FIRST_STARTUP, true)
        prefManager.setVal(SharedPreferencesKey.LAST_HIT_VOLUME, 100)
    }

    fun showSnackBar(message: String, duration: Int) {
        val snackBar = Snackbar.make(mainActivityCoordinatorView, message, duration).apply {
            setBackgroundTint(ContextCompat.getColor(context, R.color.purpleDarker))
        }
        val view: View = snackBar.view
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.layoutAnimationParameters
        params.gravity = Gravity.TOP
        params.topMargin = getStatusBarHeight()+ DimensionUtils.pxFromDp(applicationContext, 16)
        view.layoutParams = params
        snackBar.show()
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}