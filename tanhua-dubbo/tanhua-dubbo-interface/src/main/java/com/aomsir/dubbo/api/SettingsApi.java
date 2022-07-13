package com.aomsir.dubbo.api;

import com.aomsir.model.domain.Settings;

public interface SettingsApi {
    Settings findByUserId(Long id);

    void save(Settings settings);

    void update(Settings settings);

    void delete(Long userId, Long blackUserId);
}
