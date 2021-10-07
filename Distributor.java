package SupplyChainManagement;

import java.util.Objects;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Distributor {
	@Property()
	private String distributorName;
	@Property()
	private String distributorAddress;
	@Property()
	private String transactionDate;
	@Property()
	private String Assets;
	
	public String getAssets() {
		return Assets;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public String getDistributorAddress() {
		return distributorAddress;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public Distributor(@JsonProperty("distributorName") final String distributorName,
			@JsonProperty("distributorAddress") final String distributorAddress,
			@JsonProperty("transactionDate") final String transactionDate) {
		
		this.distributorName = distributorName;
		this.distributorAddress = distributorAddress;
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
 		Distributor other = (Distributor) obj;
 
		return Objects.deepEquals(new String[] { getDistributorName(), getDistributorAddress(), getTransactionDate()},
				new String[] { other.getDistributorName(), other.getDistributorAddress(), other.getTransactionDate()});
	}
 
	@Override
	public int hashCode() {
		return Objects.hash(getDistributorName(), getDistributorAddress(), getTransactionDate());
	}
 
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [distributorName=" + distributorName + ", distributorAddress=" + distributorAddress
				+ ", transactionDate=" + transactionDate + "]";
	}
}