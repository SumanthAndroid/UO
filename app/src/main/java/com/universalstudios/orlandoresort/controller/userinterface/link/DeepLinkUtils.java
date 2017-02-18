/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.link;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.alerts.AlertsListActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DetailUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.DiningPlanDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.detail.ExpressPassDetailActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreActivity;
import com.universalstudios.orlandoresort.controller.userinterface.explore.ExploreType;
import com.universalstudios.orlandoresort.controller.userinterface.faq.VacationPlannerFaqActivity;
import com.universalstudios.orlandoresort.controller.userinterface.help.AboutWifiActivity;
import com.universalstudios.orlandoresort.controller.userinterface.help.GuestServicesActivity;
import com.universalstudios.orlandoresort.controller.userinterface.hours.HoursAndDirectionsActivity;
import com.universalstudios.orlandoresort.controller.userinterface.parking.ParkingReminderActivity;
import com.universalstudios.orlandoresort.controller.userinterface.search.SearchActivity;
import com.universalstudios.orlandoresort.controller.userinterface.tutorial.TutorialActivity;
import com.universalstudios.orlandoresort.controller.userinterface.web.WebViewActivity;
import com.universalstudios.orlandoresort.model.network.domain.events.EventSeries;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author acampbell
 *
 */
public class DeepLinkUtils {

    private static final String TAG = DeepLinkUtils.class.getSimpleName();

    private static final String RIDE_DETAIL = "RideDetail";
    private static final String SHOW_DETAIL = "ShowDetail";
    private static final String SHOP_DETAIL = "ShopDetail";
    private static final String DINING_DETAIL = "DiningDetail";
    private static final String ENTERTAINMENT_DETAIL = "EntertainmentDetail";
    private static final String EVENT_DETAIL = "EventDetail";
    private static final String EVENT_SERIES_DETAIL = "EventSeriesDetail";
    private static final String MY_FAVORITES = "MyFavorites";
    private static final String PARKING_REMINDER = "ParkingReminder";
    private static final String EXPLORE_IOA = "ExploreIOA";
    private static final String EXPLORE_USF = "ExploreUSF";
    private static final String EXPLORE_CW = "ExploreCW";
    private static final String EXPLORE_VOLCANO_BAY = "ExploreVB";
    private static final String EXPLORE_RIDES = "ExploreRides";
    private static final String EXPLORE_SHOWS = "ExploreShows";
    private static final String EXPLORE_DINING = "ExploreDining";
    private static final String EXPLORE_SHOPPING = "ExploreShopping";
    private static final String EXPLORE_WNW = "ExploreWNW";
    private static final String LOCKERS = "Lockers";
    private static final String GUEST_SERVICES_LOCATIONS = "GuestServicesLocations";
    private static final String HOURS = "Hours";
    private static final String CONTACT = "Contact";
    private static final String WIFI_DETAIL = "WiFiDetail";
    private static final String VACATION_PACKAGE_FAQ = "VacationPackageFAQ";
    private static final String WALKTHROUGH = "Walkthrough";
    private static final String PRIVACY_POLICY = "PrivacyPolicy";
    private static final String PRIVACY_FAQ = "PrivacyFAQ";
    private static final String TERMS_OF_SERVICE = "TermsOfService";
    private static final String TRADEMARKS = "Trademarks";
    private static final String EXPRESS_PASS_DETAIL = "ExpressPassDetail";
    private static final String DINING_PLAN_DETAIL = "DiningPlanDetail";
    private static final String MY_ALERTS = "MyAlerts";
    private static final String SEARCH = "Search";
    private static final String APP_LINK = "AppLink";
    private static final String EXPLORE_USH = "ExploreUSH";
    private static final String EXPLORE_CWH = "ExploreCWH";
    private static final String PRIVACY_INFORMATION_CENTER = "PrivacyInformationCenter";

