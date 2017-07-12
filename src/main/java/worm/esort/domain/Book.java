package worm.esort.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class Book {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	/**
	 * e站入库日期
	 */
	private Date eInputDate;
	/**
	 * e站入库时间
	 */
	private Date eInputTime;
	
	/**
	 * book所有图片总大小
	 */
	private String fileSize;
	/**
	 * 页数
	 */
	private Integer length;
	/**
	 * 番号
	 */
	private String name;
	/**
	 * 添加收藏的人数
	 */
	private Integer favourited;
	/**
	 * 评分人数
	 */
	private Integer ratingCount;
	public Integer getRatingCount() {
		return ratingCount;
	}
	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}
	/**
	 * 平均评分
	 */
	private Float averageRating;
	/**
	 * 链接地址
	 */
	private String href;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date geteInputDate() {
		return eInputDate;
	}
	public void seteInputDate(Date eInputDate) {
		this.eInputDate = eInputDate;
	}
	public Date geteInputTime() {
		return eInputTime;
	}
	public void seteInputTime(Date eInputTime) {
		this.eInputTime = eInputTime;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFavourited() {
		return favourited;
	}
	public void setFavourited(Integer favourited) {
		this.favourited = favourited;
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
	@Override
	public String toString() {
		return "Book [id=" + id + ", eInputDate=" + eInputDate + ", eInputTime=" + eInputTime + ", fileSize=" + fileSize
				+ ", length=" + length + ", name=" + name + ", favourited=" + favourited + ", ratingCount="
				+ ratingCount + ", averageRating=" + averageRating + ", href=" + href + "]";
	}
	
	
	
}
