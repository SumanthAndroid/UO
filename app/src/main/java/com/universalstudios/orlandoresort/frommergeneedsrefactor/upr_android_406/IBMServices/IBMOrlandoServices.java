package com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMServices;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.models.Address;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.CreateAccountRequest.CreateAccountRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.CreateTravelPartyRequest.CreateTravelPartyBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.LoginUserRequest.LoginRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ResetPasswordRequest.ResetPasswordRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdateCommunicationPrefsRequest.UpdateCommunicationPrefsRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdateContactInfoRequest.UpdateContactInfoRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdatePasswordRequest.UpdatePasswordRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.UpdatePersonalInfoRequest.UpdatePersonalInfoRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ValidateEmailAddressRequest.ValidateEmailAddressBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request_params.CreateLinkedAccountRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.AddUpdateGuestAddressesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateAccountResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateLinkedAccountResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.CreateTravelPartyResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.DeleteGuestAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ForgotPasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestPreferencesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetGuestProfileResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.GetTravelPartyResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.LoginUserResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ResetPasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateCommunicationPrefsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdateContactInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdatePasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.UpdatePersonalInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ValidateEmailAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.request.AddItemRequest.AddItemRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.AddItem.response.AddItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress.AddBillingAddressRequest.AddBillingAddressBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.BillingAddress.AddBillingAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.request.UpdateItemRequest.UpdateItemRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.UpdateItem.response.UpdateItemResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo.CartShippingInfoRequest.CartShippingInfoRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Cart.shippinginfo.CartShippingInfoResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Countries.GetCountriesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity.GetAuthNRequest.GetGuestIdentityBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.GuestIdentity.GetAuthNResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ShippingModeData.GetEligibleShipModesResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.State.GetStateResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.CartTicketGroupingRequest.CartTicketGroupingRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.TicketGrouping.TicketGroupingResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.GetTridionConfigsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.UnregisteredUser.GetUnregisteredGuestRequest.GetUnregisteredGuestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.UnregisteredUser.GetUnregisteredGuestResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.ValidateZip.ValidateZipResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog.GetCategoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog.GetProductDetailsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog.GetProductSummaryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.catalog.GetSubcategoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetCatalogCategoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetCatalogSubcategoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetPersonalizationExtrasRequest.GetPersonalizationExtrasRequestBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.extras.GetPersonalizationExtrasResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.GetPriceInventoryRequest.GetPriceInventoryRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.inventory.GetPriceInventoryResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersRequest.GetPersonalizationOffersRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.offers.GetPersonalizationOffersResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.GetCardsRequest.GetCardsBodyParams;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.requests.SubmitPurchaseRequest.SubmitPurchaseRequestBody;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetCardsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.GetTridionSpecsResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.tickets.responses.SubmitPurchaseResponse;
import com.universalstudios.orlandoresort.model.network.domain.account.GetAccountStatusResponse;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.AddPromoCodeRequest.AddPromoCodeRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.AddPromoCodeResponse;
import com.universalstudios.orlandoresort.model.network.domain.cart.promo.RemovePromoCodeResponse;
import com.universalstudios.orlandoresort.model.network.domain.checkout.GetFlexpayContractRequest.GetFlexpayContractRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.checkout.GetFlexpayContractResponse;
import com.universalstudios.orlandoresort.model.network.domain.control.GetCommerceEnabledResponse;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetLinkEntitlementsMethodsRequest.GetLinkEntitlementsMethodsRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetLinkEntitlementsMethodsResponse;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.GetSalesProgramsResponse;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.LinkEntitlementsRequest.LinkEntitlementsRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.linkentitlements.LinkEntitlementsResponse;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferAcceptedRequest.OfferAcceptedRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.offers.OfferAcceptedResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.AddCardRequest.AddCardRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.AddCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.DeleteCardRequest.DeleteCardRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.DeleteCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.EditCardRequest.EditCardRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.EditCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetFolioTransactionsResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletCardsResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.GetWalletFolioResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.ModifyPinRequest.ModifyPinRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.ModifyPinResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetPrimaryCardRequest.SetPrimaryCardRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetPrimaryCardResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitAlertRequest.SetSpendingLimitAlertRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitAlertResponse;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitRequest.SetSpendingLimitRequestBodyParams;
import com.universalstudios.orlandoresort.model.network.domain.wallet.SetSpendingLimitResponse;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.http.RestMethod;

