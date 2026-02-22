package com.nit.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nit.entity.Student;
import com.nit.repository.StudentRepository;

@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentRepository repo;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(
            @RequestParam("image") MultipartFile image,
            @RequestParam("resume") MultipartFile resume,
            @ModelAttribute Student student) throws IOException {

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String imagePath = "uploads/" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
        String resumePath = "uploads/" + System.currentTimeMillis() + "_" + resume.getOriginalFilename();

        image.transferTo(new File(UPLOAD_DIR + imagePath.substring(8)));
        resume.transferTo(new File(UPLOAD_DIR + resumePath.substring(8)));

        student.setImagePath(imagePath);
        student.setResumePath(resumePath);

        repo.save(student);

        return ResponseEntity.ok("Student Saved Successfully!");
    }
    
    
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(repo.findAll());
    }
}
