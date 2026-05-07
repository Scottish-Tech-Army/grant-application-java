package org.tfg.grant_java.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.tfg.grant_java.domain.Organisation;
import org.tfg.grant_java.domain.AppUser;
import org.tfg.grant_java.domain.enums.Role;
import org.tfg.grant_java.domain.CommonField;
import org.tfg.grant_java.domain.CommonFieldVersionSet;
import org.tfg.grant_java.repo.OrganisationRepository;
import org.tfg.grant_java.repo.AppUserRepository;
import org.tfg.grant_java.repo.CommonFieldRepository;
import org.tfg.grant_java.domain.CommonFieldVersion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private CommonFieldRepository commonFieldRepository;

    @Override
    public void run(String... args) {
        // 1. Organisation
        Organisation org = organisationRepository.findAll().stream().findFirst().orElse(null);
        if (org == null) {
            org = new Organisation();
            // Set name if setter exists, else use constructor if available
            try {
                org.getClass().getMethod("setName", String.class).invoke(org, "TechForGood Demo Org");
            } catch (Exception e) {
                // fallback: do nothing
            }
            org = organisationRepository.save(org);
        }

        // 2. Admin AppUser
        Optional<AppUser> adminOpt = appUserRepository.findByOrganisationIdAndEmail(org.getId(), "admin@demo.org");
        AppUser admin;
        if (adminOpt.isEmpty()) {
            admin = new AppUser();
            admin.setOrganisation(org);
            admin.setEmail("admin@demo.org");
            admin.setDisplayName("Demo Admin");
            Set<Role> roles = admin.getRoles();
            roles.add(Role.ADMIN);
            admin = appUserRepository.save(admin);
        } else {
            admin = adminOpt.get();
        }

        // 3. CommonFields
        List<CommonField> existingFields = commonFieldRepository.findByOrganisationIdOrderByDisplayOrderAsc(org.getId());
        if (existingFields == null || existingFields.isEmpty()) {
            for (int i = 1; i <= 12; i++) {
                CommonField field = new CommonField();
                field.setOrganisation(org);
                field.setKey("field_" + i);
                field.setLabel("Field Label " + i);
                field.setHelpText("Seeded help text for field " + i);
                field.setCurrentVersion(1);
                field.setActive(true);
                field.setDisplayOrder(i);
                // DataType default is TEXT

                CommonFieldVersionSet versionSet = new CommonFieldVersionSet();
                versionSet.setCurrentVersion(1);

                CommonFieldVersion version = new CommonFieldVersion();
                version.setV(1);
                version.setValueText("");
                version.setCreatedAt(Instant.now());
                version.setCreatedBy(admin.getId());
                version.setChangeNote("Seeded");

                versionSet.getVersions().add(version);
                field.setVersionSet(versionSet);

                commonFieldRepository.save(field);
            }
        }
    }
}
