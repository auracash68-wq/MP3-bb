package com.example

import com.example.model.AppScreen
import com.example.model.AppUiState
import com.example.model.ClockStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun defaultUiState_matchesReferenceDesign() {
    val defaultState = AppUiState()
    assertEquals(AppScreen.Splash, defaultState.currentScreen)
    assertTrue(defaultState.floatingButtonEnabled)
    assertEquals(100, defaultState.overlayOpacityPercent)
    assertTrue(defaultState.alwaysOnDisplayEnabled)
    assertTrue(defaultState.volumeGestureEnabled)
    assertTrue(defaultState.peepProtectionEnabled)
    assertTrue(defaultState.biometricLockEnabled)
    assertEquals(ClockStyle.DIGITAL, defaultState.clockStyle)
    assertEquals(0, defaultState.selectedClockPreviewIndex)
    assertTrue(defaultState.showDate)
    assertTrue(defaultState.showBattery)
    assertEquals(12, defaultState.batterySavedPercent)
  }
}
