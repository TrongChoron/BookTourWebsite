package nhom04.hcmute.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhom04.hcmute.exception.AppException;
import nhom04.hcmute.exception.NotFoundException;
import nhom04.hcmute.model.Role;
import nhom04.hcmute.model.User;
import nhom04.hcmute.repository.RoleRepository;
import nhom04.hcmute.repository.UserRepository;
import nhom04.hcmute.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Create by: IntelliJ IDEA
 * User     : trongnt
 * Date     : Sun, 9/11/2022
 * Time     : 12:09
 * Filename : UserServiceImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User saveUser(User user) {
        log.info("Saving User");
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        User updateUser = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException(String.format("User with id %s not found",id)));
        updateUser.setFullName(user.getFullName());
        updateUser.setAddress(user.getAddress());
        updateUser.setGender(user.getGender());
        Date date = new Date();
        updateUser.setModifiedAt(date);
        return userRepository.save(updateUser);
    }

    @Override
    public Role saveRole(Role role) {
        Role roleExist = roleRepository.findByName(role.getRoleName());
        if(roleExist!=null){
            throw new AppException(String.format("Role with RoleName %s has existed",role.getRoleName()));
        }
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, Role role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException(String.format("User with email %s not found!",email)));
        user.getRoles().add(role);
        log.info("Add role {} into user {}",role.getRoleName(),user.getFullName());
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException(String.format("User with email %s not found!",email)));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Boolean existedByEmail(String email) {
        log.info("Check user existed with email {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteUser(String email) {
        User userDelete = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException(String.format("User with username %s not found",email
                )));
        log.info("Deleting user with email {}",email);
        userRepository.delete(userDelete);
    }

    @Override
    public Boolean checkPassword(String email, String password) {
        User users = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException(String.format("User with username %s not found",email)));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String comparePassword = users.getPassword();
        return passwordEncoder.matches(password, comparePassword);
    }

    @Override
    public Boolean changePassword(String password, String email) {
        User userChange = userRepository.findByEmail(email).orElse(null);
        if(userChange == null){
            log.error("User not found!");
            return false;
        }else {
            userChange.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
            userRepository.save(userChange);
        }
        return true;
    }

    @Override
    public Boolean forgotPassword(String email) {
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        log.info("Get all roles!");
        return roleRepository.findAll();
    }
}
