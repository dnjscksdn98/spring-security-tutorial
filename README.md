## Spring-Security Tutorial

### 1) Form Based Authentication
- 디폴트 아이디: user
- 디폴트 비밀번호: Spring-Security에서 자동 생성해준 비밀번호
- 로그아웃
  - localhost:8181/logout
  
### 2) Basic Authentication
- 모든 요청의 헤더에 Basic64로 인코딩된 username:password를 포함시키면서 서버에 요청을 보냄
- 서버는 username이 존재하고 password가 일치하면 200 응답,  
그렇지 않으면 401 응답을 보냄
- 로그아웃 불가능
  - 모든 요청에 username과 password가 전송되기 때문에

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
}
```

### 3) Ant Matchers
- 특정 URL을 권한 없이 허용

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
}
```

### 4) Password Encoder
- BCryptPasswordEncoder()

```
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);  // strength
    }
}
```

### 5) Roles & Permissions
- Roles
  - Admin, Customer, Restaurant Owner
 
- Permissions
  - Restaurant:write
  - Restaurant:delete
  - User:write
  - User:delete
  
- 하나의 사용자에게 하나 이상의 Role 부여 가능

- antMatchers()로 특정 Role이 필요한 경로 지정
- hasRole()로 필요한 Role 지정

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*")
            .permitAll()
            .antMatchers("/api/**")
            .hasRole(STUDENT.name())
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
}
```

### 6) Permissions Based Authentication

- Admin: read, write
- Admintrainee: read

**1) antMatchers() 방식**
```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
            .antMatchers("/api/**").hasRole(STUDENT.name())
            .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
            .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
            .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
            .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
}
```

**2) 어노테이션 방식**
```
/**
 * hasRole("ROLE_")
 * hasAnyRole("ROLE_")
 * hasAuthority("permission")
 * hasAnyAuthority("permission")
 *
 */

@GetMapping
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
public List<Student> getAllStudents() {
    System.out.print("Get all students: ");
    return STUDENTS;
}

@PostMapping
@PreAuthorize("hasAuthority('student:write')")
public void registerNewStudent(@RequestBody Student student) {
    System.out.print("Register new Student: ");
    System.out.println(student);
}
```

```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
```