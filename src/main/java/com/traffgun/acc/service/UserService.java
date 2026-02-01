package com.traffgun.acc.service;

import com.traffgun.acc.dto.user.ChangePasswordRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.History;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.PasswordsDoNotMatchException;
import com.traffgun.acc.exception.UserNotFoundException;
import com.traffgun.acc.model.Role;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.model.history.UserCreatedHistoryBody;
import com.traffgun.acc.model.history.UserPasswordChangedHistoryBody;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.HistoryRepository;
import com.traffgun.acc.repository.UserRepository;
import com.traffgun.acc.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final HistoryRepository historyRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getCurrentUser() throws IllegalAccessException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
        }
        throw new IllegalAccessException("User is not authenticated");
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        if (saved.getRole() != Role.ADMIN) {
            Employee employee = Employee.builder().name(saved.getUsername()).rating(0D).user(saved).build();
            employeeRepository.save(employee);
        }

        historyRepository.save(History.builder()
                .user(saved)
                .type(HistoryType.USER)
                .body(new UserCreatedHistoryBody(saved.getUsername()))
                .build()
        );

        return saved;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USERNAME_NOT_FOUND"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(String username, Role role, String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(UserSpecification.hasUsernameLike(username))
                .and(UserSpecification.hasRole(role));

        return userRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) throws IllegalAccessException {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        historyRepository.save(History.builder()
                .user(getCurrentUser())
                .type(HistoryType.USER)
                .body(new UserPasswordChangedHistoryBody(user.getUsername()))
                .build()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        historyRepository.deleteByUser_Id(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void makeAdmin(User user) {
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        employeeRepository.deleteByUser(user);
    }
}
