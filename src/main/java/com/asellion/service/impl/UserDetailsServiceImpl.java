package com.asellion.service.impl;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.asellion.model.LoginUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		final List<LoginUser> users = Arrays.asList(new LoginUser(1l, "user1", pwdEncoder.encode("1234")),
				new LoginUser(2l, "user2", pwdEncoder.encode("1234")));

		for (LoginUser appUser : users) {
			if (appUser.getUsername().equals(username)) {
				return new User(appUser.getUsername(), appUser.getPassword(), emptyList());
			}
		}

		throw new UsernameNotFoundException("Username: " + username + " not found");
	}

}
