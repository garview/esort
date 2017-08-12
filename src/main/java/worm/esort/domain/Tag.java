package worm.esort.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class Tag {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	
	@Column(unique=true)
	private String tagName;
	
	@Version
	@Column(name="OPTLOCK" , columnDefinition = "integer DEFAULT 0", nullable = false)
	private Integer version = 0;
	
	@ManyToMany(mappedBy="tags")
	private Set<Book> books;
	
	@ManyToOne
	@JoinColumn(name="type_id")
	private TagType tagType;

	public TagType getTagType() {
		return tagType;
	}

	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
