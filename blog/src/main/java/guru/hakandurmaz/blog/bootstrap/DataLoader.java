package guru.hakandurmaz.blog.bootstrap;

import guru.hakandurmaz.blog.entity.Role;
import guru.hakandurmaz.blog.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
     //Role adminRole = new Role();
     //adminRole.setName("ROLE_ADMIN");
     //roleRepository.save(adminRole);
     //Role userRole = new Role();
     //userRole.setName("ROLE_USER");
     //roleRepository.save(userRole);
    }
}
