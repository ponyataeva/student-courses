package data.repositories;

import data.dto.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;

/**
 * Repository for work with UserInfo entity
 */
@RepositoryRestResource
public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, BigInteger> {

}
