package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.util;

import com.threatmetrix.TrustDefenderMobile.Config;
import com.threatmetrix.TrustDefenderMobile.ProfilingOptions;
import com.threatmetrix.TrustDefenderMobile.TrustDefenderMobile;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesblack on 7/13/16.
 */
public class SecurityUtils {
    public static TrustDefenderMobile trustDefenderMobile;

    private static final String DEFENDER_CONFIG_ID = "yyie64fy";

    public static void initDefender() {
        trustDefenderMobile = new TrustDefenderMobile(DEFENDER_CONFIG_ID);
        Config trustDefenderConfig = new Config().setContext(UniversalOrlandoApplication.getAppContext());
        trustDefenderConfig.setDisableOkHttp(true);
        trustDefenderConfig.setRegisterForLocationServices(true);
        trustDefenderMobile.init(trustDefenderConfig);
    }

    public static void startProfiling(String orderId) {
        ProfilingOptions options = new ProfilingOptions();
        List<String> customAttributeList = new ArrayList<>();
        customAttributeList.add(orderId);
        options.setCustomAttributes(customAttributeList);
        options.setSessionID(orderId);
        trustDefenderMobile.doProfileRequest(options);
    }
}
