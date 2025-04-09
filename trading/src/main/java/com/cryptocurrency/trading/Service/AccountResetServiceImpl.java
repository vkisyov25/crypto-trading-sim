package com.cryptocurrency.trading.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountResetServiceImpl implements AccountResetService {
    private final UserService userServiceImpl;
    private final UserAssetService userAssetService;
    private final TransactionService transactionService;

    @Override
    @Transactional
    public void resetUserAccount(int userId) throws Exception {
        userServiceImpl.resetBalance(userId);
        userAssetService.removeUserAssetsByUserId(userId);
        transactionService.removeTransactionsByUserId(userId);
    }
}
