package worm.esort.proxy;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProxyInfoRepository extends CrudRepository<Proxyinfo, Long>{

	Proxyinfo findProxyInfoByIpAndPort(String ip,String port);
	List<Proxyinfo> findByStatusLessThan(int status);
}
