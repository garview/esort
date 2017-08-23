package worm.esort.proxy;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="proxyinfo")
public class Proxyinfo {
	public static final int GOOD = 1;
	public static final int NORMAL = 2;
	public static final int BAD = 3;
	public static final int WORST = 4;
	public static final int DISABLED = 5;
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String ip;
	private String port;
	/**
	 * 连接方式如：http、https等
	 */
	private String connType;
	private String serverLocation ;
	/**
	 * 连接耗时，一般用百度首页测试
	 */
	private float connectSeconds;
	
	/**
	 * 上次连接耗时
	 */
	private float preConnectSeconds;
	public float getPreConnectSeconds() {
		return preConnectSeconds;
	}
	public void setPreConnectSeconds(float preConnectSeconds) {
		this.preConnectSeconds = preConnectSeconds;
	}
	/**
	 * 连接状况
	 */
	private Integer status;
	
	/**
	 * 连接测试时间
	 */
	private Timestamp checkTime;
	
	public Timestamp getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}
	public float getConnectSeconds() {
		return connectSeconds;
	}
	public void setConnectSeconds(float connectSeconds) {
		this.preConnectSeconds = this.connectSeconds;
		this.connectSeconds = connectSeconds;
		if(connectSeconds<1){
			this.status = GOOD;
		}else if(connectSeconds<2) {
			this.status = NORMAL;
		}else if(connectSeconds<4){
			this.status = BAD;
		}else {
			this.status = WORST;
		}
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getConnType() {
		return connType;
	}
	public void setConnType(String connType) {
		this.connType = connType;
	}
	public String getServerLocation() {
		return serverLocation;
	}
	public void setServerLocation(String serverLocation) {
		this.serverLocation = serverLocation;
	}
	public void setDisabled(){
		this.status = DISABLED;
	}
	@Override
	public String toString() {
		return "ProxyInfo [id=" + id + ", ip=" + ip + ", port=" + port + ", connType=" + connType + ", serverLocation="
				+ serverLocation + ", connectSeconds=" + connectSeconds + ", status=" + status + "]";
	}
	
}