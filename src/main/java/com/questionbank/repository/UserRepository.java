package com.questionbank.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.questionbank.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	@Query(value = "select pm.pk,pm.page_menu_name from user " + "inner join user_role_mapping role_map on role_map.user_pk = user.pk  "
			+ "inner join user_role ur on ur.pk = role_map.user_role_pk "
			+ "inner join page_menu_role_mapping pmrm on pmrm.user_role_pk = ur.pk "
			+ "inner join page_menu pm on pm.pk = pmrm.pk " + " where role_map.user_pk = :pk ", nativeQuery = true)
	List<Map<String, Object>> checkMenuLinks(Long pk);

	@Query(value = "select psm.pk,psm.page_sub_menu_name,psm.page_menu_pk from user " + "inner join user_role_mapping role_map on role_map.user_pk = user.pk "
			+ "inner join user_role ur on ur.pk = role_map.user_role_pk "
			+ "inner join page_menu_role_mapping pmrm on pmrm.user_role_pk = ur.pk "
			+ "inner join page_menu pm on pm.pk = pmrm.pk  "
			+ "inner join page_sub_menu_role_mapping psmrm on psmrm.user_role_pk = ur.pk  "
			+ "inner join page_sub_menu psm on psm.page_menu_pk = pm.pk " + "and psmrm.page_sub_menu_pk = pm.pk "
			+ " where role_map.user_pk = :pk ", nativeQuery = true)
	List<Map<String, Object>> checkSubMenuLinks(Long pk);
}
