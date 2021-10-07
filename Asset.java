package SupplyChainManagement;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Assets {
	@Property()
	private String assetID;
	@Property()
	private String assetName;
	@Property()
	private String assetDescription;
	@Property()
	private String assetOwner;
	@Property()
	private String harvestDate;
	
	public String getAssetID() {
		return assetID;
	}
	public String getAssetName() {
		return assetName;
	}
	public String getAssetDescription() {
		return assetDescription;
	}
	public String getAssetOwner() {
		return assetOwner;
	}
	public String getHarvestDate() {
		return harvestDate;
	}
	public Assets(@JsonProperty("assetID") final String assetID,
			@JsonProperty("assetName") final String assetName,
			@JsonProperty("assetDescription") final String assetDescription,
			@JsonProperty("assetOwner") final String assetOwner,
			@JsonProperty("harvestDate") final String harvestDate) {
		
		this.assetID = assetID;
		this.assetName = assetName;
		this.assetDescription = assetDescription;
		this.assetOwner = assetOwner;
		this.harvestDate = harvestDate;
	}
}