package com.universalstudios.orlandoresort.controller.userinterface.linkentitlement;

import com.universalstudios.orlandoresort.controller.userinterface.linkentitlement.data.LinkEntitlementModel;

/**
 * @author tjudkins
 * @since 1/23/17
 */

interface LinkEntitlementListener {
    void onCancel();
    void onFirstFactorValidated(LinkEntitlementModel linkEntitlementModel);
    void onSecondFactorValidated(LinkEntitlementModel linkEntitlementModel);
    void onEntitlementAssigned();
}
