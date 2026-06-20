package com.example.BankProject.Security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.BankProject.Entity.User;
//
//public class CustomUserDetails implements UserDetails{
//	
//	private long id;
//	private String email;
//	private String password;
//	private String branchCode;
//	private boolean active;
//	private User user;
//	private Collection<? extends GrantedAuthority> authorities;
//	
//	//Constructor
//	public CustomUserDetails() {
//		
//	}
//	
//	public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
//		this.user = user;
//		this.authorities = authorities;
//	}
//	
////	public CustomUserDetails(long id, String email, String password,boolean active, String branchCode,
////			Collection<? extends GrantedAuthority> authorities) {
////		super();
//////		this.user = user;
////		this.id = id;
////		this.email = email;
////		this.password = password;
////		this.branchCode = branchCode;
////		this.authorities = authorities;
////		this.active=active;
////	}
//
//	public long getId() {
//		return id;
//	}
//
//	public String getBranchCode() {
//		return branchCode;
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getEmail();
//	}
//
//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return authorities;
//	}
//	
//	public User getUser() {
//		return user;
//	}
//
//
//	@Override public boolean isAccountNonExpired() {return true; }
//	@Override public boolean isAccountNonLocked() {return true; }
//	@Override public boolean isCredentialsNonExpired() {return true; }
////	@Override public boolean isEnabled() {return true;}
//	
//	
//
//	
//	
//	
//	
//
//}



public class CustomUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user,
                             Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public Long getId() {
        return user.getId();
    }

    public String getBranchCode() {
        return user.getBranchCode();   // ✅ FIXED
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return user.isActive(); }
}
