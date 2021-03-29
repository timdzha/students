package com.timdev.students.auth;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserDao {

	Optional<ApplicationUser> findApplicationUserByUsername(String username);
}

