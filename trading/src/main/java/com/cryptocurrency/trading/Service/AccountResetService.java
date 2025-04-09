package com.cryptocurrency.trading.Service;

import jakarta.transaction.Transactional;

public interface AccountResetService {
    @Transactional
    void resetUserAccount(int userId) throws Exception;
}
