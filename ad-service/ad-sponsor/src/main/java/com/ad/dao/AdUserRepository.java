package com.ad.dao;

import com.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface AdUserRepository extends JpaRepository<AdUser,Long> {

    AdUser findByUsername(String username);
}
