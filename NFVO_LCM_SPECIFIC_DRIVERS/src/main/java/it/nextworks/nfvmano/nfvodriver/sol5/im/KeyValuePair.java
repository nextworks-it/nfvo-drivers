package it.nextworks.nfvmano.nfvodriver.sol5.im;

import java.util.Objects;

public class KeyValuePair {

	private String key;
	private String value;
	
	public KeyValuePair() {	}
	
	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		KeyValuePair keyValuePair = (KeyValuePair) o;
		return Objects.equals(this.key, keyValuePair.getKey()) &&
				Objects.equals(this.value, keyValuePair.getValue());
	}

}
