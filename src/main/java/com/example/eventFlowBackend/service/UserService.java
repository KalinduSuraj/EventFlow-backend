package com.example.eventFlowBackend.service;

import com.example.eventFlowBackend.entity.Role;
import com.example.eventFlowBackend.entity.User;
import com.example.eventFlowBackend.payload.BatchDTO;
import com.example.eventFlowBackend.payload.UserDTO;
import com.example.eventFlowBackend.repository.StudentBatchRepository;
import com.example.eventFlowBackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StudentBatchRepository studentBatchRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, StudentBatchRepository studentBatchRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentBatchRepository = studentBatchRepository;
    }

    public User create(UserDTO request) {
        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setMobile(request.getMobile());
            user.setNic(request.getNic());
            user.setCreatedBy(userRepository.findById(Long.valueOf(request.getCreatedBy())).orElseThrow(() -> new RuntimeException("User not found")));
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.valueOf(request.getRole().name()));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("User not created");
        }

    }

    public void update(Long id, User updatedUser) {
        userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setMobile(updatedUser.getMobile());
            user.setNic(updatedUser.getNic());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updatePassword(Long id, User request) {
        userRepository.findById(id).map(user -> {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Long id) {
        userRepository.findById(id).map(user -> {
            user.setIsActive(false);
            return userRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("User not found"));
    }

    public List<UserDTO> findByIsActiveTrue() {
       List<UserDTO> users = new ArrayList<>();
         userRepository.findByIsActiveTrue().forEach(user -> {
              UserDTO userDTO = new UserDTO();
              userDTO.setUID(user.getUID());
              userDTO.setName(user.getName());
              userDTO.setEmail(user.getEmail());
              userDTO.setMobile(user.getMobile());
              userDTO.setNic(user.getNic());
              userDTO.setRole(user.getRole());
              userDTO.setCreatedBy(user.getCreatedBy().getUID());
              users.add(userDTO);
         });
            return users;
    }

    public List<BatchDTO> findBatchesByUser(Long uID) {
        List<BatchDTO> batches = new ArrayList<>();
        studentBatchRepository.findByUser_uID(uID).forEach(studentBatch -> {
            BatchDTO batchDTO = new BatchDTO();
            batchDTO.setBID(studentBatch.getBatch().getBID());
            batchDTO.setBatchName(studentBatch.getBatch().getBatchName());
            batchDTO.setCommonEmail(studentBatch.getBatch().getCommonEmail());
            batches.add(batchDTO);
        });

        return batches;
    }

    public Optional<User> findByID(Long id) {
        return userRepository.findById(id);
    }

    public List<UserDTO> findByNicStartingWith(String nic) {
        List<UserDTO> users = new ArrayList<>();
        userRepository.findByNicStartingWithAndIsActiveTrue(nic).forEach(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUID(user.getUID());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setMobile(user.getMobile());
            userDTO.setNic(user.getNic());
            userDTO.setRole(user.getRole());
            userDTO.setCreatedBy(user.getCreatedBy().getUID());
            users.add(userDTO);
        });
        return users;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }


}
