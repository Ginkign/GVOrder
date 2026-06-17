package com.example.userservice.service;

import com.example.userservice.dto.AddressRequest;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.User;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public User register(RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        String hashed = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());
        u.setPassword(hashed);
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
        return userRepository.save(u);
    }

    public Optional<User> login(LoginRequest req) {
        Optional<User> ou = userRepository.findByUsername(req.getUsername());
        if (ou.isEmpty()) return Optional.empty();
        User u = ou.get();
        if (BCrypt.checkpw(req.getPassword(), u.getPassword())) {
            return Optional.of(u);
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Address addAddress(Long userId, AddressRequest req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("用户未找到"));
        Address a = new Address();
        a.setUser(user);
        a.setReceiver(req.getReceiver());
        a.setPhone(req.getPhone());
        a.setAddressLine(req.getAddressLine());
        a.setCity(req.getCity());
        a.setPostalCode(req.getPostalCode());
        a.setDefault(req.isDefault());
        return addressRepository.save(a);
    }

    public List<Address> listAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
