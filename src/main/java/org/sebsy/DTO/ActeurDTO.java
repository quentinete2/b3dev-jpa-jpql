package org.sebsy.DTO;

import java.time.LocalDate;

/**
 * DTO pour afficher les informations d'un acteur
 */
public class ActeurDTO {
    private String idImdb;
    private String identite;
    private LocalDate anniversaire;
    private String href;

    public ActeurDTO() {
    }

    public ActeurDTO(String idImdb, String identite, LocalDate anniversaire, String href) {
        this.idImdb = idImdb;
        this.identite = identite;
        this.anniversaire = anniversaire;
        this.href = href;
    }

    // Getters et Setters
    public String getIdImdb() {
        return idImdb;
    }

    public void setIdImdb(String idImdb) {
        this.idImdb = idImdb;
    }

    public String getIdentite() {
        return identite;
    }

    public void setIdentite(String identite) {
        this.identite = identite;
    }

    public LocalDate getAnniversaire() {
        return anniversaire;
    }

    public void setAnniversaire(LocalDate anniversaire) {
        this.anniversaire = anniversaire;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "ActeurDTO{" +
                "idImdb='" + idImdb + '\'' +
                ", identite='" + identite + '\'' +
                ", anniversaire=" + anniversaire +
                ", href='" + href + '\'' +
                '}';
    }
}
