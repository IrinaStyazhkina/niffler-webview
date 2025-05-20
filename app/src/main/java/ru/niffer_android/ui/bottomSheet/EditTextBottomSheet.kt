package ru.niffer_android.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.niffer_android.R
import ru.niffer_android.databinding.BottomsheetEditTextBinding
import ru.niffer_android.utils.AndroidUtils.hideKeyboard
import ru.niffer_android.utils.AndroidUtils.showKeyboard

class EditTextBottomSheet(
    private val labelText: String,
    private val inputText: String,
    private val validateNotBlank: Boolean = false
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetEditTextBinding

    private var inputHint: String? = null

    private var onSaveClickListener: ((inputValue: String) -> Unit)? = null
    private var onCancelClickListener: (() -> Unit)? = null

    fun setHint(hint: String): EditTextBottomSheet {
        inputHint = hint
        return this@EditTextBottomSheet
    }

    fun setOnSaveClickListener(listener: (inputValue: String) -> Unit): EditTextBottomSheet {
        onSaveClickListener = listener
        return this@EditTextBottomSheet
    }

    fun setOnCancelClickListener(listener: () -> Unit): EditTextBottomSheet {
        onCancelClickListener = listener
        return this@EditTextBottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetEditTextBinding.inflate(inflater, container, false)

        binding.tvLabelText.text = labelText
        binding.inputText.hint = inputHint
        binding.inputText.setText(inputText)

        binding.inputText.requestFocus()
        binding.inputText.post {
            showKeyboard(binding.inputText)
        }

        binding.buttonSaveEditText.setOnClickListener {
            val text = binding.inputText.text.toString()
            if (validateNotBlank && text.isEmpty()) {
                binding.inputTextLayout.error = getString(R.string.should_not_be_blank)
            } else {
                binding.inputTextLayout.error = null
                onSaveClickListener?.invoke(text)
                binding.inputText.post {
                    hideKeyboard(binding.inputText)
                }
                dismiss()
            }
        }

        binding.buttonCancelEditText.setOnClickListener {
            onCancelClickListener?.invoke()
            binding.inputText.post {
                hideKeyboard(binding.inputText)
            }
            dismiss()
        }

        return binding.root
    }

    companion object {
        const val TAG = "EditTextBottomSheet"
    }
}