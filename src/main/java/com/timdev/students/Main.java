package com.timdev.students;

import com.timdev.students.student.Student;
import com.timdev.students.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
		return args -> {
			final List<Student> students = Stream.of(
					new Student(
							"Maria",
							"Jones",
							"maria.jones@gmail.com",
							LocalDate.of(2004, 01, 05)),
					new Student(
							"Robert",
							"Jock",
							"robert.jock@mail.ru",
							LocalDate.of(2000, 03, 07)),
					new Student(
							"Kate",
							"Lee",
							"kate.lee@ya.ru",
							LocalDate.of(1989, 06, 22))
					).collect(Collectors.toList());
			studentRepository.saveAll(students);
		};
	}
}
