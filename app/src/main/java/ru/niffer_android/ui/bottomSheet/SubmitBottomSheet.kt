package ru.niffer_android.ui.bottomSheet

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.niffer_android.R
import ru.niffer_android.databinding.BottomsheetSubmitBinding

enum class SubmitButtonStyle () {
    SUBMIT,
    WARNING
}

class SubmitBottomSheet(
    private val titleText: String,
    private val subtitleText: String? = null,
    private val helperText: String? = null,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetSubmitBinding

    private var submitButtonText: String? = null

    private var submitButtonStyle = SubmitButtonStyle.SUBMIT

    private var onSaveClickListener: (() -> Unit)? = null
    private var onCancelClickListener: (() -> Unit)? = null

    fun setSubmitButtonText(submitText: String): SubmitBottomSheet {
        submitButtonText = submitText
        return this@SubmitBottomSheet
    }

    fun setSubmitButtonStyle(submitStyle: SubmitButtonStyle): SubmitBottomSheet {
        submitButtonStyle = submitStyle
        return this@SubmitBottomSheet
    }

    fun setOnSaveClickListener(listener: () -> Unit): SubmitBottomSheet {
        onSaveClickListener = listener
        return this@SubmitBottomSheet
    }

    fun setOnCancelClickListener(listener: () -> Unit): SubmitBottomSheet {
        onCancelClickListener = listener
        return this@SubmitBottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetSubmitBinding.inflate(inflater, container, false)

        binding.tvTitle.text = titleText
        binding.tvSubtitle.text = subtitleText
        binding.tvHelperText.text = helperText
        if (submitButtonText != null) {
            binding.buttonSubmit.text = submitButtonText
        }
        binding.tvHelperText.isVisible = helperText == null

        if (submitButtonStyle == SubmitButtonStyle.WARNING) {
            val warnColor = ContextCompat.getColor(requireContext(), R.color.red)
            binding.buttonSubmit.strokeColor = ColorStateList.valueOf(warnColor)
            binding.buttonSubmit.backgroundTintList = ColorStateList.valueOf(warnColor)
        }

        binding.buttonSubmit.setOnClickListener {
            onSaveClickListener?.invoke()
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            onCancelClickListener?.invoke()
            dismiss()
        }

        return binding.root
    }

    companion object {
        const val TAG = "SubmitBottomSheet"
    }
}