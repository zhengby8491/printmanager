package com.huayin.printmanager.persist.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.huayin.printmanager.persist.entity.sys.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User>
{

	@Query(value="select * from sys_user a where a.userName=?1",nativeQuery=true)
	public List<User> getByUserNameForNative(String userName);

	@Query("from User a where a.userName=?1")
	public User getByUserName(String userName);
	
	@Query("from User a where a.companyId=?1")
	public List<User> findByCompanyId(String companyId);
	
	@Query("from User a ")
	public Page<User> findByPage(Pageable pageable);

	@Modifying
	@Query("update User a set a.realName = ?1 where a.id=?2")
	public int updateRealName(String realName, Long id);
	
}
