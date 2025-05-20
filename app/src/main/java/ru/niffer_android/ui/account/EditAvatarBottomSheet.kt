package ru.niffer_android.ui.account

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.niffer_android.R
import ru.niffer_android.utils.uriToBase64

class EditAvatarBottomSheet(private val viewModel: AccountViewModel) : BottomSheetDialogFragment() {

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                dismiss()
                return@registerForActivityResult
            }

            val uri = requireNotNull(it.data?.data)
            val base64Image = uriToBase64(requireContext(), uri)
            if (base64Image != null) {
                viewModel.updateAvatar(base64Image)
            }
            dismiss()
        }


    override fun onCreateDialog(
        savedInstanceState: Bundle?,
    ): Dialog {
        val bottomSheetDialog =
            BottomSheetDialog(
                requireContext()
            )
        bottomSheetDialog.setContentView(R.layout.bottomsheet_edit_avatar)

        val uploadPhotoOption = bottomSheetDialog.findViewById<LinearLayout>(R.id.uploadPhotoContainer)
        val takePhotoOption = bottomSheetDialog.findViewById<LinearLayout>(R.id.takePhotoContainer)

        uploadPhotoOption?.setOnClickListener {
            ImagePicker.Builder(this)
                .galleryOnly()
                .crop()
                .maxResultSize(2048, 2048)
                .createIntent(photoLauncher::launch)
        }

        takePhotoOption?.setOnClickListener {
            ImagePicker.Builder(this)
                .cameraOnly()
                .crop()
                .maxResultSize(2048, 2048)
                .createIntent(photoLauncher::launch)
        }

        return bottomSheetDialog
    }


    companion object {
        const val TAG = "EditAvatarBottomSheet"
    }
}