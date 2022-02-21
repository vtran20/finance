package com.easysoft.finance.configuration;

import com.easysoft.finance.configuration.exception.ResourceNotFoundException;
import com.easysoft.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;

    @Override
    @Cacheable(value = "userByUsername", key = "#userName")
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<com.easysoft.finance.domain.User> user = userRepository.findByUsername(userName);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Not Found", "Incorrect username or password", null, null);
        } else {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.get().getRoles()));
            return new User(user.get().getUsername(), user.get().getPassword(), user.get().isActive(user.get().getActive()), true, true, true, authorities);
        }
    }

    @CacheEvict(value = "userByUsername", key = "#cacheKey")
    public void evictSingleCacheValue(String cacheKey) {
    }
}
