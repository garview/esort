package worm.esort.proxy;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProxyInfoRepository extends CrudRepository<ProxyInfo, Long>{

	ProxyInfo findProxyInfoByIpAndPort(String ip,String port);
	List<ProxyInfo> findByStatusLessThan(int status);
}
