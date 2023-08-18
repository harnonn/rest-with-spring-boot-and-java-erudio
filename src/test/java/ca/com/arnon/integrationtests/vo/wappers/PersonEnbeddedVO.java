package ca.com.arnon.integrationtests.vo.wappers;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.com.arnon.integrationtests.vo.PersonVO;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PersonEnbeddedVO")
public class PersonEnbeddedVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("personVOList")
	private List<PersonVO> persons;

	public PersonEnbeddedVO() {

	}

	public List<PersonVO> getPersons() {
		return persons;
	}

	public void setPersons(List<PersonVO> persons) {
		this.persons = persons;
	}

	@Override
	public int hashCode() {
		return Objects.hash(persons);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonEnbeddedVO other = (PersonEnbeddedVO) obj;
		return Objects.equals(persons, other.persons);
	}

}
