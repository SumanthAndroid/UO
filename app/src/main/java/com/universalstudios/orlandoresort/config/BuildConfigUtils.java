package com.universalstudios.orlandoresort.config;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.model.security.SecurityUtils;

/**
 * Util class to work with {@link com.universalstudios.orlandoresort.BuildConfig}
 *
 * @author Steven Byle
 */
public class BuildConfigUtils {
    public static final String FLAVOR_LOCATION_ORLANDO = "orlando";
    public static final String FLAVOR_LOCATION_HOLLYWOOD = "hollywood";

    public static final String FLAVOR_SERVICE_ENV_DEV = "devEnv";
    public static final String FLAVOR_SERVICE_ENV_QA = "qaEnv";
    public static final String FLAVOR_SERVICE_ENV_STG = "stgEnv";
    public static final String FLAVOR_SERVICE_ENV_UAT = "uatEnv";
    public static final String FLAVOR_SERVICE_ENV_PROD = "prodEnv";

    public static final String BUILD_TYPE_DEBUG = "debug";
    public static final String BUILD_TYPE_ALPHA = "alpha";
    public static final String BUILD_TYPE_RELEASE = "release";

    /**
     * Flavor Dimension: Location
     */
    private static boolean isLocationFlavor(String locationFlavor) {
        return BuildConfig.FLAVOR_DIMENSION_LOCATION.equals(locationFlavor);
    }

    public static boolean isLocationFlavorHollywood() {
        return isLocationFlavor(FLAVOR_LOCATION_HOLLYWOOD);
    }

    public static boolean isLocationFlavorOrlando() {
        return isLocationFlavor(FLAVOR_LOCATION_ORLANDO);
    }

    /**
     * Flavor Dimension: Service Environment
     */
    private static boolean isServiceEnvFlavor(String serviceEnvFlavor) {
        return BuildConfig.FLAVOR_DIMENSION_SERVICE_ENVIRONMENT.equals(serviceEnvFlavor);
    }

    public static boolean isServiceEnvFlavorDev() {
        return isServiceEnvFlavor(FLAVOR_SERVICE_ENV_DEV);
    }

    public static boolean isServiceEnvFlavorQa() {
        return isServiceEnvFlavor(FLAVOR_SERVICE_ENV_QA);
    }

    public static boolean isServiceEnvFlavorStg() {
        return isServiceEnvFlavor(FLAVOR_SERVICE_ENV_STG);
    }

    public static boolean isServiceEnvFlavorUat() {
        return isServiceEnvFlavor(FLAVOR_SERVICE_ENV_UAT);
    }

    public static boolean isServiceEnvFlavorProd() {
        return isServiceEnvFlavor(FLAVOR_SERVICE_ENV_PROD);
    }

    /**
     * Build Type
     */
    private static boolean isBuildType(String buildType) {
        return BuildConfig.BUILD_TYPE.equals(buildType);
    }

    public static boolean isBuildTypeDebug() {
        return isBuildType(BUILD_TYPE_DEBUG);
    }

    public static boolean getBuildTypeAlpha() {
        return isBuildType(BUILD_TYPE_ALPHA);
    }

    public static boolean isBuildTypeRelease() {
        return isBuildType(BUILD_TYPE_RELEASE);
    }

    /**
     * Obfuscated config values
     */
    public static String deobfuscateString(String stringToDeobfuscate) {
        return SecurityUtils.deobfuscateString(stringToDeobfuscate);
    }

    public static String getRawVersionName() {
        String[] versionParts = BuildConfig.VERSION_NAME.split("_");
        if (versionParts.length > 0) {
            return versionParts[0];
        } else {
            return BuildConfig.VERSION_NAME;
        }
    }
}