/**
 * Network API endpoints.
 *
 */
@SuppressWarnings("javadoc")
public interface IBMOrlandoServices {

    /**
     * retrofit 1.9 does not support DELETE requests with bodies, this is a workaround
     * https://github.com/square/retrofit/issues/458
     */
    @Documented
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @RestMethod(value = "DELETE", hasBody = true)
    @interface RetrofitDeleteWithBody {
        String value();
    }


    /**
	 * Registration
	 */
	@POST("/guest/GuestProfiles/createUnregistered")
	public void createUnregisterGuest(@Body GetUnregisteredGuestBodyParams body, Callback<GetUnregisteredGuestResponse> success);

	@POST("/guest/GuestProfiles/createUnregistered")
	public GetUnregisteredGuestResponse createUnregisterGuest(@Body GetUnregisteredGuestBodyParams body);

	/**
	 * Guest Identity
	 */
	@POST("/guest/guestProfiles/commerce/authN")
	public GetAuthNResponse postAuthN(@Body GetGuestIdentityBodyParams bodyParams);

	/**
	 * Guest Identity
	 */
	@POST("/guest/guestProfiles/commerce/authN")
	public void postAuthN(@Body GetGuestIdentityBodyParams bodyParams, Callback<GetAuthNResponse> success);

	/**
	 * Validate ZIP code
     */
	@GET("/apic/Utilities/validateFLresident")
	public ValidateZipResponse validateFLZIP(@Query("zipCode") String zip);

	@GET("/apic/Utilities/validateFLresident")
	public void validateFLZIP(@Query("zipCode") String zip, Callback<ValidateZipResponse> cb);

	/**
	 * Adds item to the cart
     */
	@POST("/{shopJunction}/wcs/resources/store/{storeId}/cart")
	public void addCartItem(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body AddItemRequestBodyParams requestBody, Callback<AddItemResponse> cb);

	@POST("/{shopJunction}/wcs/resources/store/{storeId}/cart")
	public AddItemResponse addCartItem(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body AddItemRequestBodyParams requestBody);

	/**
	 * Retrieves ticket groups in cart
     */
	@POST("/cp/personalization/getcart")
	TicketGroupingResponse getTicketingGroupsInCart(@Header("cache-control") String cacheVal, @Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @QueryMap Map<String, String> queryMap, @Body CartTicketGroupingRequestBodyParams bodyParams);

	@POST("/cp/personalization/getcart")
	void getTicketingGroupsInCart(@Header("cache-control") String cacheVal, @Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @QueryMap Map<String, String> queryMap, @Body CartTicketGroupingRequestBodyParams bodyParams, Callback<TicketGroupingResponse> cb);

	/**
	 * Add promo code to cart
	 */
	@POST("/shop/wcs/resources/store/{storeId}/cart/@self/assigned_promotion_code")
	AddPromoCodeResponse addPromoCodeToCart(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("storeId") String storeId, @Body AddPromoCodeRequestBodyParams requestBody);

	@POST("/shop/wcs/resources/store/{storeId}/cart/@self/assigned_promotion_code")
	void addPromoCodeToCart(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("storeId") String storeId, @Body AddPromoCodeRequestBodyParams requestBody, Callback<AddPromoCodeResponse> cb);

