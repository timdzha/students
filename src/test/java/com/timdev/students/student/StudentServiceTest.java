package com.timdev.students.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository; // studentRepository has already tested and we just verify this

	private StudentService underTest;

	@BeforeEach
	void setUp() {
		underTest = new StudentService(studentRepository);
	}

	@Test
	void itShouldGetAllStudents() {
		// when
		underTest.getStudents();
		// then
		verify(studentRepository).findAll();
	}

	@Test
	void itShouldGetStudent() {
		// given
		final Long id = 1L;
		Student student = new Student(id, "Tim", "Dev", "timdj01@mail.ru", LocalDate.of(1999, 2, 12));
		given(studentRepository.findById(id)).willReturn(Optional.of(student));
		// when
		Student studentFound = underTest.getStudent(id.intValue());
		// then
		assertThat(studentFound).isEqualTo(student);
		assertStudentFields(student, studentFound);
	}

	@Test
	void itShouldThrowIfStudentNotFound() {
		// when
		final int studentId = 1;
		// then
		assertThatThrownBy(() -> underTest.getStudent(1))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining(String.format("Student %s not found", studentId));
	}

	@Test
	void itShouldCreateStudent() {
		// given
		Student student = new Student("Tim", "Dev", "timdj01@mail.ru", LocalDate.of(1999, 2, 12));
		// when
		underTest.createStudent(student);
		// then
		ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
		verify(studentRepository).save(studentArgumentCaptor.capture());
		final Student capturedStudent = studentArgumentCaptor.getValue();
		assertThat(capturedStudent).isEqualTo(student);
	}

	@Test
	void itShouldThrowWhenEmailIsTaken() {
		// given
		Student student = new Student("Tim", "Dev", "timdj01@mail.ru", LocalDate.of(1999, 2, 12));
		given(studentRepository.findStudentByEmail(student.getEmail()))
				.willReturn(Optional.of(student));
		// when
		// then
		assertThatThrownBy(() -> underTest.createStudent(student))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Email taken");
		// verify that repository will never save anything because exception
		verify(studentRepository, never()).save(any());
	}

	@Test
	void itShouldDeleteStudent() {
		// given
		final Long id = 1L;
		given(studentRepository.existsById(id)).willReturn(true);
		// when
		underTest.deleteStudent(id);
		// then
		ArgumentCaptor<Long> studentArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		verify(studentRepository).deleteById(studentArgumentCaptor.capture());
	}

	@Test
	void itShouldThrowIfStudentNotFoundToDelete() {
		// given
		final Long id = 1L;
		// when
		// then
		assertThatThrownBy(() -> underTest.deleteStudent(id))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining(String.format("Student with id %s doesn't exist", id));
		verify(studentRepository, never()).deleteById(id);
	}

	@Test
	void itShouldUpdateStudent() {
		// given
		final Long id = 1L;
		final String email = "updated@mail.ru";
		final String name = "TimUpdated";
		Student student = new Student(id, "Tim", "Dev", "timdj01@mail.ru", LocalDate.of(1999, 2, 12));
		given(studentRepository.findById(id)).willReturn(Optional.of(student));
		// when
		underTest.updateStudent(id, name, email);
		// then
		verify(studentRepository).findById(id);
		verify(studentRepository).findStudentByEmail(email);
		ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
		verify(studentRepository).save(studentArgumentCaptor.capture());
		final Student captured = studentArgumentCaptor.getValue();
		assertStudentFields(student, captured);
	}

	@Test
	void itShouldNotUpdateStudentIfEmailIsTaken() {
		// given
		final Long id = 1L;
		final String email = "updatedTimdj01@mail.ru";
		final String name = "TimUpdated";
		Student student = new Student(id, name, "Dev", "timdj01@mail.ru", LocalDate.of(1999, 2, 12));
		given(studentRepository.findById(id)).willReturn(Optional.of(student));
		given(studentRepository.findStudentByEmail(email)).willReturn(Optional.of(student));
		// when
		// then
		assertThatThrownBy(() -> underTest.updateStudent(id, name, email))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Email taken");
		verify(studentRepository, never()).save(student);
	}

	private void assertStudentFields(Student student, Student studentFound) {
		assertThat(studentFound.getEmail()).isEqualTo(student.getEmail());
		assertThat(studentFound.getFirstName()).isEqualTo(student.getFirstName());
		assertThat(studentFound.getLastName()).isEqualTo(student.getLastName());
		assertThat(studentFound.getDateOfBirth()).isEqualTo(student.getDateOfBirth());
		assertThat(studentFound.getAge()).isEqualTo(student.getAge());
		assertThat(studentFound.getId()).isEqualTo(student.getId());
	}

}