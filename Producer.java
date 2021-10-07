package SupplyChainManagement;

import java.util.Objects;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Producer {
	@Property()
	private String producerName;
	@Property()
	private String producerAddress;
	@Property()
	private Assets assets;

	public String getProducerName() {
		return producerName;
	}
	public String getProducerAddress() {
		return producerAddress;
	}
	public Assets getAssets() {
		return assets;
	}
	
	public Producer(@JsonProperty("producerName") final String producerName,
			@JsonProperty("producerAddress") final String producerAddress,
			@JsonProperty("Assets") final Assets assets) {
		
		this.producerName = producerName;
		this.producerAddress = producerAddress;
		this.assets = assets;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
 		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
 		Producer other = (Producer) obj;
 
		return Objects.deepEquals(new String[] { getProducerName(), getProducerAddress()},
				new String[] { other.getProducerName(), other.getProducerAddress()});
	}
 
	@Override
	public int hashCode() {
		return Objects.hash(getProducerName(), getProducerAddress(), getAssets());
	}
 
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [producerName=" + producerName + ", producerAddress=" + producerAddress
				+ "]";
	}
}