	/**
	 * Remove promo code from cart
	 */
	@DELETE("/shop/wcs/resources/store/{storeId}/cart/@self/assigned_promotion_code/{promoCode}")
	RemovePromoCodeResponse removePromoCodeFromCart(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("storeId") String storeId, @Path("promoCode") String promoCode);

	@DELETE("/shop/wcs/resources/store/{storeId}/cart/@self/assigned_promotion_code/{promoCode}")
	void removePromoCodeFromCart(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("storeId") String storeId, @Path("promoCode") String promoCode, Callback<RemovePromoCodeResponse> cb);

	/**
	 * Adds a billing address for the guest
     */
	@POST("/{shopJunction}/wcs/resources/store/{storeId}/person/@self/contact")
	public void addBillingAddress(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body AddBillingAddressBody itemData, Callback<AddBillingAddressResponse> cb);

	@POST("/{shopJunction}/wcs/resources/store/{storeId}/person/@self/contact")
	public AddBillingAddressResponse addBillingAddress(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body AddBillingAddressBody itemData);

	/**
	 * Retrieves a list of states
     */
	@GET("/apic/Utilities/list/provState")
	public void getStates(Callback<GetStateResponse> cb);

	@GET("/apic/Utilities/list/provState")
	public GetStateResponse getStates();

	@PUT("/{shopJunction}/wcs/resources/store/{storeId}/cart/@self/update_order_item")
	public void updateCartItem(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body UpdateItemRequestBodyParams itemData, Callback<UpdateItemResponse> cb);

	@PUT("/{shopJunction}/wcs/resources/store/{storeId}/cart/@self/update_order_item")
	public UpdateItemResponse updateCartItem(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body UpdateItemRequestBodyParams itemData);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/cart/eligible_ship_modes")
	public void getEligibleShipModes(@Header("cache-control") String cacheVal, @Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Query("langId") String langId, @Query("contractId") String contractId, Callback<GetEligibleShipModesResponse> cb);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/cart/eligible_ship_modes")
	public GetEligibleShipModesResponse getEligibleShipModes(@Header("cache-control") String cacheVal, @Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Query("langId") String langId, @Query("contractId") String contractId);

	@PUT("/{shopJunction}/wcs/resources/store/{storeId}/cart/@self/shipping_info")
	public void updateCartShippingInfo(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body CartShippingInfoRequestBodyParams itemData, Callback<CartShippingInfoResponse> cb);

	@PUT("/{shopJunction}/wcs/resources/store/{storeId}/cart/@self/shipping_info")
	public CartShippingInfoResponse updateCartShippingInfo(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body CartShippingInfoRequestBodyParams itemData);

	/**
	 * Retrieves a list of countries
     */
	@GET("/apic/Utilities/list/countries")
	public void getCountries(Callback<GetCountriesResponse> cb);

	@GET("/apic/Utilities/list/countries")
	public GetCountriesResponse getCountries();

	/**
	 * Creates a new account
     */
	@POST("/guest/GuestProfiles/createRegistered")
	void createAccount(@Body CreateAccountRequestBodyParams requestBody, Callback<CreateAccountResponse> cb);

	@POST("/guest/GuestProfiles/createRegistered")
	CreateAccountResponse createAccount(@Body CreateAccountRequestBodyParams requestBody);

	/**
	 * Creates a linked user account
     */
	@POST("/guest/guestprofiles/createLinkedAccount")
	void createLinkedAccount(@Body CreateLinkedAccountRequestBody requestBody, Callback<CreateLinkedAccountResponse> cb);

	@POST("/guest/guestprofiles/createLinkedAccount")
	CreateLinkedAccountResponse createLinkedAccount(@Body CreateLinkedAccountRequestBody requestBody);

	/**
	 * Updates the user's communication preferences
	 */
	@PUT("/guest/GuestProfiles/{guestId}/preferences")
	void updateCommunicationPrefs(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body UpdateCommunicationPrefsRequestBodyParams communicationPreference, Callback<UpdateCommunicationPrefsResponse> cb);

