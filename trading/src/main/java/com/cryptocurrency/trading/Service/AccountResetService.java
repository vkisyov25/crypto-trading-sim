package com.cryptocurrency.trading.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountResetService {
    private final UserService userService;
    private final UserAssetService userAssetService;
    private final TransactionService transactionService;

    @Transactional
    public void resetUserAccount(int userId) throws Exception {
        userService.resetBalance(userId);
        userAssetService.removeUserAssetsByUserId(userId);
        transactionService.removeTransactionsByUserId(userId);
    }
}
