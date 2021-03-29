package com.timdev.students.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

	public Student getStudent(Integer studentId) {
		return tryToFindStudent(studentId);
	}

	public void createStudent(Student student) {
		tryToCheckIfStudentAlreadyExists(student.getEmail());
		studentRepository.save(student);
	}

	public void deleteStudent(Long studentId) {
		final boolean exists = studentRepository.existsById(studentId);
		if (!exists) {
			throw new IllegalStateException(String.format("Student with id %s doesn't exist", studentId));
		}
		studentRepository.deleteById(studentId);
	}

	@Transactional
	public void updateStudent(Long studentId, String name, String email) {
		final Student student = tryToFindStudent(studentId.intValue());

		if (name != null
				&& name.length() > 0
				&& !Objects.equals(student.getFirstName(), name)) {
			student.setFirstName(name);
		}

		if (email != null
				&& email.length() > 0
				&& !Objects.equals(student.getEmail(), email)) {
			tryToCheckIfStudentAlreadyExists(email);
			student.setEmail(email);
		}
		studentRepository.save(student);
	}


	private Student tryToFindStudent(Integer studentId) {
		return studentRepository
				.findById(Long.valueOf(studentId))
				.orElseThrow(() -> new IllegalStateException(String.format("Student %s not found", studentId)));
	}

	private void tryToCheckIfStudentAlreadyExists(String email) {
		final Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
		if (studentOptional.isPresent()) {
			throw new IllegalStateException("Email taken");
		}
	}

}
