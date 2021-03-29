package com.timdev.students.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.Period;

@Entity(name = "Student")
@Table(
		name = "student",
		uniqueConstraints = {
				@UniqueConstraint(name = "student_email_unique", columnNames = "email")
		}
)
public class Student {

	@Id
	@SequenceGenerator(
			name = "student_sequence",
			sequenceName = "student_sequence",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "student_sequence"
	)
	@Column(
			name = "id",
			updatable = false
	)
	private Long id;

	@Column(
			name = "first_name",
			nullable = false,
			columnDefinition = "TEXT"
	)
	private String firstName;

	@Column(
			name = "last_name",
			nullable = false,
			columnDefinition = "TEXT"
	)
	private String lastName;

	@Column(
			name = "email",
			nullable = false,
			columnDefinition = "TEXT"
	)
	private String email;

	@Column(
			name = "date_of_birth",
			nullable = false
	)
	private LocalDate dateOfBirth;

	@Transient //calculate it using dateOfBirth
	private Integer age;

	public Student() {
	}

	public Student(Integer id, String firstName) {
		this(Long.valueOf(id), firstName, null, null, null);
	}

	public Student(String firstName, String lastName, String email, LocalDate dateOfBirth) {
		this(null, firstName, lastName, email, dateOfBirth);
	}

	public Student(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getAge() {
		return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", dateOfBirth=" + dateOfBirth +
				", age=" + age +
				'}';
	}
}
