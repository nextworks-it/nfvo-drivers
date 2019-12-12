package it.nextworks.nfvmano.nfvodriver.sol5.im;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import it.nextworks.openapi.msno.model.KeyValuePairs;

public class KeyValuePairsString extends KeyValuePairs {

	private List<KeyValuePair> kvs = new ArrayList<KeyValuePair>();
	
	public KeyValuePairsString() {	}
	
	public KeyValuePairsString(List<KeyValuePair> kvs) {
		if (kvs != null) this.kvs = kvs;
	}

	/**
	 * @return the kvs
	 */
	public List<KeyValuePair> getKvs() {
		return kvs;
	}

	/**
	 * @param kvs the kvs to set
	 */
	public void setKvs(List<KeyValuePair> kvs) {
		this.kvs = kvs;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		KeyValuePairsString keyValuePairs = (KeyValuePairsString) o;
		return Objects.equals(this.kvs, keyValuePairs.getKvs());
	}
	
	public void add(String k, String v) {
		this.kvs.add(new KeyValuePair(k, v));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class KeyValuePairsString {\n");

		for (KeyValuePair kv : kvs) {
			sb.append("    KVS: ").append("\n");
			sb.append("    Key: ").append(toIndentedString(kv.getKey())).append("\n");
			sb.append("    Key: ").append(toIndentedString(kv.getValue())).append("\n");
		}  
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	@Override
	public int hashCode() {
		return Objects.hash(kvs);
	}

}
