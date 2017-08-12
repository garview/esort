package worm.esort.dto;

import java.sql.Date;

public class BookScore {
	
	private long bookId;

	private String name;

	private float score;

	private Integer favourited;

	private Integer ratingCount;

	private Float averageRating;
	private String category;

	private String href;
	private Date eInputDate;
	private Integer length;

	public BookScore() {
		super();
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BookScore(String name, float score) {
		super();
		this.name = name;
		this.score = score;
	}
//
//
	public BookScore(String name, Integer length, Integer ratingCount, Integer favourited, Float averageRating, Date eInputDate,String href, float score
			) {
		super();
		this.name = name;
		this.score = score;
		this.favourited = favourited;
		this.ratingCount = ratingCount;
		this.averageRating = averageRating;
		this.href = href;
		this.eInputDate = eInputDate;
		this.length = length;
	}

	public BookScore(String name, Integer length, float score) {
		super();
		this.name = name;
		this.score = score;
		this.length = length;
	}

	public Integer getFavourited() {
		return favourited;
	}

	public void setFavourited(Integer favourited) {
		this.favourited = favourited;
	}

	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Date geteInputDate() {
		return eInputDate;
	}

	public void seteInputDate(Date eInputDate) {
		this.eInputDate = eInputDate;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "BookScore [name=" + name + ", score=" + score + ", favourited=" + favourited + ", ratingCount="
				+ ratingCount + ", averageRating=" + averageRating + ", href=" + href + ", eInputDate=" + eInputDate
				+ ", length=" + length + "]";
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
