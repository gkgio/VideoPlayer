package com.gkgio.videoplayer.presentation

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.WindowManager.LayoutParams.*
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
import java.lang.reflect.Method
import javax.inject.Inject
import kotlin.math.roundToInt


class LaunchActivity : AppCompatActivity(), ClickDialogCallBack {

    companion object {
        private const val CRITICAL_BATTERY_LEVEL = 20
        private const val OVERLAY_PERMISSION_REQ_CODE = 3233
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

      //  requestOverlayPermission()

        val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // This work only for android 4.4+

        // This work only for android 4.4+
        window.decorView.systemUiVisibility = flags

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        val decorView: View = window.decorView
        decorView
            .setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN === 0) {
                    decorView.systemUiVisibility = flags
                }
            }

        lockStatusBar(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       /* if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                DialogUtils.showDialog(
                    "dfer",
                    supportFragmentManager,
                    "Для работы приложения требуется предоставить разрешение",
                    "Хорошо"
                )
            }
        }*/
    }

    override fun onRightButtonClick(fragmentTag: String) {
        super.onRightButtonClick(fragmentTag)
        requestOverlayPermission()
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + this.packageName)
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onUserLeaveHint() {
        (applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME)
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        disableStatusBar()
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun disableStatusBar() {
        try {
            val service = getSystemService("statusbar")
            val claz = Class.forName("android.app.StatusBarManager")
            val expand: Method = claz.getMethod("collapse")
            expand.invoke(service)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {

    }

    private fun lockStatusBar(context: Context) {
       val windowManager =
            applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams()
        params.apply {
            type = TYPE_SYSTEM_ERROR
            gravity = Gravity.TOP
            flags = FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCH_MODAL or FLAG_LAYOUT_IN_SCREEN
            width = MATCH_PARENT
            val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            var result = 0
            if (resId > 0) result = context.resources.getDimensionPixelSize(resId)
            height = result
            format = PixelFormat.TRANSPARENT
        }
        val interceptView = InterceptViewGroup(context)

        try {
            windowManager.addView(interceptView, params)
        } catch (e: RuntimeException) {


        }
    }
}