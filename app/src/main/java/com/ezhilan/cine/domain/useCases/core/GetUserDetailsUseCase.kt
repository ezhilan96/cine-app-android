package com.ezhilan.cine.domain.useCases.core

import com.ezhilan.cine.domain.entity.UserData
import com.ezhilan.cine.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    operator fun invoke(): Flow<UserData?> = authRepo.userDetails
}