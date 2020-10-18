package com.gkgio.videoplayer.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.BatteryManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.forEach
import com.gkgio.videoplayer.R
import com.gkgio.videoplayer.di.AppInjector
import com.gkgio.videoplayer.presentation.ext.closeKeyboard
import com.gkgio.videoplayer.presentation.ext.createViewModel
import com.gkgio.videoplayer.presentation.navigation.Navigator
import kotlinx.android.synthetic.main.activity_launch.*
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject
import kotlin.math.roundToInt


class LaunchActivity : AppCompatActivity() {

    companion object {
        private const val CRITICAL_BATTERY_LEVEL = 20
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val viewModel by lazy { createViewModel { AppInjector.appComponent.launchViewModel } }

    private val navigator = Navigator(this, R.id.containerRoot)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppInjector.appComponent.inject(this)
        setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_launch)

        ViewCompat.setOnApplyWindowInsetsListener(containerRoot) { view, insets ->
            var consumed = false
            (view as ViewGroup).forEach { child ->
                val childResult = ViewCompat.dispatchApplyWindowInsets(child, insets)
                if (childResult.isConsumed) {
                    consumed = true
                }
            }
            if (consumed) insets.consumeSystemWindowInsets() else insets
        }

        if (savedInstanceState == null) {
            viewModel.onNewStart()
        }

        val phoneStateFilter = IntentFilter()
        phoneStateFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        phoneStateFilter.addAction(Intent.ACTION_SHUTDOWN)
        registerReceiver(batteryStateReceiver, phoneStateFilter)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            (currentFocus as? EditText)?.let { editText ->
                if (editText.tag == null || editText.tag !is String) {
                    currentFocus?.let { focus ->
                        val outR = Rect()
                        editText.getGlobalVisibleRect(outR)
                        val isKeyboardOpen = !outR.contains(event.rawX.toInt(), event.rawY.toInt())
                        if (isKeyboardOpen) {
                            closeKeyboard()
                            focus.clearFocus()
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private val batteryStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SHUTDOWN -> {
                    viewModel.shutDown()
                }

                Intent.ACTION_BATTERY_CHANGED -> {
                    val maximumBattery = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
                    val currentBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    val fPercent = currentBattery.toFloat() / maximumBattery.toFloat() * 100f
                    val percent = fPercent.roundToInt()

                    if (percent < CRITICAL_BATTERY_LEVEL) {
                        viewModel.criticalBatteryLevel()
                    }
                }
            }
        }
    }
}