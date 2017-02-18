package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author Steven Byle
 */
@Parcel
public class GetPoisResponse extends NetworkResponse {

	@SerializedName("Rides")
	List<Ride> rides;

	@SerializedName("DiningLocations")
	List<Dining> dining;

	@SerializedName("Shows")
	List<Show> shows;

	@SerializedName("Hotels")
	List<Hotel> hotels;

	@SerializedName("Parades")
	List<Parade> parades;

	@SerializedName("Restrooms")
	List<Restroom> restrooms;

	@SerializedName("Atms")
	List<PointOfInterest> atms;

	@SerializedName("ParkingAreas")
	List<PointOfInterest> parking;

	@SerializedName("Shops")
	List<Shop> shops;

	@SerializedName("Rentals")
	List<RentalServices> rentalServices;

	@SerializedName("Lockers")
	List<Lockers> lockers;

	@SerializedName("ServiceAnimalRestAreas")
	List<PointOfInterest> serviceAnimalRestAreas;

	@SerializedName("SmokingAreas")
	List<PointOfInterest> smokingAreas;

	@SerializedName("PackagePickups")
	List<PointOfInterest> packagePickups;

	@SerializedName("LostAndFoundStations")
	List<PointOfInterest> lostAndFoundStations;

	@SerializedName("GuestServices")
	List<PointOfInterest> guestServices;

	@SerializedName("FirstAidStations")
	List<PointOfInterest> firstAidStations;

	@SerializedName("ChargingStations")
	List<PointOfInterest> chargingStations;

	@SerializedName("PhoneCardDispensers")
	List<PointOfInterest> phoneCardDispensers;

	@SerializedName("NightlifeLocations")
	List<Entertainment> entertainment;

	@SerializedName("WaterParks")
	List<PointOfInterest> waterParks;
	
	@SerializedName("Events")
	List<Event> events;

	@SerializedName("GeneralLocations")
	List<PointOfInterest> generalLocations;

	@SerializedName("Gateways")
	List<Gateway> gateways;

	/**
	 * @return the rides
	 */
	public List<Ride> getRides() {
		return rides;
	}

	/**
	 * @return the dining
	 */
	public List<Dining> getDining() {
		return dining;
	}

	/**
	 * @return the shows
	 */
	public List<Show> getShows() {
		return shows;
	}

	/**
	 * @return the hotels
	 */
	public List<Hotel> getHotels() {
		return hotels;
	}

	/**
	 * @return the parades
	 */
	public List<Parade> getParades() {
		return parades;
	}

	/**
	 * @return the restrooms
	 */
	public List<Restroom> getRestrooms() {
		return restrooms;
	}

	/**
	 * @return the atms
	 */
	public List<PointOfInterest> getAtms() {
		return atms;
	}

	/**
	 * @return the parking
	 */
	public List<PointOfInterest> getParking() {
		return parking;
	}

	/**
	 * @return the shops
	 */
	public List<Shop> getShops() {
		return shops;
	}

	/**
	 * @return the Rental Services
	 */
	public List<RentalServices> getRentalServices() {
		return rentalServices;
	}

	/**
	 * @return the lockers
	 */
	public List<Lockers> getLockers() {
		return lockers;
	}

	/**
	 * @return the serviceAnimalRestAreas
	 */
	public List<PointOfInterest> getServiceAnimalRestAreas() {
		return serviceAnimalRestAreas;
	}

	/**
	 * @return the smokingAreas
	 */
	public List<PointOfInterest> getSmokingAreas() {
		return smokingAreas;
	}

	/**
	 * @return the packagePickups
	 */
	public List<PointOfInterest> getPackagePickups() {
		return packagePickups;
	}

	/**
	 * @return the lostAndFoundStations
	 */
	public List<PointOfInterest> getLostAndFoundStations() {
		return lostAndFoundStations;
	}

	/**
	 * @return the guestServices
	 */
	public List<PointOfInterest> getGuestServices() {
		return guestServices;
	}

	/**
	 * @return the firstAidStations
	 */
	public List<PointOfInterest> getFirstAidStations() {
		return firstAidStations;
	}

	/**
	 * @return the chargingStations
	 */
	public List<PointOfInterest> getChargingStations() {
		return chargingStations;
	}

	/**
	 * @return the phoneCardDispensers
	 */
	public List<PointOfInterest> getPhoneCardDispensers() {
		return phoneCardDispensers;
	}

	/**
	 * @return the entertainment
	 */
	public List<Entertainment> getEntertainment() {
		return entertainment;
	}

	/**
	 * @return the waterParks
	 */
	public List<PointOfInterest> getWaterParks() {
		return waterParks;
	}

    /**
     * @return the events
     */
    public List<Event> getEvents() {
        return events;
    }

	/**
	 * @return the general locations
	 */
	public List<PointOfInterest> getGeneralLocations() {
		return generalLocations;
	}

	/**
	 * @return the gateways
	 */
	public List<Gateway> getGateways() {
		return gateways;
	}

}