	/**
	 * Updates the user's communication preferences
	 */
	@PUT("/guest/GuestProfiles/{guestId}/preferences")
	UpdateCommunicationPrefsResponse updateCommunicationPrefs(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body UpdateCommunicationPrefsRequestBodyParams communicationPreference);

	/**
	 * Call for forgot password feature
     */
	@GET("/cred/auth/forgotPassword/{email}")
	ForgotPasswordResponse forgotPassword(@Path("email") String email);

	/**
	 * Call for forgot password feature
	 */
	@GET("/cred/auth/forgotPassword/{email}")
	void forgotPassword(@Path("email") String email, Callback<ForgotPasswordResponse> cb);

	/**
	 * Call for reset password feature
	 */
	@PUT("/cred/auth/resetPassword/{email}/{requestId}")
	void resetPassword(@Path("email") String email, @Path("requestId") String requestId, @Body ResetPasswordRequestBody requestBody, Callback<ResetPasswordResponse> cb);

	/**
	 * Call for reset password feature
	 */
	@PUT("/cred/auth/resetPassword/{email}/{requestId}")
	ResetPasswordResponse resetPassword(@Path("email") String email, @Path("requestId") String requestId, @Body ResetPasswordRequestBody requestBody);

	/**
	 * Call for updating password
	 */
	@PUT("/cred/auth/changePassword/{userId}")
	void updatePassword(@Path("userId") String userId, @Body UpdatePasswordRequestBodyParams body, Callback<UpdatePasswordResponse> cb);

	/**
	 * Call for updating password
	 */
	@PUT("/cred/auth/changePassword/{userId}")
	UpdatePasswordResponse updatePassword(@Path("userId") String userId, @Body UpdatePasswordRequestBodyParams body);

	/**
	 * Retrieves profile information for a guest
     */
    @GET("/guest/GuestProfiles")
    void getGuestProfile(@Header("Authorization") String auth, @Query("email") String email, Callback<GetGuestProfileResponse> cb);

	@GET("/guest/GuestProfiles")
	GetGuestProfileResponse getGuestProfile(@Header("Authorization") String auth, @Query("email") String email);

	/**
	 * Updates a guest's contact information
	 */
	@PUT("/guest/GuestProfiles/{guestId}/contact")
	void updateContactInfo(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body UpdateContactInfoRequestBodyParams bodyParams, Callback<UpdateContactInfoResponse> cb);

	/**
	 * Updates a guest's contact information
     */
	@PUT("/guest/GuestProfiles/{guestId}/contact")
	UpdateContactInfoResponse updateContactInfo(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body UpdateContactInfoRequestBodyParams bodyParams);

	/**
	 * Updates a guest's personal information (updated GuestProfile)
	 */
	@PUT("/guest/GuestProfiles/{guestId}")
	UpdatePersonalInfoResponse updatePersonalInfo(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body UpdatePersonalInfoRequestBody guestProfile);

	@PUT("/guest/GuestProfiles/{guestId}")
	void updatePersonalInfo(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body UpdatePersonalInfoRequestBody guestProfile, Callback<UpdatePersonalInfoResponse> cb);

	/**
	 * Adds a new address for a guest
     */
    @PUT("/guest/GuestProfiles/{guestId}/addresses")
	AddUpdateGuestAddressesResponse addGuestAddress(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body Address guestAddressRequestBody);

    @PUT("/guest/GuestProfiles/{guestId}/addresses")
    void addGuestAddress(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body Address guestAddressRequestBody, Callback<AddUpdateGuestAddressesResponse> cb);

	/**
	 * Deletes a guest's address
	 */
    @DELETE("/guest/GuestProfiles/{guestId}/addresses/{addressId}")
	DeleteGuestAddressResponse deleteGuestAddress(@Header("Authorization") String auth, @Path("guestId") String guestId, @Path("addressId") String addressId);

