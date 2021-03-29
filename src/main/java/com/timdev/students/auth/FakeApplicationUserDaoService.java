package com.timdev.students.auth;

import com.google.common.collect.Lists;
import com.timdev.students.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<ApplicationUser> findApplicationUserByUsername(String username) {
		return getApplicationUsers()
				.stream()
				.filter(applicationUser -> username.equals(applicationUser.getUsername()))
				.findFirst();
	}

	private List<ApplicationUser> getApplicationUsers() {
		return Lists.newArrayList(
				new ApplicationUser(
						"student1",
						passwordEncoder.encode("student1"),
						ApplicationUserRole.STUDENT.getGrantedAuthorities(),
						true,
						true,
						true,
						true
				),
				new ApplicationUser(
						"admin1",
						passwordEncoder.encode("admin1"),
						ApplicationUserRole.ADMIN.getGrantedAuthorities(),
						true,
						true,
						true,
						true
				),
				new ApplicationUser(
						"trainee1",
						passwordEncoder.encode("trainee1"),
						ApplicationUserRole.TRAINEE.getGrantedAuthorities(),
						true,
						true,
						true,
						true
				)
		);
	}
}
