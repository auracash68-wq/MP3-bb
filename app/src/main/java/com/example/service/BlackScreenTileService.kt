package com.example.service

import android.app.PendingIntent
import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.example.MainActivity
import com.example.R
import java.util.concurrent.Executors

class BlackScreenTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        qsTile?.apply {
            state = Tile.STATE_INACTIVE
            label = getString(R.string.qs_tile_label)
            updateTile()
        }
    }

    override fun onClick() {
        super.onClick()
        val intent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_ACTIVATE_BLACK_SCREEN
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }

    companion object {
        const val ACTION_ACTIVATE_BLACK_SCREEN = "com.example.ACTION_ACTIVATE_BLACK_SCREEN"

        fun requestAddQuickTile(context: Context, onResult: (String) -> Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val statusBarManager = context.getSystemService(StatusBarManager::class.java)
                if (statusBarManager != null) {
                    statusBarManager.requestAddTileService(
                        ComponentName(context, BlackScreenTileService::class.java),
                        context.getString(R.string.qs_tile_label),
                        Icon.createWithResource(context, R.drawable.ic_qs_tile),
                        Executors.newSingleThreadExecutor()
                    ) { resultCode ->
                        val msg = when (resultCode) {
                            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED ->
                                "Quick Tile added to Quick Settings"
                            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED ->
                                "Quick Tile is already active in Quick Settings"
                            else ->
                                "Quick Tile ready in Quick Settings panel"
                        }
                        onResult(msg)
                    }
                    return
                }
            }
            onResult("Quick Tile registered. Swipe down Quick Settings to add 'Black Screen'.")
        }
    }
}
