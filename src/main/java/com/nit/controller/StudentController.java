package com.nit.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.nit.entity.Student;
import com.nit.repository.StudentRepository;

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

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;




@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentRepository repo;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(
            @RequestParam("image") MultipartFile image,
            @RequestParam("resume") MultipartFile resume,
            @ModelAttribute Student student) throws IOException {

        // Upload image
        Map imageUpload = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.emptyMap()
        );

        // Get original file extension
        String originalName = resume.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));

        // Create temp file with correct extension
        File tempFile = File.createTempFile("resume-", extension);
        resume.transferTo(tempFile);

        // Upload as raw (works for PDF, Excel, DOC, etc.)
        Map resumeUpload = cloudinary.uploader().upload(
                tempFile,
                ObjectUtils.asMap(
                        "resource_type", "raw"
                )
        );

        tempFile.delete();

        String imageUrl = imageUpload.get("secure_url").toString();
        String resumeUrl = resumeUpload.get("secure_url").toString();

        student.setImagePath(imageUrl);
        student.setResumePath(resumeUrl);

        repo.save(student);

        return ResponseEntity.ok("Student Saved Successfully!");
    }
    
    
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(repo.findAll());
    }
}