    public enum DeepLinkType {
    	EVENT_SERIES_DETAIL,
        POI_DETAIL,
        MY_FAVORITES,
        PARKING_REMINDER,
        EXPLORE_IOA,
        EXPLORE_USF,
        EXPLORE_CW,
        EXPLORE_VB,
        EXPLORE_RIDES,
        EXPLORE_SHOWS,
        EXPLORE_DINING,
        EXPLORE_SHOPPING,
        EXPLORE_WNW,
        LOCKERS,
        GUEST_SERVICES_LOCATIONS,
        HOURS,
        CONTACT,
        WIFI_DETAIL,
        VACATION_PACKAGE_FAQ,
        WALKTHROUGH,
        PRIVACY_POLICY,
        PRIVACY_FAQ,
        TERMS_OF_SERVICE,
        TRADEMARKS,
        EXPRESS_PASS_DETAIL,
        DINING_PLAN_DETAIL,
        MY_ALERTS,
        SEARCH,
        APP_LINK,
        EXPLORE_USH,
        EXPLORE_CWH,
        PRIVACY_INFORMATION_CENTER
    }

    private static Map<String, DeepLinkType> LINK_DESTINATIONS;

    private static Map<String, DeepLinkType> getLinkDestinations() {
        if (LINK_DESTINATIONS == null) {
            LINK_DESTINATIONS = new HashMap<String, DeepLinkType>();
            LINK_DESTINATIONS.put(RIDE_DETAIL, DeepLinkType.POI_DETAIL);
            LINK_DESTINATIONS.put(SHOW_DETAIL, DeepLinkType.POI_DETAIL);
            LINK_DESTINATIONS.put(SHOP_DETAIL, DeepLinkType.POI_DETAIL);
            LINK_DESTINATIONS.put(DINING_DETAIL, DeepLinkType.POI_DETAIL);
            LINK_DESTINATIONS.put(ENTERTAINMENT_DETAIL, DeepLinkType.POI_DETAIL);
            LINK_DESTINATIONS.put(EVENT_DETAIL, DeepLinkType.POI_DETAIL);
            LINK_DESTINATIONS.put(EVENT_SERIES_DETAIL, DeepLinkType.EVENT_SERIES_DETAIL);
            LINK_DESTINATIONS.put(MY_FAVORITES, DeepLinkType.MY_FAVORITES);
            LINK_DESTINATIONS.put(PARKING_REMINDER, DeepLinkType.PARKING_REMINDER);
            LINK_DESTINATIONS.put(EXPLORE_IOA, DeepLinkType.EXPLORE_IOA);
            LINK_DESTINATIONS.put(EXPLORE_USF, DeepLinkType.EXPLORE_USF);
            LINK_DESTINATIONS.put(EXPLORE_CW, DeepLinkType.EXPLORE_CW);
            LINK_DESTINATIONS.put(EXPLORE_VOLCANO_BAY, DeepLinkType.EXPLORE_VB);
            LINK_DESTINATIONS.put(EXPLORE_RIDES, DeepLinkType.EXPLORE_RIDES);
            LINK_DESTINATIONS.put(EXPLORE_SHOWS, DeepLinkType.EXPLORE_SHOWS);
            LINK_DESTINATIONS.put(EXPLORE_DINING, DeepLinkType.EXPLORE_DINING);
            LINK_DESTINATIONS.put(EXPLORE_SHOPPING, DeepLinkType.EXPLORE_SHOPPING);
            LINK_DESTINATIONS.put(EXPLORE_WNW, DeepLinkType.EXPLORE_WNW);
            LINK_DESTINATIONS.put(LOCKERS, DeepLinkType.LOCKERS);
            LINK_DESTINATIONS.put(GUEST_SERVICES_LOCATIONS, DeepLinkType.GUEST_SERVICES_LOCATIONS);
            LINK_DESTINATIONS.put(HOURS, DeepLinkType.HOURS);
            LINK_DESTINATIONS.put(CONTACT, DeepLinkType.CONTACT);
            LINK_DESTINATIONS.put(WIFI_DETAIL, DeepLinkType.WIFI_DETAIL);
            LINK_DESTINATIONS.put(VACATION_PACKAGE_FAQ, DeepLinkType.VACATION_PACKAGE_FAQ);
            LINK_DESTINATIONS.put(WALKTHROUGH, DeepLinkType.WALKTHROUGH);
            LINK_DESTINATIONS.put(PRIVACY_POLICY, DeepLinkType.PRIVACY_POLICY);
            LINK_DESTINATIONS.put(PRIVACY_FAQ, DeepLinkType.PRIVACY_FAQ);
            LINK_DESTINATIONS.put(TERMS_OF_SERVICE, DeepLinkType.TERMS_OF_SERVICE);
            LINK_DESTINATIONS.put(TRADEMARKS, DeepLinkType.TRADEMARKS);
            LINK_DESTINATIONS.put(EXPRESS_PASS_DETAIL, DeepLinkType.EXPRESS_PASS_DETAIL);
            LINK_DESTINATIONS.put(DINING_PLAN_DETAIL, DeepLinkType.DINING_PLAN_DETAIL);
            LINK_DESTINATIONS.put(MY_ALERTS, DeepLinkType.MY_ALERTS);
            LINK_DESTINATIONS.put(SEARCH, DeepLinkType.SEARCH);
            LINK_DESTINATIONS.put(APP_LINK, DeepLinkType.APP_LINK);
            LINK_DESTINATIONS.put(PRIVACY_INFORMATION_CENTER, DeepLinkType.PRIVACY_INFORMATION_CENTER);
            LINK_DESTINATIONS.put(EXPLORE_CWH, DeepLinkType.EXPLORE_CWH);
            LINK_DESTINATIONS.put(EXPLORE_USH, DeepLinkType.EXPLORE_USH);
        }

        return LINK_DESTINATIONS;
    }

