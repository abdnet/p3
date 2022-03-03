package org.amawal.service.impl;

import java.util.List;
import java.util.Optional;
import org.amawal.domain.Groupe;
import org.amawal.repository.GroupeRepository;
import org.amawal.service.GroupeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Groupe}.
 */
@Service
@Transactional
public class GroupeServiceImpl implements GroupeService {

    private final Logger log = LoggerFactory.getLogger(GroupeServiceImpl.class);

    private final GroupeRepository groupeRepository;

    public GroupeServiceImpl(GroupeRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }

    @Override
    public Groupe save(Groupe groupe) {
        log.debug("Request to save Groupe : {}", groupe);
        return groupeRepository.save(groupe);
    }

    @Override
    public Optional<Groupe> partialUpdate(Groupe groupe) {
        log.debug("Request to partially update Groupe : {}", groupe);

        return groupeRepository
            .findById(groupe.getId())
            .map(existingGroupe -> {
                if (groupe.getGroupeId() != null) {
                    existingGroupe.setGroupeId(groupe.getGroupeId());
                }
                if (groupe.getGroupeCode() != null) {
                    existingGroupe.setGroupeCode(groupe.getGroupeCode());
                }
                if (groupe.getHabilitationLevel() != null) {
                    existingGroupe.setHabilitationLevel(groupe.getHabilitationLevel());
                }

                return existingGroupe;
            })
            .map(groupeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groupe> findAll() {
        log.debug("Request to get all Groupes");
        return groupeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Groupe> findOne(Long id) {
        log.debug("Request to get Groupe : {}", id);
        return groupeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groupe : {}", id);
        groupeRepository.deleteById(id);
    }
}
