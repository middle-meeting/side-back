package com.iny.side.security.service;

import com.iny.side.users.domain.entity.Account;
import com.iny.side.users.domain.repository.UserRepository;
import com.iny.side.users.web.dto.AccountContext;
import com.iny.side.users.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRole().getRoleName()));

        AccountDto accountDto = AccountDto.from(account);

        return new AccountContext(accountDto, authorities);
    }
}
