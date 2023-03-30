package com.webflux.service;

import com.webflux.dto.StudentInput;
import com.webflux.dto.SubjectInput;
import com.webflux.entity.Address;
import com.webflux.entity.Student;
import com.webflux.entity.Subject;
import com.webflux.repository.AddressRepository;
import com.webflux.repository.StudentRepository;
import com.webflux.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StudentService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    public Mono<Student> addStudent(StudentInput studentInput) {
        // Create and save Address entity
        Address address = new Address();
        address.setStreet(studentInput.getAddress().getStreet());
        address.setCity(studentInput.getAddress().getCity());
        log.info("saving Student Address");
        Mono<Address> savedAddress = addressRepository.save(address);

        // Create and save Student entity
        Student student = new Student();
        student.setFirstName(studentInput.getFirstName());
        student.setLastName(studentInput.getLastName());
        student.setEmail(studentInput.getEmail());
        log.info("saving Student ");

        Mono<Student> savedStudent = savedAddress.flatMap(savedAddr -> {
            student.setAddress(savedAddr);
            return studentRepository.save(student);
        });

        // Create and save Subject entities
        List<Mono<Subject>> savedSubjects = new ArrayList<>();
        for (SubjectInput subjectInput : studentInput.getLearningSubjects()) {
            log.info("saving Student Subjects");
            savedSubjects.add(savedStudent.flatMap(savedStd -> {
                Subject subject = new Subject();
                subject.setSubjectName(subjectInput.getSubjectName());
                subject.setMarksObtained(subjectInput.getMarksObtained());
                subject.setStudent(savedStd);
                return subjectRepository.save(subject);
            }));
        }

//            Mono.when(savedSubjects).block();
        System.out.println("Student subject : " + student.getLearningSubjects());
        return savedStudent;
    }

    //This method is used to create List of Student along with its Resources.
   /* public Flux<Student> addStudents(List<StudentInput> studentInputs) {
        Flux<Student> addedStudents = new ArrayList<>();

        for (StudentInput studentInput : studentInputs) {
            // Creating and save Address entity
            Address address = new Address();
            address.setStreet(studentInput.getAddress().getStreet());
            address.setCity(studentInput.getAddress().getCity());
            addressRepository.save(address);

            // Creating and save Student entity
            Student student = new Student();
            student.setFirstName(studentInput.getFirstName());
            student.setLastName(studentInput.getLastName());
            student.setEmail(studentInput.getEmail());
            student.setAddress(address);
            studentRepository.save(student);

            // Creating and save Subject entities
            for (SubjectInput subjectInput : studentInput.getLearningSubjects()) {
                Subject subject = new Subject();
                subject.setSubjectName(subjectInput.getSubjectName());
                subject.setMarksObtained(subjectInput.getMarksObtained());
                subject.setStudent(student);
                subjectRepository.save(subject);
            }
            addedStudents.add(student);
        }
        return  addedStudents;
    }
*/
    public Flux<Student> addStudents(List<StudentInput> studentInputs) {
        return Flux.fromIterable(studentInputs)
                .flatMap(studentInput -> {
                    Address address = new Address();
                    address.setStreet(studentInput.getAddress().getStreet());
                    address.setCity(studentInput.getAddress().getCity());
                    Mono<Address> savedAddress = addressRepository.save(address);

                    Student student = new Student();
                    student.setFirstName(studentInput.getFirstName());
                    student.setLastName(studentInput.getLastName());
                    student.setEmail(studentInput.getEmail());

                    Mono<Student> savedStudent = savedAddress.flatMap(savedAddr -> {
                        student.setAddress(savedAddr);
                        return studentRepository.save(student);
                    });
                    return savedStudent;
                });

    }
}
