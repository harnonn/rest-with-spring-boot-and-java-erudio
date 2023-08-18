package ca.com.arnon.integrationtests.vo.pagedModels;

import java.util.List;
import java.util.Objects;

import ca.com.arnon.integrationtests.vo.PersonVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PagedModelPerson")
public class PagedModelPerson {

	@XmlElement(name = "content")
	private List<PersonVO> content;

	public PagedModelPerson() {
	}

	public List<PersonVO> getContent() {
		return content;
	}

	public void setContent(List<PersonVO> content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagedModelPerson other = (PagedModelPerson) obj;
		return Objects.equals(content, other.content);
	}

}
