package com.ezhilan.cine.domain.useCases.home

import android.content.Context
import android.net.Uri
import com.ezhilan.cine.R
import com.ezhilan.cine.core.Constants
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.presentation.util.UiText
import com.ezhilan.cine.presentation.util.cacheExternalUri
import com.ezhilan.cine.presentation.util.convertToMultipartData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val repo: HomeRepository,
    @ApplicationContext private val context: Context,
) {

    operator fun invoke(uri: Uri): Flow<DataState<String>> {
        val fileUri =
            if (uri.scheme == Constants.KEY_CONTENT) uri.cacheExternalUri(context) else uri
        return fileUri?.convertToMultipartData()?.let { multipartBody ->
            repo.uploadFile(multipartBody).map { dataState ->
                when (dataState) {
                    is DataState.InProgress -> dataState

                    is DataState.Success -> {
                        context.cacheDir.deleteRecursively()
                        context.cacheDir.mkdir()
                        val imageUrl = dataState.data.data?.image
                        if (!imageUrl.isNullOrEmpty()) {
                            DataState.Success(imageUrl)
                        } else {
                            DataState.Error.Local(UiText.Resource(R.string.error_invalid_image_url))
                        }
                    }

                    is DataState.Error -> dataState
                }
            }
        } ?: flowOf(DataState.Error.Local(UiText.Resource(R.string.error_file_not_found)))
    }
}