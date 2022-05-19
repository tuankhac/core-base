package com.vmo.core.modules.models.integration.auth.responses.account;

import com.vmo.core.modules.models.integration.auth.responses.account.details.AccountUserResponse;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class AccountDetailResponse extends CustomerDetailResponse {
    @Nullable
    private AccountUserResponse user;
}
