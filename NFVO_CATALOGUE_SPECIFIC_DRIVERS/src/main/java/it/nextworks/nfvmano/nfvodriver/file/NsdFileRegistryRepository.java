package it.nextworks.nfvmano.nfvodriver.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NsdFileRegistryRepository extends JpaRepository<NsdFileRegistry, Long> {

    Optional<NsdFileRegistry> findByNsdIdentifierAndNsdVersion(String nsdIdentifier, String nsdVersion);
    Optional<NsdFileRegistry> findByNsdInfoId(String nsdInfoId);


}
