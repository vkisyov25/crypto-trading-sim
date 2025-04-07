package com.cryptocurrency.trading.Controllers;

import com.cryptocurrency.trading.Models.UserAsset;
import com.cryptocurrency.trading.Service.UserAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/user-assets")
@RequiredArgsConstructor
public class UserAssetController {

    private final UserAssetService userAssetService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserAsset>> getUserAssets(@PathVariable int userId) throws SQLException {
        List<UserAsset> assets = userAssetService.getAssetsByUserId(userId);
        return ResponseEntity.ok(assets);
    }
}