    @DELETE("/guest/GuestProfiles/{guestId}/addresses/{addressId}")
    void deleteGuestAddress(@Header("Authorization") String auth, @Path("guestId") String guestId, @Path("addressId") String addressId, Callback<DeleteGuestAddressResponse> cb);

	/**
	 * Adds/updates list of addresses for the given guest
     */
    @PUT("/guest/GuestProfiles/{guestId}/addresses")
    AddUpdateGuestAddressesResponse addUpdateGuestAddresses(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body List<Address> addresses);

    @PUT("/guest/GuestProfiles/{guestId}/addresses")
    void addUpdateGuestAddresses(@Header("Authorization") String auth, @Path("guestId") String guestId, @Body List<Address> addresses, Callback<AddUpdateGuestAddressesResponse> cb);

	/**
	 * Retrieves all guest preferences
     */
	@GET("/guest/GuestProfiles/list/guestPreferences")
	GetGuestPreferencesResponse getGuestPreferences();

	@GET("/guest/GuestProfiles/list/guestPreferences")
	void getGuestPreferences(Callback<GetGuestPreferencesResponse> cb);

	/**
	 * Performs the guest login
	 */
	@POST("/cred/auth/login")
	void loginUser(@Body LoginRequestBody body, Callback<LoginUserResponse> cb);

	/**
	 * Performs the guest login
	 */
	@POST("/cred/auth/login")
	LoginUserResponse loginUser(@Body LoginRequestBody body);

	/**
	 * Retrieves travel party members
     */
	@GET("/guest/travelparty/retrieveassignednames")
	GetTravelPartyResponse getTravelPartyMembers(@Header("Authorization") String auth);

	@GET("/guest/travelparty/retrieveassignednames")
	void getTravelPartyMembers(@Header("Authorization") String auth, Callback<GetTravelPartyResponse> callback);

	/**
	 * Creates a travel party member
     */
	@POST("/guest/TravelParty/createAssignee")
    CreateTravelPartyResponse createTravelPartyMember(@Header("Authorization") String auth, @Body CreateTravelPartyBodyParams body);

	@POST("/guest/TravelParty/createAssignee")
	void createTravelPartyMember(@Header("Authorization") String auth, @Body CreateTravelPartyBodyParams assignee, Callback<CreateTravelPartyResponse> body);

    // Commerce-related requests

	/**
	 * Retrieves product cards
     */
    @POST("/cp/personalization/gettickets")
    void getCommerceCards(@Body GetCardsBodyParams body, Callback<GetCardsResponse> cb);

	@POST("/cp/personalization/gettickets")
	GetCardsResponse getCommerceCards(@Body GetCardsBodyParams body);

	/**
	 * Retrieves tridion strings for certain tridion keys
	 */
	@GET("/cp/content/pages")
	GetTridionSpecsResponse getTridionTicketSpecs(@Query("ids") List<String> ids);

	/**
	 * Retrieves tridion strings for certain tridion keys
     */
	@GET("/cp/content/pages")
	void getTridionTicketSpecs(@Query("ids") List<String> ids, Callback<GetTridionSpecsResponse> cb);

	/**
	 * Submits a purchase payment
     */
	@POST("/pay/payment/card")
	void submitPurchase(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken,
						@Body SubmitPurchaseRequestBody body, Callback<SubmitPurchaseResponse> cb);

	@POST("/pay/payment/card")
	SubmitPurchaseResponse submitPurchase(@Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken,
						@Body SubmitPurchaseRequestBody body);

	/**
	 * Retrieves all tridion string keys/values
     */
	@GET("/cp/content/config/icedata")
	void getTridionConfigs(Callback<GetTridionConfigsResponse> cb);

	@GET("/cp/content/config/icedata")
	GetTridionConfigsResponse getTridionConfigs();

	/**
	 * Validates the given email address
     */
	@POST("/utils/utilities/validateEmail")
	public ValidateEmailAddressResponse validateEmailAddress(@Body ValidateEmailAddressBodyParams body);

