package com.universalstudios.orlandoresort.frommergeneedsrefactor.permissions;

import java.util.List;

/**
 * Created by Kevin Haines on 5/5/16.
 * Project: Universal Orlando
 * Class Name: PermissionsRequestsListener
 * Class Description:
 */
public interface PermissionsRequestsListener {
    void onPermissionsUpdated(List<String> accepted, List<String> denied);
}
