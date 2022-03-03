package org.amawal.service.impl;

import java.util.Optional;
import org.amawal.domain.Utilisateur;
import org.amawal.repository.UtilisateurRepository;
import org.amawal.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        log.debug("Request to save Utilisateur : {}", utilisateur);
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Optional<Utilisateur> partialUpdate(Utilisateur utilisateur) {
        log.debug("Request to partially update Utilisateur : {}", utilisateur);

        return utilisateurRepository
            .findById(utilisateur.getId())
            .map(existingUtilisateur -> {
                if (utilisateur.getUtilisateurId() != null) {
                    existingUtilisateur.setUtilisateurId(utilisateur.getUtilisateurId());
                }
                if (utilisateur.getNom() != null) {
                    existingUtilisateur.setNom(utilisateur.getNom());
                }
                if (utilisateur.getPrenom() != null) {
                    existingUtilisateur.setPrenom(utilisateur.getPrenom());
                }
                if (utilisateur.getAdresseEmail() != null) {
                    existingUtilisateur.setAdresseEmail(utilisateur.getAdresseEmail());
                }
                if (utilisateur.getAvatar() != null) {
                    existingUtilisateur.setAvatar(utilisateur.getAvatar());
                }
                if (utilisateur.getDateInscription() != null) {
                    existingUtilisateur.setDateInscription(utilisateur.getDateInscription());
                }
                if (utilisateur.getValidProfil() != null) {
                    existingUtilisateur.setValidProfil(utilisateur.getValidProfil());
                }
                if (utilisateur.getActivateProfil() != null) {
                    existingUtilisateur.setActivateProfil(utilisateur.getActivateProfil());
                }
                if (utilisateur.getGenre() != null) {
                    existingUtilisateur.setGenre(utilisateur.getGenre());
                }
                if (utilisateur.getPassword() != null) {
                    existingUtilisateur.setPassword(utilisateur.getPassword());
                }

                return existingUtilisateur;
            })
            .map(utilisateurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Utilisateur> findAll(Pageable pageable) {
        log.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findOne(Long id) {
        log.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
    }
}
