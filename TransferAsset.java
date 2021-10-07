package SupplyChainManagement;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract(
        name = "SupplyChainManagement",
        info = @Info(
                title = "SupplyChainManagement contract",
                description = "Hyperledger Fabric Contract to transfer assets from producer to distributor and retailer",
                version = "0.0.1-SNAPSHOT"))
 
@Default
public final class TransferAssets implements ContractInterface{
	// Creating Genson object which will get used to convert object to String object
	private final Genson json = new Genson();
	// creating error enum for handling exceptions
	private enum SupplyChainErrors{
		ASSET_NOT_FOUND,
		ASSET_ALREADY_EXISTS,
		UNABLE_TO_TRANSFER_ASSET,
		DISTRIBUTOR_NOT_FOUND,
		RETAILER_NOT_FOUND
	}
	@Transaction
	public void initLedger(final Context ctx) {
		//geting instance of the ledger
		ChaincodeStub stub = ctx.getStub();
		// creating first asset during the ledger is initialization
		Assets asset1  = new Assets("1","Mangoes","This new Asset Mango","Santosh Singh","21-Sep-2021");
		// creating second asset during the ledger is initialization
		Assets asset2 = new Assets("2","Apples","This new Asset Apple","Santosh Singh","21-Sep-2021");
		// creating first producer and associating assets owned by producer
		Producer producer1  = new Producer("Santosh Singh","34,Phantom Street, Pune",asset1);
		// Single producer can own multiple assets, hence creating and associating second asset with producer
		Producer producer2 = new Producer("Santosh Singh","34,Phantom Street, Pune",asset2);
		// During ledger initialization creating new distributor on the ledger
		Distributor distributor = new Distributor("Bharat","5th Main, 8th cross, Bangalore","22-Sep-2021");
		// During ledger initialization creating new retailer on the ledger
		Retailer retailer = new Retailer("Rohit Mathur","Bhandarkar Road, Kondhwa, Pune", "22-Sep-2021");
		// Converting first Asset OBject to String object so that we can store it on ledger
		String strAsset1 = json.serialize(asset1);
		// Converting second Asset OBject to String object so that we can store it on ledger
		String strAsset2 = json.serialize(asset2);
		// Converting first producer OBject to String object so that we can store it on ledger
		String strProd1 = json.serialize(producer1);
		// Converting second producer OBject to String object so that we can store it on ledger
		String strProd2 = json.serialize(producer2);
		// Converting distributor Object to String object so that we can store it on ledger
		String strDist = json.serialize(distributor);
		// Converting retailer Object to String object so that we can store it on ledger
		String strReta = json.serialize(retailer);
		// Storing first asset on the ledger
		stub.putStringState("1", strAsset1);
		// Storing second asset on the ledger
		stub.putStringState("2", strAsset2);
		// Storing first producer on the ledger
		stub.putStringState("Santosh Singh", strProd1);
		// Storing second producer on the ledger
		stub.putStringState("Santosh Singh", strProd2);
		// Storing distributor on the ledger
		stub.putStringState("Bharat", strDist);
		// Storing retailer on the ledger
		stub.putStringState("Rohit", strReta);
	}
	@Transaction
	public Assets addNewAsset(final Context ctx, 
			final String assetID,
			final String assetName, 
			final String assetDesc,
			final String assetOWner,
			final String harvestDate) {
		// Getting ledger context
		ChaincodeStub stub =  ctx.getStub();
		// finding if suppled assetID exists on the ledger
		String strAsset = stub.getStringState(assetID);
		// if supplied asset ID exists on the ledger then throw an exception that asset already exists
		if(!strAsset.isEmpty()) {
			String errorMsg = String.format("The Asset %s that you are trying to add, already exists on ledger");
			throw new ChaincodeException(errorMsg, SupplyChainErrors.ASSET_ALREADY_EXISTS.toString());
		}
		// Creating new Asset Object by passing supplied asset attributes
		Assets asset  = new Assets(assetID,assetName,assetDesc,assetOWner,harvestDate);
		// Converting asset object into String object which can be stored in the ledger
		String newAsset = json.serialize(asset);
		// String Asset string object on the ledger
		stub.putStringState(assetID, newAsset);
		// returning newly creating asset
		return asset;
	}
	@Transaction
	/*
	 * This function transfer the assets owned by producer to distributor  
	 * */
	public Assets transferAssetFromProdToDistributor(final Context ctx, final String assetID,final String transactionDate, final String distributor) {
		// get ledger context
		ChaincodeStub stub = ctx.getStub();
		// check if supplied assetID exists on the ledger
		String strAsset = stub.getStringState(assetID);
		// if asset does not exists with assetID, throw asset not found exception
		if(strAsset.isEmpty()) {
			String errorMsg = String.format("The Asset %s that you are trying to look, does not exists on ledger",assetID);
			throw new ChaincodeException(errorMsg, SupplyChainErrors.ASSET_NOT_FOUND.toString());
		}
		//if asset is found on the ledger, deserialize asset string object to Asset Object
		Assets assets = json.deserialize(strAsset, Assets.class);
		// Get details of the distrubutor stored on ledger
		String strDistributor = stub.getStringState(distributor);
		// if distributor object is empty, throw exception
		if(strDistributor.isEmpty()) {
			String errorMsg = String.format("The Distributor %s that you are trying to look, does not exists on ledger",distributor);
			throw new ChaincodeException(errorMsg, SupplyChainErrors.DISTRIBUTOR_NOT_FOUND.toString());
		}
		// if distributor is found on the ledger, get the distributor object from ledger
		Distributor distri = json.deserialize(strDistributor, Distributor.class);
		// create new Asset object by passing the new owner name as the distributor name from ledger
		Assets newAsset = new Assets(assets.getAssetID(),assets.getAssetName(),assets.getAssetDescription(),distri.getDistributorName(),assets.getHarvestDate());
		// Convert Asset object to String object
		String strNewProd = json.serialize(newAsset);
		// Strong new Asset Object with updating Owner name on the ldger
		stub.putStringState(assetID, strNewProd);
		// return updated asset object
		return newAsset;
	}
	@Transaction
	public Assets transferAssetFromDistToRetailer(final Context ctx, final String assetID,final String retailer,final String transactionDate) {
		// get the Ledger context
		ChaincodeStub stub = ctx.getStub();
		// check if the passed assetID is available on the ledger
		String strAsset = stub.getStringState(assetID);
		// if asset object is null then throw an exception
		if(strAsset.isEmpty()) {
			String errorMsg = String.format("The Asset %s that you are trying to look, does not exists on ledger");
			throw new ChaincodeException(errorMsg, SupplyChainErrors.ASSET_NOT_FOUND.toString());
		}
		// if asset exists on the ldger the the asset object from the ledger and convert it into Asset object
		Assets assets = json.deserialize(strAsset, Assets.class);
		// get retailer object from the ledger
		String strRetailer = stub.getStringState(retailer);
		// if retailer object is null from the ldger, then throw an exception
		if(strRetailer.isEmpty()) {
			String errorMsg = String.format("The retailer %s that you are trying to look, does not exists on ledger",retailer);
			throw new ChaincodeException(errorMsg, SupplyChainErrors.RETAILER_NOT_FOUND.toString());
		}
		// get retailer object from ledger and convert it into Retailer object
		Retailer oldretailer = json.deserialize(strRetailer, Retailer.class);
		// Create new asset object and change the asset ownership name to new owner i.e. retailer 
		Assets newAsset = new Assets(assets.getAssetID(),assets.getAssetName(),assets.getAssetDescription(),oldretailer.getRetailerName(),assets.getHarvestDate());
		// Conver new asset object into String object that can be sotred on ledger
		String strNewReta = json.serialize(newAsset);
		// Store new String object of Asset on the the ledger
		stub.putStringState(assetID, strNewReta);
		// return updated asset object 
		return newAsset;
	}
	@Transaction
	public Assets listAllAssets(final Context ctx,final String assetID) {
		// get the ledger context
		ChaincodeStub stub = ctx.getStub();
		// see if the supplied asset Id is present on the ledger
		String asset = stub.getStringState(assetID);
		// if asset object does not exists on ledger then throw an exception
		if(asset.isEmpty()) {
			String errorMsg = String.format("The Asset %s that you are trying to look, does not exists on ledger");
			throw new ChaincodeException(errorMsg, SupplyChainErrors.ASSET_NOT_FOUND.toString());
		}
		// if asset object is present on the ldger for given assetID, convert string asset object to Asset Object 
		Assets assets = json.deserialize(asset, Assets.class);
		// return asset object 
		return assets;
	}
	@Transaction
	public Assets findByAssetID(final Context ctx,final String assetID) {
		ChaincodeStub stub = ctx.getStub();
		String assets = stub.getStringState(assetID);
		if(assets.isEmpty()) {
			String errorMsg = String.format("The Asset %s that you are trying to look, does not exists on ledger");
			throw new ChaincodeException(errorMsg, SupplyChainErrors.ASSET_NOT_FOUND.toString());
		}
		Assets assetObject = json.deserialize(assets, Assets.class);
		return assetObject;
	}
}
