package ca.com.arnon.integrationtests.vo.wappers;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WrapperPersonVO")
public class WrapperPersonVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("_embedded")
	private PersonEnbeddedVO embedded;

	public WrapperPersonVO() {
	}

	public PersonEnbeddedVO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(PersonEnbeddedVO embedded) {
		this.embedded = embedded;
	}

	@Override
	public int hashCode() {
		return Objects.hash(embedded);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapperPersonVO other = (WrapperPersonVO) obj;
		return Objects.equals(embedded, other.embedded);
	}

}