	@POST("/utils/utilities/validateEmail")
	public void validateEmailAddress(@Body ValidateEmailAddressBodyParams body, Callback<ValidateEmailAddressResponse> callback);

	// Categories - main/base/old services

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/categoryview/@top")
	public GetCategoryResponse getCategories(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/categoryview/@top")
	public void getCategories(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, Callback<GetCategoryResponse> callback);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/categoryview/byParentCategory/{categoryId}")
	public GetSubcategoryResponse getSubCategoriesByCategoryId(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Path("categoryId") String categoryId);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/categoryview/byParentCategory/{categoryId}")
	public void getSubCategoriesByCategoryId(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Path("categoryId") String categoryId, Callback<GetSubcategoryResponse> callback);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/productview/byCategory/{categoryId}")
	public GetProductSummaryResponse getProductSummaryByCategoryId(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Path("categoryId") String categoryId);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/productview/byCategory/{categoryId}")
	public void getProductSummaryByCategoryId(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Path("categoryId") String categoryId, Callback<GetProductSummaryResponse> callback);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/productview/byId/{categoryId}")
	public void getProductDetailsByCategoryId(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Path("categoryId") String categoryId, Callback<GetProductDetailsResponse> callback);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/productview/byId/{categoryId}")
	public GetProductDetailsResponse getProductDetailsByCategoryId(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Path("categoryId") String categoryId);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/productview/byIds")
	public void getProductDetailsByIds(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Query("id") List<String> ids, Callback<GetProductDetailsResponse> callback);

	@GET("/{shopJunction}/wcs/resources/store/{storeId}/productview/byIds")
	public GetProductDetailsResponse getProductDetailsByIds(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Query("id") List<String> ids);

	// Categories - Microservices

	/**
	 * Get extras category
     */
	@GET("/cp/catalog/category")
	public GetCatalogCategoryResponse getCatalogCategories();

	@GET("/cp/catalog/category")
	public void getCatalogCategories(Callback<GetCatalogCategoryResponse> callback);

	/**
	 * Get extras subcategories for a category
	 */
	@GET("/cp/catalog/subcategory/{categoryId}")
	public GetCatalogSubcategoryResponse getCatalogSubcategory(@Path("categoryId") String categoryId);

	@GET("/cp/catalog/subcategory/{categoryId}")
	public void getCatalogSubcategory(@Path("categoryId") String categoryId, Callback<GetCatalogSubcategoryResponse> callback);

	/**
	 * Get all extras, given the body
	 */
	@POST("/cp/personalization/getextras")
	public GetPersonalizationExtrasResponse getPersonalizationExtras(@Body GetPersonalizationExtrasRequestBodyParams body);

	@POST("/cp/personalization/getextras")
	public void getPersonalizationExtras(@Body GetPersonalizationExtrasRequestBodyParams body, Callback<GetPersonalizationExtrasResponse> callback);

	/**
	 * Get all offers, given the body
	 */
	@POST("/cp/personalization/getoffers")
	public GetPersonalizationOffersResponse getPersonalizationOffers(@Body GetPersonalizationOffersRequestBody body);

	@POST("/cp/personalization/getoffers")
	public void getPersonalizationOffers(@Body GetPersonalizationOffersRequestBody body, Callback<GetPersonalizationOffersResponse> callback);

	/**
	 * Track accepted offer
	 */
	@POST("/cp/personalization/offersaccepted")
	OfferAcceptedResponse getOffersAccepted(@Body OfferAcceptedRequestBodyParams body);

	@POST("/cp/personalization/offersaccepted")
	void getOffersAccepted(@Body OfferAcceptedRequestBodyParams body, Callback<OfferAcceptedResponse> cb);

	// Price Inventory

	/**
	 * Get inventory information for a contract
     */
	@POST("/{shopJunction}/wcs/resources/store/{storeId}/event/priceAndInventory")
	public GetPriceInventoryResponse getPriceInventory(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body GetPriceInventoryRequestBody body);

	@POST("/{shopJunction}/wcs/resources/store/{storeId}/event/priceAndInventory")
	public void getPriceInventory(@Path("shopJunction") String shopJunction, @Path("storeId") String storeId, @Body GetPriceInventoryRequestBody body, Callback<GetPriceInventoryResponse> callback);

	/**
	 * Get flexpay retrieve contract
	 */
	@POST("/flexpay/Flexpay/retrieveContract")
	public GetFlexpayContractResponse getFlexpayContract(@Header("Authorization") String auth, @Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Body GetFlexpayContractRequestBodyParams body);

	@GET("/folio/folios/{guestId}/cards")
	public GetWalletCardsResponse getWalletCards(@Header("Authorization") String auth, @Path("guestId") String guestId);

	@GET("/folio/folios/{guestId}/cards")
	public void getWalletCards(@Header("Authorization") String auth, @Path("guestId") String guestId, Callback<GetWalletCardsResponse> cb);

	@GET("/folio/folios/{guestId}")
	public GetWalletFolioResponse getWalletFolio(@Header("Authorization") String auth, @Path("guestId") String guestId);

	@GET("/folio/folios/{guestId}")
    public void getWalletFolio(@Header("Authorization") String auth, @Path("guestId") String guestId, Callback<GetWalletFolioResponse> cb);

	@POST("/flexpay/Flexpay/retrieveContract")
	public void getFlexpayContract(@Header("Authorization") String auth, @Header("WCToken") String wsToken, @Header("WCTrustedToken") String wsRefreshToken, @Body GetFlexpayContractRequestBodyParams body, Callback<GetFlexpayContractResponse> callback);

	/**
	 * Endpoint for checking account and email status.
     */
	@GET("/guest/guestProfiles/accountAvailable")
	GetAccountStatusResponse getAccountStatus(@Query("email") String email);

	@GET("/guest/guestProfiles/accountAvailable")
	void getAccountStatus(@Query("email") String email, Callback<GetAccountStatusResponse> callback);

	/**
	 * Adds a card to the guest's wallet
	 */
	@PUT("/pay/payment/wallet")
	AddCardResponse addCardToWallet(@Header("Authorization") String auth, @Body AddCardRequestBodyParams requestBody);

	@PUT("/pay/payment/wallet")
	void addCardToWallet(@Header("Authorization") String auth, @Body AddCardRequestBodyParams requestBody, Callback<AddCardResponse> callback);

	/**
	 * Performs an edit on a card to the guest's wallet
	 */
	@PUT("/pay/payment/wallet")
	EditCardResponse editCardInWallet(@Header("Authorization") String auth, @Body EditCardRequestBodyParams requestBody);

	@PUT("/pay/payment/wallet")
	void editCardInWallet(@Header("Authorization") String auth, @Body EditCardRequestBodyParams requestBody, Callback<EditCardResponse> callback);

	/**
	 * Sets the primary card for a guest.
	 */
	@PUT("/folio/folios/card")
	SetPrimaryCardResponse setPrimaryCard(@Header("Authorization") String auth, @Body SetPrimaryCardRequestBodyParams requestBody);

	@PUT("/folio/folios/card")
	void setPrimaryCard(@Header("Authorization") String auth, @Body SetPrimaryCardRequestBodyParams requestBody, Callback<SetPrimaryCardResponse> callback);

	/**
	 * Deletes a card from the guest's wallet
	 */
    @RetrofitDeleteWithBody("/folio/folios/{externalGuestId}/card")
	DeleteCardResponse deleteCardFromWallet(@Path("externalGuestId") String externalGuestId, @Header("Authorization") String auth, @Body DeleteCardRequestBodyParams requestBody);

    @RetrofitDeleteWithBody("/folio/folios/{externalGuestId}/card")
	void deleteCardFromWallet(@Path("externalGuestId") String externalGuestId, @Header("Authorization") String auth, @Body DeleteCardRequestBodyParams requestBody, Callback<DeleteCardResponse> callback);

	/**
	 * Sets the guest's spending limit.
     */
	@POST("/folio/folios/{guestId}/limit")
	SetSpendingLimitResponse setSpendingLimit(@Path("guestId") String guestId, @Header("Authorization") String auth, @Body SetSpendingLimitRequestBodyParams requestBody);

	@POST("/folio/folios/{guestId}/limit")
	void setSpendingLimit(@Path("guestId") String guestId, @Header("Authorization") String auth, @Body SetSpendingLimitRequestBodyParams requestBody, Callback<SetSpendingLimitResponse> callback);
	/**
	 * Endpoint for retrieving sales programs.
	 */
	@GET("/et/SalesPrograms")
	GetSalesProgramsResponse getSalesPrograms();

	@GET("/et/SalesPrograms")
	void getSalesPrograms(Callback<GetSalesProgramsResponse> callback);

	/**
	 * Endpoint for retrieving link entitlements methods.
	 * @param bodyParams
	 */
	@POST("/et/Entitlements/link/methods")
	GetLinkEntitlementsMethodsResponse getLinkEntitlementsMethods(@Body GetLinkEntitlementsMethodsRequestBodyParams bodyParams);

	@POST("/et/Entitlements/link/methods")
	void getLinkEntitlementsMethods(@Body GetLinkEntitlementsMethodsRequestBodyParams bodyParams, Callback<GetLinkEntitlementsMethodsResponse> callback);

	/**
	 * Endpoint for linking entitlements
	 * @param bodyParams
	 */
	@POST("/et/Entitlements/link")
	LinkEntitlementsResponse linkEntitlements(@Body LinkEntitlementsRequestBodyParams bodyParams);

	@POST("/et/Entitlements/link")
	void linkEntitlements(@Body LinkEntitlementsRequestBodyParams bodyParams, Callback<LinkEntitlementsResponse> callback);

	/**
	 * Retrieves transactions for a given guest.
     */
	@GET("/folio/folios/{guestId}/transactions")
	public GetFolioTransactionsResponse getFolioTransactions(@Header("Authorization") String auth, @Path("guestId") String guestId);

	@GET("/folio/folios/{guestId}/transactions")
	public void getFolioTransactions(@Header("Authorization") String auth, @Path("guestId") String guestId, Callback<GetFolioTransactionsResponse> cb);

	/**
	 * Set e-folio spending limit alert preferences
     */
	@POST("/folio/folios/{guestId}/alert/preferences")
	SetSpendingLimitAlertResponse setSpendingLimitAlert(@Path("guestId") String guestId, @Header("Authorization") String auth, @Body SetSpendingLimitAlertRequestBodyParams requestBody);

	@POST("/folio/folios/{guestId}/alert/preferences")
	void setSpendingLimitAlert(@Path("guestId") String guestId, @Header("Authorization") String auth, @Body SetSpendingLimitAlertRequestBodyParams requestBody, Callback<SetSpendingLimitAlertResponse> callback);

	/**
	 * Modifies a guest's PIN, or adds it if it doesn't exist.
	 */
	@POST("/folio/folios/{guestId}/pin")
	ModifyPinResponse modifyCardPin(@Path("guestId") String guestId, @Header("Authorization") String auth, @Body ModifyPinRequestBodyParams requestBody);

	@POST("/folio/folios/{guestId}/pin")
	void modifyCardPin(@Path("guestId") String guestId, @Header("Authorization") String auth, @Body ModifyPinRequestBodyParams requestBody, Callback<ModifyPinResponse> callback);

	@GET("/utils/utilities/appActive/{versionuri}")
	GetCommerceEnabledResponse getCommerceEnabled(@Path("versionuri") String versionName);

	@GET("/utils/utilities/appActive/{versionuri}")
	void getCommerceEnabled(@Path("versionuri") String versionName, Callback<GetCommerceEnabledResponse> cb);

}
