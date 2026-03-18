package ru.sergeyabadzhev.weatherappkmp.core.network

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebugBuild: Boolean = Platform.isDebugBinary
