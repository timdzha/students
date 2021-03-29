package com.timdev.students.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

	@Autowired
	private StudentRepository underTest;

	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}

	@Test
	void itShouldChecksIfStudentEmailExists() {
		// given
		final String email = "timdj01@mail.ru";
		Student student = new Student("Tim", "Dev", email, LocalDate.of(1999, 2, 12));
		underTest.save(student);

		//when
		final boolean expected = underTest.findStudentByEmail(email).isPresent();

		// then
		assertThat(expected).isTrue();
	}

	@Test
	void itShouldChecksIfStudentEmailDoesNotExist() {
		// given
		final String email = "timdj01@mail.ru";

		//when
		final boolean expected = underTest.findStudentByEmail(email).isPresent();

		// then
		assertThat(expected).isFalse();
	}
}