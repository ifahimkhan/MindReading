package ml.fahimkhan.myapplication

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class UpdateHelper(private val activity: ComponentActivity) {

    private val TAG = "UpdateHelper"
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)

    private val updateLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            ComponentActivity.RESULT_OK -> {
                Toast.makeText(activity, "App updated successfully", Toast.LENGTH_SHORT).show()
            }

            ComponentActivity.RESULT_CANCELED -> {
                Toast.makeText(activity, "Update canceled", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(activity, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkForUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
            Log.d(TAG, "updateAvailability: ${updateInfo.updateAvailability()}")
            Log.d(
                TAG,
                "isImmediateAllowed: ${updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)}"
            )
            Log.d(TAG, "availableVersionCode: ${updateInfo.availableVersionCode()}")

            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdate(updateInfo)
            }
        }.addOnFailureListener {
            Log.e(TAG, "Update check failed: ${it.localizedMessage}")
        }
    }

    fun resumeUpdateIfInterrupted() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdate(updateInfo)
            }
        }
    }

    private fun startUpdate(updateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                updateInfo,
                updateLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )


        } catch (e: Exception) {
            Log.e(TAG, "startUpdate failed", e)
        }
    }
}
