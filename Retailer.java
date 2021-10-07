package SupplyChainManagement;

import java.util.Objects;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Retailer {
	@Property()
	private String retailerName;
	@Property()
	private String retailerAddress;
	@Property()
	private String transactionDate;
	@Property()
	private String Assets;
	
	public String getAssets() {
		return Assets;
	}
	
	public String getRetailerName() {
		return retailerName;
	}
	public String getRetailerAddress() {
		return retailerAddress;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public Retailer(@JsonProperty("retailerName") final String retailerName,
			@JsonProperty("retailerAddress") final String retailerAddress,
			@JsonProperty("transactionDate") final String transactionDate) {
		
		this.retailerName = retailerName;
		this.retailerAddress = retailerAddress;
		this.transactionDate = transactionDate;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
 		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
 		Retailer other = (Retailer) obj;
 
		return Objects.deepEquals(new String[] { getRetailerName(), getRetailerAddress(), getTransactionDate()},
				new String[] { other.getRetailerName(), other.getRetailerAddress(), other.getTransactionDate()});
	}
 
	@Override
	public int hashCode() {
		return Objects.hash(getRetailerName(), getRetailerAddress(), getTransactionDate());
	}
 
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [retailerName=" + retailerName + ", retailerAddress=" + retailerAddress
				+ ", transactionDate=" + transactionDate + "]";
	}
}
