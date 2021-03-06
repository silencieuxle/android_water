package me.gndev.water

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.gndev.water.core.base.ViewModelBase
import me.gndev.water.core.constant.Container
import me.gndev.water.core.constant.SharedPreferencesKey
import me.gndev.water.core.model.DataResult
import me.gndev.water.data.entity.Game
import me.gndev.water.data.repository.GameRepository
import me.gndev.water.data.share_preferences.PrefManager
import me.gndev.water.service.NotificationWorker
import me.gndev.water.util.CommonExtensions.applySystemBarsInset
import me.gndev.water.util.DateTimeUtils
import me.gndev.water.util.DimensionUtils
import java.util.concurrent.TimeUnit
import javax.inject.Inject


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
                createNotificationChannel()
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

        val workRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                getString(R.string.app_name),
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
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

    @SuppressLint("InflateParams")
    fun showSnackBar(
        tittle: String?,
        message: String,
        @DrawableRes icon: Int?,
        actionText: String?,
        callBack: (() -> Unit)? = null,
        duration: Int
    ) {
        val snackBar = Snackbar.make(mainActivityCoordinatorView, "", duration)
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)

        val customSnackView: View = layoutInflater.inflate(R.layout.snackbar, null)
        val tvTitle = customSnackView.findViewById<TextView>(R.id.tv_title)
        if (title.isNullOrEmpty()) tvTitle.visibility = View.GONE else tvTitle.text = tittle

        customSnackView.findViewById<TextView>(R.id.tv_message).text = message

        val btnAction = customSnackView.findViewById<TextView>(R.id.btn_action)
        if (actionText.isNullOrEmpty()) {
            btnAction.visibility = View.GONE
        } else {
            btnAction.text = actionText
            btnAction.setOnClickListener {
                callBack?.invoke()
            }
        }

        if (icon != null) {
            customSnackView.findViewById<ImageView>(R.id.iv_icon).setBackgroundResource(icon)
        }

        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        val params = snackBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        params.apply {
            gravity = Gravity.TOP
            topMargin = getStatusBarHeight() + DimensionUtils.pxFromDp(applicationContext, 16)
        }
        snackBarLayout.apply {
            setPadding(0, 0, 0, 0)
            layoutParams = params
            addView(customSnackView, 0)
        }
        snackBar.show()
    }

    private fun initSharedPreferences() {
        prefManager.setVal(SharedPreferencesKey.DEFAULT_CONTAINER, Container.CUP)
        prefManager.setVal(SharedPreferencesKey.DEFAULT_VOLUME, 100)
        prefManager.setVal(SharedPreferencesKey.IS_FIRST_STARTUP, true)
        prefManager.setVal(SharedPreferencesKey.LAST_HIT_VOLUME, 100)
        prefManager.setVal(SharedPreferencesKey.MIN_AGE, 9)
        prefManager.setVal(SharedPreferencesKey.MIN_HEIGHT, 50)
        prefManager.setVal(SharedPreferencesKey.MIN_WEIGHT, 20)
        prefManager.setVal(SharedPreferencesKey.MAX_AGE, 130)
        prefManager.setVal(SharedPreferencesKey.MAX_HEIGHT, 250)
        prefManager.setVal(SharedPreferencesKey.MAX_WEIGHT, 600)
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.app_name)
        val descriptionText = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(getString(R.string.app_name), name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModelBase() {
    private val _currentGame: MutableLiveData<Game> = MutableLiveData()
    val currentGame: LiveData<Game> get() = _currentGame

    fun initGame() {
        viewModelScope.launch {
            when (val gameExists = gameRepository.exists(DateTimeUtils.getTodayAsNumber())) {
                is DataResult.Success -> {
                    val result = if (gameExists.data) {
                        gameRepository.getToday()
                    } else {
                        gameRepository.insert(Game())
                        gameRepository.getToday()
                    }
                    when (result) {
                        is DataResult.Success -> _currentGame.value = result.data
                        else -> errorLiveEvent.value = result as DataResult.Error
                    }
                }
            }
        }
    }

    fun updateCurrentGame(totalVolume: Int) {
        viewModelScope.launch {
            _currentGame.value?.score = totalVolume
            gameRepository.update(_currentGame.value!!)
        }
    }
}