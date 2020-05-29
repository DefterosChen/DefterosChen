package com.horizon.collector.setting.model

import com.horizon.lightkv.KVData
import com.horizon.lightkv.LightKV
import com.kky.healthcaregardens.common.widget.waterfall.base.config.GlobalConfig
import com.kky.healthcaregardens.common.widget.waterfall.collector.config.GlobalLogger

object UserSetting : KVData() {
    override val data: LightKV
        get() = LightKV.Builder(GlobalConfig.getAppContext(), "user_setting")
                .logger(GlobalLogger.getInstance())
                .async()

    var showHidden by boolean(1)
    var huabanChannels by string(2)
    var unsplashChannels by string(3)
    var collectPath by string(4)
    var lastShowingFragment by string(5)
}

