package com.mobile.reconnect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mobile.reconnect.databinding.ActivityMainBinding
import com.mobile.reconnect.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

	private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
	private val channelId = "alarm_channel"
	private val channelName = "알림 채널"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestNotificationPermission()
		showCustomNotification("실종자 알림", "실종자 한수현씨(남, 17세)가 근처에 있을 확률이 높습니다.", "곱슬머리, 파란색 긴팔티, 흰색 가방")

		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
		val navController = navHostFragment.navController

		binding.navView.setupWithNavController(navController)
	}

	private fun requestNotificationPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
				!= PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(
					this,
					arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
					NOTIFICATION_PERMISSION_REQUEST_CODE
				)
			}
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int, permissions: Array<String>, grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)

		if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "알림 권한이 부여되었습니다.", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(this, "알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun showCustomNotification(title: String, content: String, additionalInfo: String?) {
		val notificationManager =
			getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val importance = NotificationManager.IMPORTANCE_HIGH
			val channel = NotificationChannel(channelId, channelName, importance)
			notificationManager.createNotificationChannel(channel)
		}

		val customView = RemoteViews(packageName, R.layout.item_noti)
		customView.setTextViewText(R.id.tv_main_message, title)
		customView.setTextViewText(R.id.tv_additional_info, additionalInfo ?: "")
		customView.setViewVisibility(R.id.tv_additional_info, if (additionalInfo.isNullOrEmpty()) View.GONE else View.VISIBLE)

		val notification = NotificationCompat.Builder(this, channelId)
			.setSmallIcon(R.drawable.icon_note)
			.setStyle(NotificationCompat.DecoratedCustomViewStyle())
			.setCustomContentView(customView)
			.build()

		notificationManager.notify(System.currentTimeMillis().toInt(), notification)
	}
}