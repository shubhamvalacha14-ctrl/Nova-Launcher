package com.nova.launch.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FillMaxWidth
import androidx.compose.foundation.foundation.rememberScrollState
import androidx.compose.foundation.layout.VerticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.compose.ui.Modifier
import androidx.compose.compose.ui.res.stringResource
import androidx.compose.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.nova.launch.R
import com.nova.launch.game.download.jvm_server.JvmCrashException
import com.nova.launch.game.download.modpack.install.ModpackImporter
import com.nova.launch.game.download.modpack.install.PackNotSupportedException
import com.nova.launch.game.download.modpack.install.UnsupportedPackReason
import com.nova.launch.game.download.modpack.platform.PackPlatform
import com.nova.launch.game.version.download.DownloadFailedException
import com.nova.launch.game.version.installed.VersionsManager
import com.nova.launch.ui.components.MarqueeText
import com.nova.launch.ui.components.SimpleAlertDialog
import com.nova.launch.ui.components.FadeEdge
import com.nova.launch.ui.screens.content.download.ModpackVersionNameDialog
import com.nova.launch.ui.screens.content.download.assets.elements.PackIdentifier
import com.nova.launch.ui.screens.content.elements.TitleTaskFlowDialog
import com.nova.launch.utils.logging.Logger.IError
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.Continuation

/** 导入整合包相关操作 */
sealed interface ModpackImportOperation {
    data object None : ModpackImportOperation
    /** 开始导入整合包 */
    data object Import : ModpackImportOperation
    /** 不支持的整合包或格式无效 */
    data class NotSupport(val reason: UnsupportedPackReason) : ModpackImportOperation
    /** 整合包导入完成 */
    data object Finished : ModpackImportOperation
    /** 导入整合包出现异常 */
    data class Error(val th: Throwable) : ModpackImportOperation
}

/** 整合包版本命名自定义状态操作 */
sealed interface VersionNameOperation {
    data object None : VersionNameOperation
    /** 等待用户输入版本名称 */
    data class Waiting(val name: String) : VersionNameOperation
}

/** 整合包安装确认使用移动网络状态操作 */
sealed interface ConfirmMobileDataOperation {
    data object None : ConfirmMobileDataOperation
    /** 等待用户确认使用移动网络 */
    data object Waiting : ConfirmMobileDataOperation
}

/**
 * 导入整合包ViewModel
 */
class ModpackImportViewModel : ViewModel() {
    var importOperation by mutableStateOf<ModpackImportOperation>(ModpackImportOperation.None)
    var versionNameOperation by mutableStateOf<VersionNameOperation>(VersionNameOperation.None)
}
