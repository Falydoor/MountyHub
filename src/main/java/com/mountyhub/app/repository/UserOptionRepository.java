package com.mountyhub.app.repository;

import com.mountyhub.app.domain.UserOption;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserOption entity.
 */
public interface UserOptionRepository extends JpaRepository<UserOption,Long> {

    @Query("select userOption from UserOption userOption where userOption.user.login = ?#{principal.username}")
    List<UserOption> findByUserIsCurrentUser();

}
