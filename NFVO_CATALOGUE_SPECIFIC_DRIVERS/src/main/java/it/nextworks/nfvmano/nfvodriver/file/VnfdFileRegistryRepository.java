package it.nextworks.nfvmano.nfvodriver.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VnfdFileRegistryRepository extends JpaRepository<VnfdFileRegistry, Long> {

    List<VnfdFileRegistry> findAllByVnfdId(String vnfdId);
    Optional<VnfdFileRegistry> findByVnfdIdAndVnfdVersion(String vnfdId, String vnfdVersion);
    Optional<VnfdFileRegistry> findByVnfdInfoId(String nsdInfoId);
    Optional<VnfdFileRegistry> findByPackageNameAndPackageProviderAndPackageVersion(String name, String provider, String version);


}