    private static Map<String, Integer> LINK_STRING_RESOURCES;

    private static Map<String, Integer> getLinkStringResources() {
        if (LINK_STRING_RESOURCES == null) {
            LINK_STRING_RESOURCES = new HashMap<String, Integer>();
            LINK_STRING_RESOURCES.put(RIDE_DETAIL, R.string.news_view_ride);
            LINK_STRING_RESOURCES.put(SHOW_DETAIL, R.string.news_view_show);
            LINK_STRING_RESOURCES.put(SHOP_DETAIL, R.string.news_view_shopping);
            LINK_STRING_RESOURCES.put(DINING_DETAIL, R.string.news_view_dining);
            LINK_STRING_RESOURCES.put(ENTERTAINMENT_DETAIL, R.string.news_view_entertainment);
            LINK_STRING_RESOURCES.put(EVENT_DETAIL, R.string.news_view_event);
            LINK_STRING_RESOURCES.put(EVENT_SERIES_DETAIL, R.string.news_view_event_series);
            LINK_STRING_RESOURCES.put(MY_FAVORITES, R.string.news_view_favorites);
            LINK_STRING_RESOURCES.put(PARKING_REMINDER, R.string.news_view_parking_reminder);
            LINK_STRING_RESOURCES.put(EXPLORE_IOA, R.string.news_view_islands_of_adventure);
            LINK_STRING_RESOURCES.put(EXPLORE_VOLCANO_BAY, R.string.news_view_volcano_bay);
            LINK_STRING_RESOURCES.put(EXPLORE_USF, R.string.news_view_universal_studios_florida);
            LINK_STRING_RESOURCES.put(EXPLORE_CW, R.string.news_view_city_walk);
            LINK_STRING_RESOURCES.put(EXPLORE_RIDES, R.string.news_view_rides);
            LINK_STRING_RESOURCES.put(EXPLORE_SHOWS, R.string.news_view_shows);
            LINK_STRING_RESOURCES.put(EXPLORE_DINING, R.string.news_view_dining);
            LINK_STRING_RESOURCES.put(EXPLORE_SHOPPING, R.string.news_view_shopping);
            LINK_STRING_RESOURCES.put(EXPLORE_WNW, R.string.news_view_wet_n_wild_orlando);
            LINK_STRING_RESOURCES.put(LOCKERS, R.string.news_view_lockers);
            LINK_STRING_RESOURCES.put(GUEST_SERVICES_LOCATIONS, R.string.news_view_guest_services_locations);
            LINK_STRING_RESOURCES.put(HOURS, R.string.news_view_hours_and_directions);
            LINK_STRING_RESOURCES.put(CONTACT, R.string.news_view_contact_guest_services);
            LINK_STRING_RESOURCES.put(WIFI_DETAIL, R.string.news_view_free_inpark_wifi);
            LINK_STRING_RESOURCES.put(VACATION_PACKAGE_FAQ, R.string.news_view_vacation_package_faq);
            LINK_STRING_RESOURCES.put(WALKTHROUGH, R.string.news_view_tutorial);
            LINK_STRING_RESOURCES.put(PRIVACY_POLICY, R.string.news_view_privacy_policy);
            LINK_STRING_RESOURCES.put(PRIVACY_FAQ, R.string.news_view_privcy_faq);
            LINK_STRING_RESOURCES.put(TERMS_OF_SERVICE, R.string.news_view_terms_of_service);
            LINK_STRING_RESOURCES.put(TRADEMARKS, R.string.news_view_copyright_and_trademarks);
            LINK_STRING_RESOURCES.put(EXPRESS_PASS_DETAIL, R.string.news_view_universal_express);
            LINK_STRING_RESOURCES.put(DINING_PLAN_DETAIL, R.string.news_view_universal_dining_plans);
            LINK_STRING_RESOURCES.put(MY_ALERTS, R.string.news_view_my_alerts);
            LINK_STRING_RESOURCES.put(SEARCH, R.string.news_view_search);
            LINK_STRING_RESOURCES.put(APP_LINK, R.string.news_view_default);
            LINK_STRING_RESOURCES.put(PRIVACY_INFORMATION_CENTER, R.string.name_information_privacy_information);
        }

        return LINK_STRING_RESOURCES;
    }

