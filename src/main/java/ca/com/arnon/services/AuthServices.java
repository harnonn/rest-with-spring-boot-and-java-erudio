package ca.com.arnon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.com.arnon.data.vo.security.AccountCredentialsVO;
import ca.com.arnon.data.vo.security.TokenVO;
import ca.com.arnon.repositories.UserRepository;
import ca.com.arnon.security.jwt.JwtTokenProvider;

@Service
public class AuthServices {
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity signin(AccountCredentialsVO data) {
		try {
			var username = data.getUsername();
			var password = data.getPassword();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			var user = repository.findByUserName(username);
			var tokenResponse = new TokenVO();
			if (user != null) {
				tokenResponse = tokenProvider.createAcessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username "+ username +" not found!");
			}
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity refreshToken(String username, String refreshToken) {

		var user = repository.findByUserName(username);
		var tokenResponse = new TokenVO();
		if (user != null) {
			tokenResponse = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username "+ username +" not found!");
		}
		return ResponseEntity.ok(tokenResponse);
	}

}
