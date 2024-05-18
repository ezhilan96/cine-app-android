package com.ezhilan.cine.data.model.remote.response

data class ImageUploadResponse(
    var data: DataResponse?,
)

data class DataResponse(
    var image: String?,
)