    private DeepLinkUtils() {}

    public static boolean isLinkAvailable(News news) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "isLinkAvailable");
        }
        return news != null && !TextUtils.isEmpty(news.getLinkDestination());
    }

    private static DeepLinkType getLinkType(News news) {
        if (news != null && !TextUtils.isEmpty(news.getLinkDestination())) {
            return getLinkDestinations().get(news.getLinkDestination());
        }
        return null;
    }

    public static boolean isDatabaseRequired(News news) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "isDatabaseRequired");
        }
        DeepLinkType deepLinkType = getLinkType(news);
        if (deepLinkType != null) {
            switch (deepLinkType) {
                case POI_DETAIL:
                    return true;
                case EVENT_SERIES_DETAIL:
                	return true;
                default:
                    break;
            }
        }

        return false;
    }

    public static int getLinkStringResId(News news) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "getLinkStringResId");
        }
        if (news != null && !TextUtils.isEmpty(news.getLinkDestination()) && getLinkStringResources().containsKey(news.getLinkDestination())) {
            return getLinkStringResources().get(news.getLinkDestination());
        }
        return R.string.news_view_default;
    }

    public static boolean loadDetailPage(News news, Context context, String venueObjectJson,
            String poiObjectJson, int poiTypeId) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "loadPage");
        }

        if (news != null && !TextUtils.isEmpty(news.getLinkDestination())) {
            switch (getLinkDestinations().get(news.getLinkDestination())) {
                case POI_DETAIL:
                    if (news.getLinkId() != null) {
                        DetailUtils.openDetailPage(context, venueObjectJson, poiObjectJson, poiTypeId, false, null);
                        return true;
                    }
                    break;
                case EVENT_SERIES_DETAIL:
                	if(news.getLinkId() != null) {
                        EventSeries eventSeries = EventSeries.fromJson(poiObjectJson, EventSeries.class);
                        Long mapTileId = null;
                        if (null != eventSeries) {
                            mapTileId = eventSeries.getCustomMapTileId();
                        }
                		DetailUtils.openEventSeriesDetailPage(context, venueObjectJson, poiObjectJson, mapTileId);
                		return true;
                	}
                default:
                    break;
            }
        }

        return false;
    }

    public static boolean loadPage(DeepLinkType deepLinkType, Context context) {
        switch (deepLinkType) {
            case MY_FAVORITES: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_favorites,
                        ExploreType.FAVORITES));
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_IOA: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_islands_of_adventure,
                        ExploreType.ISLANDS_OF_ADVENTURE));
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_USF: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity
                        .newInstanceBundle(R.string.drawer_item_universal_studios_florida,
                                ExploreType.UNIVERSAL_STUDIOS_FLORIDA));
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_VB: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_volcano_bay,
                        ExploreType.VOLCANO_BAY));
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_CW: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_citywalk_orlando,
                        ExploreType.CITY_WALK_ORLANDO));
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_RIDES: {
                if (BuildConfigUtils.isLocationFlavorHollywood()) {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity
                            .newInstanceBundle(R.string.drawer_item_universal_studios_hollywood,
                                    ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_rides,
                            ExploreType.RIDES));
                    context.startActivity(intent);
                }
                return true;
            }
            case EXPLORE_SHOWS: {
                if (BuildConfigUtils.isLocationFlavorHollywood()) {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity
                            .newInstanceBundle(R.string.drawer_item_universal_studios_hollywood,
                                    ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_shows,
                            ExploreType.SHOWS));
                    context.startActivity(intent);
                }
                return true;
            }
            case EXPLORE_DINING: {
                if (BuildConfigUtils.isLocationFlavorHollywood()) {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_universal_studios_hollywood,
                            ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_DINING));
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_dining,
                            ExploreType.DINING));
                    context.startActivity(intent);
                }
                return true;
            }
            case EXPLORE_SHOPPING: {
                if (BuildConfigUtils.isLocationFlavorHollywood()) {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_universal_studios_hollywood,
                            ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_SHOPPING));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_shopping,
                            ExploreType.SHOPPING));
                    context.startActivity(intent);
                }
                return true;
            }
            case EXPLORE_WNW: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_wet_n_wild,
                        ExploreType.WET_N_WILD));
                context.startActivity(intent);
                return true;
            }
            case LOCKERS: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_lockers,
                        ExploreType.LOCKERS));
                context.startActivity(intent);
                return true;
            }
            case GUEST_SERVICES_LOCATIONS: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(
                        R.string.drawer_item_guest_services_locations, ExploreType.GUEST_SERVICES_BOOTHS));
                context.startActivity(intent);
                return true;
            }
            case CONTACT: {
                Intent intent = new Intent(context, GuestServicesActivity.class);
                context.startActivity(intent);
                return true;
            }
            case WIFI_DETAIL: {
                Intent intent = new Intent(context, AboutWifiActivity.class);
                context.startActivity(intent);
                return true;
            }
            case VACATION_PACKAGE_FAQ: {
                Intent intent = new Intent(context, VacationPlannerFaqActivity.class);
                context.startActivity(intent);
                return true;
            }
            case WALKTHROUGH: {
                Intent intent = new Intent(context, TutorialActivity.class);
                context.startActivity(intent);
                return true;
            }
            case PRIVACY_POLICY: {
                Uri webPageUri = ServiceEndpointUtils.buildUri(ServiceEndpointUtils.URL_PATH_PRIVACY_POLICY);

                Bundle bundle = WebViewActivity.newInstanceBundle(
                        R.string.privacy_and_legal_option_privacy_policy, webPageUri.toString());
                context.startActivity(new Intent(context, WebViewActivity.class).putExtras(bundle));
                return true;
            }
            case PRIVACY_FAQ: {
                String privacyFaqAddress = ServiceEndpointUtils.getPrivacyFaqEndPoint();
                Uri webPageUri = ServiceEndpointUtils.buildUri(privacyFaqAddress);

                Bundle bundle = WebViewActivity.newInstanceBundle(
                        R.string.privacy_and_legal_option_privacy_faq, webPageUri.toString());
                context.startActivity(new Intent(context, WebViewActivity.class).putExtras(bundle));
                return true;
            }
            case TERMS_OF_SERVICE: {
                Uri webPageUri = ServiceEndpointUtils.buildUri(ServiceEndpointUtils.URL_PATH_TERMS_OF_SERVICE);

                Bundle bundle = WebViewActivity.newInstanceBundle(
                        R.string.privacy_and_legal_option_terms_of_service, webPageUri.toString());
                context.startActivity(new Intent(context, WebViewActivity.class).putExtras(bundle));
                return true;
            }
            case TRADEMARKS: {
                Uri webPageUri = ServiceEndpointUtils.buildUri(ServiceEndpointUtils.URL_PATH_COPYRIGHT);

                Bundle bundle = WebViewActivity.newInstanceBundle(
                        R.string.privacy_and_legal_option_copyright_and_trademarks, webPageUri.toString());
                context.startActivity(new Intent(context, WebViewActivity.class).putExtras(bundle));
                return true;
            }
            case EXPRESS_PASS_DETAIL: {
                Intent intent = new Intent(context, ExpressPassDetailActivity.class);
                context.startActivity(intent);
                return true;
            }
            case DINING_PLAN_DETAIL: {
                Intent intent = new Intent(context, DiningPlanDetailActivity.class);
                context.startActivity(intent);
                return true;
            }
            case MY_ALERTS: {
                Intent intent = new Intent(context, AlertsListActivity.class);
                context.startActivity(intent);
                return true;
            }
            case SEARCH: {
                Intent intent = new Intent(context, SearchActivity.class);
                context.startActivity(intent);
                return true;
            }
            case PARKING_REMINDER: {
                Intent intent = new Intent(context, ParkingReminderActivity.class);
                context.startActivity(intent);
                return true;
            }
            case HOURS: {
                Intent intent = new Intent(context, HoursAndDirectionsActivity.class);
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_USH: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_universal_studios_hollywood,
                        ExploreType.UNIVERSAL_STUDIOS_HOLLYWOOD_ATTRACTIONS));
                context.startActivity(intent);
                return true;
            }
            case EXPLORE_CWH: {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtras(ExploreActivity.newInstanceBundle(R.string.drawer_item_citywalk_hollywood,
                        ExploreType.CITY_WALK_HOLLYWOOD));
                context.startActivity(intent);
                return true;
            }
            case PRIVACY_INFORMATION_CENTER: {
                Bundle bundle = WebViewActivity.newInstanceBundle(
                        R.string.name_information_privacy_information, ServiceEndpointUtils.URL_PRIVACY_INFORMATION_CENTER);
                context.startActivity(new Intent(context, WebViewActivity.class).putExtras(bundle));
                return true;
            }
            default:
                break;
        }

        return false;
    }

    public static boolean loadPage(News news, Context context, FragmentManager fragmentManager,
            int containerId) {
        DeepLinkType deepLinkType = getLinkType(news);
        Log.d("DeepLinkUtils", "Link type is "+ deepLinkType.name());
        Log.d("DeepLinkUtils", "Link url is: "+news.getAndroidLink());
        if (deepLinkType != null  && news.getAndroidLink() != null && !"".equals(news.getAndroidLink())) {
            switch (deepLinkType) {
                case APP_LINK:
                    Log.d("DeepLinkUtils", "Link is external "+news.getIsExternalLink());
                    if(news.getIsExternalLink()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri webUri = Uri.parse(news.getAndroidLink());
                        if(webUri != null) {
                            intent.setData(webUri);
                            context.startActivity(intent);
                        }
                    } else {
                        Bundle bundle = WebViewActivity.newInstanceBundle(
                                news.getMessageHeading(), news.getAndroidLink());
                        context.startActivity(new Intent(context, WebViewActivity.class).putExtras(bundle));
                    }
                    return true;
                default:
                    return loadPage(deepLinkType, context);
            }

        }
        return false;
    }
}
